import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ApiRequestError } from '../../core/errors/api-request.error';
import { ToastService } from '../../core/services/toast.service';
import {
  FixedExpenseTemplateResponse
} from '../../models/fixed-expense-template.models';
import { FixedExpenseTemplateService } from '../../services/fixed-expense-template.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { FieldErrorsComponent } from '../../shared/components/field-errors/field-errors.component';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { getFriendlyErrorMessage, toFieldErrorMap } from '../../shared/utils/api-error.utils';

@Component({
  selector: 'app-fixed-expenses-page',
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    CardComponent,
    AlertComponent,
    EmptyStateComponent,
    FieldErrorsComponent,
    LoadingComponent
  ],
  templateUrl: './fixed-expenses-page.component.html',
  styleUrl: './fixed-expenses-page.component.scss'
})
export class FixedExpensesPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly fixedService = inject(FixedExpenseTemplateService);
  private readonly toastService = inject(ToastService);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly generalError = signal<string | null>(null);
  readonly backendFieldErrors = signal<Record<string, string>>({});

  readonly templates = signal<FixedExpenseTemplateResponse[]>([]);
  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    defaultAmount: [0, [Validators.required, Validators.min(0)]],
    defaultDay: [1, [Validators.required, Validators.min(1), Validators.max(31)]],
    active: [true, [Validators.required]],
    autoLaunchEnabled: [true, [Validators.required]]
  });

  readonly sortedTemplates = computed(() => [...this.templates()].sort((a, b) => a.defaultDay - b.defaultDay));

  ngOnInit(): void {
    this.loadTemplates();
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.generalError.set(null);
    this.backendFieldErrors.set({});

    const values = this.form.getRawValue();
    const payload = {
      name: values.name.trim(),
      defaultAmount: this.toNumber(values.defaultAmount),
      defaultDay: this.toNumber(values.defaultDay),
      active: values.active,
      autoLaunchEnabled: values.autoLaunchEnabled
    };

    const editingId = this.editingId();
    const request$ = editingId ? this.fixedService.update(editingId, payload) : this.fixedService.create(payload);

    request$.subscribe({
      next: () => {
        this.toastService.success(editingId ? 'Gasto fixo atualizado.' : 'Gasto fixo criado com sucesso.');
        this.limpar();
        this.loadTemplates();
      },
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível guardar o gasto fixo.'));

        if (error instanceof ApiRequestError) {
          this.backendFieldErrors.set(toFieldErrorMap(error.errors));
        }
      },
      complete: () => this.saving.set(false)
    });
  }

  editar(template: FixedExpenseTemplateResponse): void {
    this.editingId.set(template.id);
    this.backendFieldErrors.set({});
    this.generalError.set(null);

    this.form.patchValue({
      name: template.name,
      defaultAmount: template.defaultAmount,
      defaultDay: template.defaultDay,
      active: template.active,
      autoLaunchEnabled: template.autoLaunchEnabled
    });
  }

  limpar(): void {
    this.editingId.set(null);
    this.backendFieldErrors.set({});
    this.generalError.set(null);

    this.form.reset({
      name: '',
      defaultAmount: 0,
      defaultDay: 1,
      active: true,
      autoLaunchEnabled: true
    });
  }

  statusLabel(template: FixedExpenseTemplateResponse): string {
    return template.active ? 'Ativo' : 'Pausado';
  }

  private loadTemplates(): void {
    this.loading.set(true);

    this.fixedService.list().subscribe({
      next: (response) => this.templates.set(response.templates),
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível carregar os gastos fixos.'));
      },
      complete: () => this.loading.set(false)
    });
  }

  private toNumber(value: unknown): number {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : 0;
  }
}
