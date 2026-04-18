import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';

import { MonthContextStore } from '../../core/state/month-context.store';
import { MonthlySummaryResponse } from '../../models/monthly-summary.models';
import { MonthlySummaryService } from '../../services/monthly-summary.service';
import { AlertComponent } from '../../shared/components/alert/alert.component';
import { CardComponent } from '../../shared/components/card/card.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { getFriendlyErrorMessage } from '../../shared/utils/api-error.utils';

@Component({
  selector: 'app-monthly-summary-page',
  imports: [CurrencyPipe, CardComponent, AlertComponent, EmptyStateComponent, LoadingComponent],
  templateUrl: './monthly-summary-page.component.html',
  styleUrl: './monthly-summary-page.component.scss'
})
export class MonthlySummaryPageComponent implements OnInit {
  private readonly summaryService = inject(MonthlySummaryService);
  readonly monthStore = inject(MonthContextStore);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly summary = signal<MonthlySummaryResponse | null>(null);

  ngOnInit(): void {
    this.load();
  }

  atualizar(): void {
    this.load();
  }

  private load(): void {
    const year = this.monthStore.selectedYear();
    const month = this.monthStore.selectedMonth();

    if (!year || !month) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.summaryService.getSummary(year, month).subscribe({
      next: (summary) => this.summary.set(summary),
      error: (error: unknown) => this.error.set(getFriendlyErrorMessage(error, 'Não foi possível carregar o resumo.')),
      complete: () => this.loading.set(false)
    });
  }
}
