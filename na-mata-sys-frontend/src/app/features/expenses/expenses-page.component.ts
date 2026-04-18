import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ApiRequestError } from '../../core/errors/api-request.error';
import { ToastService } from '../../core/services/toast.service';
import { MonthContextStore } from '../../core/state/month-context.store';
import { ExpenseEntryResponse, ExpenseNature } from '../../models/expense.models';
import { ExpenseService } from '../../services/expense.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { ConfirmDialogComponent } from '../../shared/components/confirm-dialog/confirm-dialog.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { FieldErrorsComponent } from '../../shared/components/field-errors/field-errors.component';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { getFriendlyErrorMessage, toFieldErrorMap } from '../../shared/utils/api-error.utils';

@Component({
  selector: 'app-expenses-page',
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    CardComponent,
    AlertComponent,
    ConfirmDialogComponent,
    EmptyStateComponent,
    FieldErrorsComponent,
    LoadingComponent
  ],
  templateUrl: './expenses-page.component.html',
  styleUrl: './expenses-page.component.scss'
})
export class ExpensesPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly expenseService = inject(ExpenseService);
  private readonly monthStore = inject(MonthContextStore);
  private readonly toastService = inject(ToastService);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly deleting = signal(false);

  readonly entries = signal<ExpenseEntryResponse[]>([]);
  readonly editingId = signal<number | null>(null);
  readonly entryPendingDelete = signal<ExpenseEntryResponse | null>(null);

  readonly generalError = signal<string | null>(null);
  readonly backendFieldErrors = signal<Record<string, string>>({});

  readonly form = this.fb.nonNullable.group({
    day: [this.getSuggestedDay(), [Validators.required, Validators.min(1), Validators.max(31)]],
    description: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]],
    amount: [0, [Validators.required, Validators.min(0)]],
    expenseType: ['VARIABLE' as ExpenseNature, [Validators.required]]
  });

  readonly sortedEntries = computed(() => [...this.entries()].sort((a, b) => a.day - b.day));

  ngOnInit(): void {
    this.loadEntries();
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

    this.saving.set(true);
    this.generalError.set(null);
    this.backendFieldErrors.set({});

    const values = this.form.getRawValue();
    const payload = {
      day: this.toNumber(values.day),
      description: values.description.trim(),
      amount: this.toNumber(values.amount),
      expenseType: values.expenseType,
      fixedExpenseTemplateId: null
    };

    const editingId = this.editingId();

    const request$ = editingId
      ? this.expenseService.update(editingId, payload)
      : this.expenseService.create({ year, month, ...payload });

    request$.subscribe({
      next: () => {
        this.toastService.success(editingId ? 'Gasto atualizado.' : 'Gasto registado com sucesso.');
        this.limpar();
        this.loadEntries();
      },
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível guardar o gasto.'));

        if (error instanceof ApiRequestError) {
          this.backendFieldErrors.set(toFieldErrorMap(error.errors));
        }
      },
      complete: () => this.saving.set(false)
    });
  }

  editar(entry: ExpenseEntryResponse): void {
    this.editingId.set(entry.id);
    this.backendFieldErrors.set({});
    this.generalError.set(null);
    this.form.patchValue({
      day: entry.day,
      description: entry.description,
      amount: entry.amount,
      expenseType: entry.expenseType
    });
  }

  pedirConfirmacaoEliminar(entry: ExpenseEntryResponse): void {
    this.entryPendingDelete.set(entry);
  }

  cancelarEliminar(): void {
    this.entryPendingDelete.set(null);
  }

  confirmarEliminar(): void {
    const entry = this.entryPendingDelete();

    if (!entry) {
      return;
    }

    this.deleting.set(true);

    this.expenseService.delete(entry.id).subscribe({
      next: () => {
        this.toastService.success('Gasto eliminado.');
        this.entryPendingDelete.set(null);
        this.loadEntries();
      },
      error: (error: unknown) => {
        this.toastService.error(getFriendlyErrorMessage(error, 'Não foi possível eliminar o gasto.'));
      },
      complete: () => this.deleting.set(false)
    });
  }

  limpar(): void {
    this.editingId.set(null);
    this.backendFieldErrors.set({});
    this.generalError.set(null);
    this.form.reset({
      day: this.getSuggestedDay(),
      description: '',
      amount: 0,
      expenseType: 'VARIABLE'
    });
  }

  expenseTypeLabel(type: ExpenseNature): string {
    return type === 'FIXED' ? 'Fixo' : 'Variável';
  }

  private loadEntries(): void {
    const year = this.monthStore.selectedYear();
    const month = this.monthStore.selectedMonth();

    if (!year || !month) {
      return;
    }

    this.loading.set(true);

    this.expenseService.listByMonth(year, month).subscribe({
      next: (response) => this.entries.set(response.entries),
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível carregar os gastos.'));
      },
      complete: () => this.loading.set(false)
    });
  }

  private getSuggestedDay(): number {
    const now = new Date();

    if (
      this.monthStore.selectedYear() === now.getFullYear() &&
      this.monthStore.selectedMonth() === now.getMonth() + 1
    ) {
      return now.getDate();
    }

    return 1;
  }

  private toNumber(value: unknown): number {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : 0;
  }
}
