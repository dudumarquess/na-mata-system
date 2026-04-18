import { AbstractControl } from '@angular/forms';
import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-field-errors',
  templateUrl: './field-errors.component.html',
  styleUrl: './field-errors.component.scss'
})
export class FieldErrorsComponent {
  readonly control = input<AbstractControl | null>(null);
  readonly backendError = input<string | null | undefined>(null);

  readonly localMessage = computed(() => {
    const control = this.control();

    if (!control || !control.errors || !(control.dirty || control.touched)) {
      return null;
    }

    if (control.errors['required']) {
      return 'Este campo é obrigatório.';
    }

    if (control.errors['min']) {
      return `Valor mínimo: ${control.errors['min'].min}.`;
    }

    if (control.errors['max']) {
      return `Valor máximo: ${control.errors['max'].max}.`;
    }

    if (control.errors['minlength']) {
      return `Mínimo de ${control.errors['minlength'].requiredLength} caracteres.`;
    }

    if (control.errors['maxlength']) {
      return `Máximo de ${control.errors['maxlength'].requiredLength} caracteres.`;
    }

    if (control.errors['pattern']) {
      return 'Formato inválido.';
    }

    return null;
  });
}
