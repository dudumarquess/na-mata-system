import { Injectable, signal } from '@angular/core';

export type ToastType = 'sucesso' | 'erro' | 'info';

export interface ToastMessage {
  id: number;
  type: ToastType;
  text: string;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly timeoutMs = 3500;
  private idCounter = 0;

  readonly messages = signal<ToastMessage[]>([]);

  success(text: string): void {
    this.add('sucesso', text);
  }

  error(text: string): void {
    this.add('erro', text);
  }

  info(text: string): void {
    this.add('info', text);
  }

  remove(id: number): void {
    this.messages.update((current) => current.filter((message) => message.id !== id));
  }

  private add(type: ToastType, text: string): void {
    const id = ++this.idCounter;
    this.messages.update((current) => [...current, { id, type, text }]);

    setTimeout(() => {
      this.remove(id);
    }, this.timeoutMs);
  }
}
