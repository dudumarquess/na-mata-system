import { computed, Injectable, signal } from '@angular/core';

import { MONTHS_PT } from '../../models/month.models';
import { MonthlyRecordResponse } from '../../models/monthly-record.models';

interface MonthPersistedState {
  year: number;
  month: number;
  openedMonthlyRecord: MonthlyRecordResponse | null;
}

@Injectable({ providedIn: 'root' })
export class MonthContextStore {
  private readonly storageKey = 'na-mata:selected-month';

  readonly selectedYear = signal<number | null>(null);
  readonly selectedMonth = signal<number | null>(null);
  readonly openedMonthlyRecord = signal<MonthlyRecordResponse | null>(null);

  readonly hasSelection = computed(() => this.selectedYear() !== null && this.selectedMonth() !== null);

  readonly label = computed(() => {
    const year = this.selectedYear();
    const month = this.selectedMonth();

    if (!year || !month) {
      return 'Mês não selecionado';
    }

    const monthName = MONTHS_PT.find((item) => item.value === month)?.label ?? `Mês ${month}`;
    return `${monthName} de ${year}`;
  });

  constructor() {
    this.restore();
  }

  setSelection(year: number, month: number, openedMonthlyRecord: MonthlyRecordResponse | null): void {
    this.selectedYear.set(year);
    this.selectedMonth.set(month);
    this.openedMonthlyRecord.set(openedMonthlyRecord);
    this.persist();
  }

  clear(): void {
    this.selectedYear.set(null);
    this.selectedMonth.set(null);
    this.openedMonthlyRecord.set(null);
    localStorage.removeItem(this.storageKey);
  }

  private persist(): void {
    const year = this.selectedYear();
    const month = this.selectedMonth();

    if (year === null || month === null) {
      return;
    }

    const payload: MonthPersistedState = {
      year,
      month,
      openedMonthlyRecord: this.openedMonthlyRecord()
    };

    localStorage.setItem(this.storageKey, JSON.stringify(payload));
  }

  private restore(): void {
    const raw = localStorage.getItem(this.storageKey);

    if (!raw) {
      return;
    }

    try {
      const parsed = JSON.parse(raw) as Partial<MonthPersistedState>;

      if (typeof parsed.year === 'number' && typeof parsed.month === 'number') {
        this.selectedYear.set(parsed.year);
        this.selectedMonth.set(parsed.month);
        this.openedMonthlyRecord.set(parsed.openedMonthlyRecord ?? null);
      }
    } catch {
      localStorage.removeItem(this.storageKey);
    }
  }
}
