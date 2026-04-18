import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { ApiRequestError } from '../../core/errors/api-request.error';
import { ToastService } from '../../core/services/toast.service';
import { MonthContextStore } from '../../core/state/month-context.store';
import { MONTHS_PT } from '../../models/month.models';
import { MonthlyRecordService } from '../../services/monthly-record.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { FieldErrorsComponent } from '../../shared/components/field-errors/field-errors.component';
import { getFriendlyErrorMessage, toFieldErrorMap } from '../../shared/utils/api-error.utils';

@Component({
  selector: 'app-month-selection-page',
  imports: [ReactiveFormsModule, CardComponent, AlertComponent, FieldErrorsComponent],
  templateUrl: './month-selection-page.component.html',
  styleUrl: './month-selection-page.component.scss'
})
export class MonthSelectionPageComponent {
  private readonly monthlyRecordService = inject(MonthlyRecordService);
  readonly monthStore = inject(MonthContextStore);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  readonly loading = signal(false);
  readonly generalError = signal<string | null>(null);
  readonly backendFieldErrors = signal<Record<string, string>>({});

  readonly months = MONTHS_PT;
  readonly years = this.buildYearOptions();

  private readonly currentDate = new Date();

  readonly form = this.fb.nonNullable.group({
    year: [
      this.monthStore.selectedYear() ?? this.currentDate.getFullYear(),
      [Validators.required, Validators.min(2000)]
    ],
    month: [
      this.monthStore.selectedMonth() ?? this.currentDate.getMonth() + 1,
      [Validators.required, Validators.min(1), Validators.max(12)]
    ]
  });

  entrarMesAtual(): void {
    void this.router.navigateByUrl('/inicio');
  }

  abrirMes(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.generalError.set(null);
    this.backendFieldErrors.set({});

    const { year, month } = this.form.getRawValue();

    this.monthlyRecordService.openMonth({ year, month }).subscribe({
      next: (record) => {
        this.monthStore.setSelection(year, month, record);
        this.toastService.success('Mês aberto com sucesso.');
        void this.router.navigateByUrl('/inicio');
      },
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível abrir o mês.'));

        if (error instanceof ApiRequestError) {
          this.backendFieldErrors.set(toFieldErrorMap(error.errors));
        }
      },
      complete: () => this.loading.set(false)
    });
  }

  private buildYearOptions(): number[] {
    const year = new Date().getFullYear();
    return [year - 1, year, year + 1, year + 2];
  }
}
