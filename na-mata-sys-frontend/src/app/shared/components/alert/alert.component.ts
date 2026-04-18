import { Component, input } from '@angular/core';

export type AlertType = 'erro' | 'sucesso' | 'info';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss'
})
export class AlertComponent {
  readonly text = input.required<string>();
  readonly type = input<AlertType>('info');
}
