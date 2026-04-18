import { Routes } from '@angular/router';

import { monthSelectedGuard } from './core/guards/month-selected.guard';
import { MonthDashboardPageComponent } from './features/dashboard/month-dashboard-page.component';
import { DailyRevenuePageComponent } from './features/daily-revenue/daily-revenue-page.component';
import { ExpensesPageComponent } from './features/expenses/expenses-page.component';
import { FixedExpensesPageComponent } from './features/fixed-expenses/fixed-expenses-page.component';
import { MonthSelectionPageComponent } from './features/month-selection/month-selection-page.component';
import { SettingsPageComponent } from './features/settings/settings-page.component';
import { MonthlySummaryPageComponent } from './features/summary/monthly-summary-page.component';
import { AppShellComponent } from './layout/app-shell.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'mes'
  },
  {
    path: 'mes',
    component: MonthSelectionPageComponent
  },
  {
    path: '',
    component: AppShellComponent,
    canActivate: [monthSelectedGuard],
    children: [
      {
        path: 'inicio',
        component: MonthDashboardPageComponent
      },
      {
        path: 'receitas',
        component: DailyRevenuePageComponent
      },
      {
        path: 'gastos',
        component: ExpensesPageComponent
      },
      {
        path: 'resumo',
        component: MonthlySummaryPageComponent
      },
      {
        path: 'gastos-fixos',
        component: FixedExpensesPageComponent
      },
      {
        path: 'configuracoes',
        component: SettingsPageComponent
      },
      {
        path: '**',
        redirectTo: 'inicio'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'mes'
  }
];
