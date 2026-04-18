import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ApiRequestError } from '../../core/errors/api-request.error';
import { ToastService } from '../../core/services/toast.service';
import { MonthContextStore } from '../../core/state/month-context.store';
import { DailyRevenueEntryResponse } from '../../models/daily-revenue.models';
import { DailyRevenueService } from '../../services/daily-revenue.service';
import { SystemSettingsService } from '../../services/system-settings.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { FieldErrorsComponent } from '../../shared/components/field-errors/field-errors.component';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { getFriendlyErrorMessage, toFieldErrorMap } from '../../shared/utils/api-error.utils';

interface SavedRevenueCalculation {
  day: number;
  officialAmount: number;
  appFeeAmount: number;
  realAmount: number;
  otherIncomeAmount: number;
  appFeePercentageUsed: number;
}

@Component({
  selector: 'app-daily-revenue-page',
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    DecimalPipe,
    CardComponent,
    AlertComponent,
    EmptyStateComponent,
    FieldErrorsComponent,
    LoadingComponent
  ],
  templateUrl: './daily-revenue-page.component.html',
  styleUrl: './daily-revenue-page.component.scss'
})
export class DailyRevenuePageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly monthStore = inject(MonthContextStore);
  private readonly dailyRevenueService = inject(DailyRevenueService);
  private readonly settingsService = inject(SystemSettingsService);
  private readonly toastService = inject(ToastService);

  readonly loadingEntries = signal(true);
  readonly loadingFee = signal(true);
  readonly saving = signal(false);

  readonly entries = signal<DailyRevenueEntryResponse[]>([]);
  readonly appFeePercentage = signal(30);
  readonly editingEntryId = signal<number | null>(null);
  readonly savedCalculation = signal<SavedRevenueCalculation | null>(null);

  readonly generalError = signal<string | null>(null);
  readonly backendFieldErrors = signal<Record<string, string>>({});

  readonly form = this.fb.nonNullable.group({
    day: [this.getSuggestedDay(), [Validators.required, Validators.min(1), Validators.max(31)]],
    cashAmount: [0, [Validators.required, Validators.min(0)]],
    multibancoAmount: [0, [Validators.required, Validators.min(0)]],
    appsGrossAmount: [0, [Validators.required, Validators.min(0)]],
    otherIncomeAmount: [0, [Validators.required, Validators.min(0)]]
  });

  readonly sortedEntries = computed(() => [...this.entries()].sort((a, b) => a.day - b.day));

  ngOnInit(): void {
    this.loadFee();
    this.loadEntries();
  }

  editar(entry: DailyRevenueEntryResponse): void {
    this.editingEntryId.set(entry.id);
    this.backendFieldErrors.set({});
    this.generalError.set(null);

    this.form.patchValue({
      day: entry.day,
      cashAmount: entry.cashAmount,
      multibancoAmount: entry.multibancoAmount,
      appsGrossAmount: entry.appsGrossAmount,
      otherIncomeAmount: entry.otherIncomeAmount
    });
  }

  limparFormulario(): void {
    this.editingEntryId.set(null);
    this.backendFieldErrors.set({});
    this.generalError.set(null);
    this.form.reset({
      day: this.getSuggestedDay(),
      cashAmount: 0,
      multibancoAmount: 0,
      appsGrossAmount: 0,
      otherIncomeAmount: 0
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const year = this.monthStore.selectedYear();
    const month = this.monthStore.selectedMonth();

    if (!year || !month) {
      return;
    }

    const values = this.form.getRawValue();
    this.saving.set(true);
    this.generalError.set(null);
    this.backendFieldErrors.set({});

    const payload = {
      day: this.toNumber(values.day),
      cashAmount: this.toNumber(values.cashAmount),
      multibancoAmount: this.toNumber(values.multibancoAmount),
      appsGrossAmount: this.toNumber(values.appsGrossAmount),
      otherIncomeAmount: this.toNumber(values.otherIncomeAmount),
      appFeePercentageUsed: this.appFeePercentage()
    };

    const editingId = this.editingEntryId();

    const request$ = editingId
      ? this.dailyRevenueService.update(editingId, payload)
      : this.dailyRevenueService.create({ year, month, ...payload });

    request$.subscribe({
      next: (savedEntry) => {
        const officialAmount = this.toNumber(savedEntry.officialAmount);
        const realAmount = this.toNumber(savedEntry.realAmount);

        this.savedCalculation.set({
          day: this.toNumber(savedEntry.day),
          officialAmount,
          appFeeAmount: officialAmount - realAmount,
          realAmount,
          otherIncomeAmount: this.toNumber(savedEntry.otherIncomeAmount),
          appFeePercentageUsed: this.toNumber(savedEntry.appFeePercentageUsed)
        });

        this.toastService.success(editingId ? 'Receita atualizada.' : 'Receita guardada com sucesso.');
        this.limparFormulario();
        this.loadEntries();
      },
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível guardar a receita.'));

        if (error instanceof ApiRequestError) {
          this.backendFieldErrors.set(toFieldErrorMap(error.errors));
        }
      },
      complete: () => this.saving.set(false)
    });
  }

  private loadEntries(): void {
    const year = this.monthStore.selectedYear();
    const month = this.monthStore.selectedMonth();

    if (!year || !month) {
      return;
    }

    this.loadingEntries.set(true);

    this.dailyRevenueService.listByMonth(year, month).subscribe({
      next: (response) => this.entries.set(response.entries),
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível carregar as receitas.'));
      },
      complete: () => this.loadingEntries.set(false)
    });
  }

  private loadFee(): void {
    this.loadingFee.set(true);

    this.settingsService.getSettings().subscribe({
      next: (settings) => this.appFeePercentage.set(this.toNumber(settings.defaultAppFeePercentage) || 30),
      error: () => this.appFeePercentage.set(30),
      complete: () => this.loadingFee.set(false)
    });
  }

  private getSuggestedDay(): number {
    const now = new Date();
    const currentYear = now.getFullYear();
    const currentMonth = now.getMonth() + 1;

    if (this.monthStore.selectedYear() === currentYear && this.monthStore.selectedMonth() === currentMonth) {
      return now.getDate();
    }

    return 1;
  }

  private toNumber(value: unknown): number {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : 0;
  }
}
