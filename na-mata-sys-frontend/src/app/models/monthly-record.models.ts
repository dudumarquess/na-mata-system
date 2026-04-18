export type MonthStatus = 'OPEN' | 'CLOSED';

export interface OpenMonthlyRecordRequest {
  year: number;
  month: number;
}

export interface MonthlyRecordResponse {
  id: number;
  year: number;
  month: number;
  status: MonthStatus;
}
