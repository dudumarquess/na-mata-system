import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { MonthContextStore } from '../../core/state/month-context.store';
import { MonthlySummaryResponse } from '../../models/monthly-summary.models';
import { MonthlySummaryService } from '../../services/monthly-summary.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { getFriendlyErrorMessage } from '../../shared/utils/api-error.utils';

interface QuickAction {
  label: string;
  subtitle: string;
  route: string;
}

@Component({
  selector: 'app-month-dashboard-page',
  imports: [RouterLink, CurrencyPipe, CardComponent, AlertComponent, EmptyStateComponent, LoadingComponent],
  templateUrl: './month-dashboard-page.component.html',
  styleUrl: './month-dashboard-page.component.scss'
})
export class MonthDashboardPageComponent implements OnInit {
  private readonly summaryService = inject(MonthlySummaryService);
  readonly monthStore = inject(MonthContextStore);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly summary = signal<MonthlySummaryResponse | null>(null);

  readonly quickActions: QuickAction[] = [
    { label: 'Registrar receita do dia', subtitle: 'Adicionar ou editar receitas', route: '/receitas' },
    { label: 'Registrar gasto', subtitle: 'Guardar gastos do mês', route: '/gastos' },
    { label: 'Ver resumo do mês', subtitle: 'Acompanhar resultados', route: '/resumo' },
    { label: 'Gastos fixos', subtitle: 'Gerir lançamentos recorrentes', route: '/gastos-fixos' },
    { label: 'Configurações', subtitle: 'Ajustar taxa de apps', route: '/configuracoes' }
  ];

  readonly hasNumbers = computed(() => {
    const summary = this.summary();
    return !!summary && (summary.totalOfficial > 0 || summary.totalExpenses > 0);
  });

  ngOnInit(): void {
    this.loadSummary();
  }

  refresh(): void {
    this.loadSummary();
  }

  private loadSummary(): void {
    const year = this.monthStore.selectedYear();
    const month = this.monthStore.selectedMonth();

    if (!year || !month) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.summaryService.getSummary(year, month).subscribe({
      next: (response) => this.summary.set(response),
      error: (error: unknown) => this.error.set(getFriendlyErrorMessage(error, 'Não foi possível carregar o resumo.')),
      complete: () => this.loading.set(false)
    });
  }
}
