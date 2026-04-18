export interface CreateDailyRevenueEntryRequest {
  year: number;
  month: number;
  day: number;
  cashAmount: number;
  multibancoAmount: number;
  appsGrossAmount: number;
  otherIncomeAmount: number;
  appFeePercentageUsed?: number | null;
}

export interface UpdateDailyRevenueEntryRequest {
  day: number;
  cashAmount: number;
  multibancoAmount: number;
  appsGrossAmount: number;
  otherIncomeAmount: number;
  appFeePercentageUsed?: number | null;
}

export interface DailyRevenueEntryResponse {
  id: number;
  monthlyRecordId: number;
  day: number;
  cashAmount: number;
  multibancoAmount: number;
  appsGrossAmount: number;
  otherIncomeAmount: number;
  appFeePercentageUsed: number;
  officialAmount: number;
  realAmount: number;
}

export interface MonthlyDailyRevenueListResponse {
  year: number;
  month: number;
  entries: DailyRevenueEntryResponse[];
}
