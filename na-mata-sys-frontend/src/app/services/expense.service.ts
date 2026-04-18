import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClientService } from '../core/services/api-client.service';
import {
  CreateExpenseEntryRequest,
  ExpenseEntryResponse,
  MonthlyExpenseListResponse,
  UpdateExpenseEntryRequest
} from '../models/expense.models';

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  constructor(private readonly apiClient: ApiClientService) {}

  listByMonth(year: number, month: number): Observable<MonthlyExpenseListResponse> {
    return this.apiClient.get('/api/expenses', { year, month });
  }

  create(request: CreateExpenseEntryRequest): Observable<ExpenseEntryResponse> {
    return this.apiClient.post('/api/expenses', request);
  }

  update(id: number, request: UpdateExpenseEntryRequest): Observable<ExpenseEntryResponse> {
    return this.apiClient.put(`/api/expenses/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.apiClient.delete(`/api/expenses/${id}`);
  }
}
