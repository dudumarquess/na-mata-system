import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { MonthContextStore } from '../core/state/month-context.store';

interface NavigationItem {
  label: string;
  path: string;
}

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app-shell.component.html',
  styleUrl: './app-shell.component.scss'
})
export class AppShellComponent {
  readonly monthStore = inject(MonthContextStore);

  readonly navigation: NavigationItem[] = [
    { label: 'Início', path: '/inicio' },
    { label: 'Receitas', path: '/receitas' },
    { label: 'Gastos', path: '/gastos' },
    { label: 'Resumo', path: '/resumo' },
    { label: 'Fixos', path: '/gastos-fixos' },
    { label: 'Config.', path: '/configuracoes' }
  ];
}
