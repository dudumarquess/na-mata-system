import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClientService } from '../core/services/api-client.service';
import {
  CreateDailyRevenueEntryRequest,
  DailyRevenueEntryResponse,
  MonthlyDailyRevenueListResponse,
  UpdateDailyRevenueEntryRequest
} from '../models/daily-revenue.models';

@Injectable({ providedIn: 'root' })
export class DailyRevenueService {
  constructor(private readonly apiClient: ApiClientService) {}

  listByMonth(year: number, month: number): Observable<MonthlyDailyRevenueListResponse> {
    return this.apiClient.get('/api/daily-revenues', { year, month });
  }

  create(request: CreateDailyRevenueEntryRequest): Observable<DailyRevenueEntryResponse> {
    return this.apiClient.post('/api/daily-revenues', request);
  }

  update(id: number, request: UpdateDailyRevenueEntryRequest): Observable<DailyRevenueEntryResponse> {
    return this.apiClient.put(`/api/daily-revenues/${id}`, request);
  }
}
