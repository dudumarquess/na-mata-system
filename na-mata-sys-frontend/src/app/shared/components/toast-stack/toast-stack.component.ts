import { Component, inject } from '@angular/core';

import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-toast-stack',
  templateUrl: './toast-stack.component.html',
  styleUrl: './toast-stack.component.scss'
})
export class ToastStackComponent {
  readonly toastService = inject(ToastService);
}
