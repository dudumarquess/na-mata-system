import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ApiRequestError } from '../../core/errors/api-request.error';
import { ToastService } from '../../core/services/toast.service';
import { SystemSettingsService } from '../../services/system-settings.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { FieldErrorsComponent } from '../../shared/components/field-errors/field-errors.component';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { getFriendlyErrorMessage, toFieldErrorMap } from '../../shared/utils/api-error.utils';

@Component({
  selector: 'app-settings-page',
  imports: [ReactiveFormsModule, CardComponent, AlertComponent, FieldErrorsComponent, LoadingComponent],
  templateUrl: './settings-page.component.html',
  styleUrl: './settings-page.component.scss'
})
export class SettingsPageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly settingsService = inject(SystemSettingsService);
  private readonly toastService = inject(ToastService);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly generalError = signal<string | null>(null);
  readonly backendFieldErrors = signal<Record<string, string>>({});

  readonly form = this.fb.nonNullable.group({
    defaultAppFeePercentage: [30, [Validators.required, Validators.min(0), Validators.max(100)]]
  });

  ngOnInit(): void {
    this.load();
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.generalError.set(null);
    this.backendFieldErrors.set({});

    const payload = {
      defaultAppFeePercentage: Number(this.form.controls.defaultAppFeePercentage.getRawValue())
    };

    this.settingsService.updateSettings(payload).subscribe({
      next: (response) => {
        this.form.patchValue({ defaultAppFeePercentage: response.defaultAppFeePercentage });
        this.toastService.success('Configurações atualizadas.');
      },
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível atualizar as configurações.'));

        if (error instanceof ApiRequestError) {
          this.backendFieldErrors.set(toFieldErrorMap(error.errors));
        }
      },
      complete: () => this.saving.set(false)
    });
  }

  private load(): void {
    this.loading.set(true);
    this.generalError.set(null);

    this.settingsService.getSettings().subscribe({
      next: (settings) => {
        this.form.patchValue({ defaultAppFeePercentage: settings.defaultAppFeePercentage });
      },
      error: (error: unknown) => {
        this.generalError.set(getFriendlyErrorMessage(error, 'Não foi possível carregar as configurações.'));
      },
      complete: () => this.loading.set(false)
    });
  }
}
