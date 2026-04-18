import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClientService } from '../core/services/api-client.service';
import {
  CreateFixedExpenseTemplateRequest,
  FixedExpenseTemplateListResponse,
  FixedExpenseTemplateResponse,
  UpdateFixedExpenseTemplateRequest
} from '../models/fixed-expense-template.models';

@Injectable({ providedIn: 'root' })
export class FixedExpenseTemplateService {
  constructor(private readonly apiClient: ApiClientService) {}

  list(): Observable<FixedExpenseTemplateListResponse> {
    return this.apiClient.get('/api/fixed-expense-templates');
  }

  create(request: CreateFixedExpenseTemplateRequest): Observable<FixedExpenseTemplateResponse> {
    return this.apiClient.post('/api/fixed-expense-templates', request);
  }

  update(id: number, request: UpdateFixedExpenseTemplateRequest): Observable<FixedExpenseTemplateResponse> {
    return this.apiClient.put(`/api/fixed-expense-templates/${id}`, request);
  }
}
