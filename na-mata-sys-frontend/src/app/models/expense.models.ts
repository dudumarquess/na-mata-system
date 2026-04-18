export type ExpenseNature = 'FIXED' | 'VARIABLE';

export interface CreateExpenseEntryRequest {
  year: number;
  month: number;
  day: number;
  description: string;
  amount: number;
  expenseType: ExpenseNature;
  fixedExpenseTemplateId?: number | null;
}

export interface UpdateExpenseEntryRequest {
  day: number;
  description: string;
  amount: number;
  expenseType: ExpenseNature;
  fixedExpenseTemplateId?: number | null;
}

export interface ExpenseEntryResponse {
  id: number;
  monthlyRecordId: number;
  day: number;
  description: string;
  amount: number;
  expenseType: ExpenseNature;
  fixedExpenseTemplateId: number | null;
}

export interface MonthlyExpenseListResponse {
  year: number;
  month: number;
  entries: ExpenseEntryResponse[];
}
