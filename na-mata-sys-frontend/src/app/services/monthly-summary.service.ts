import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClientService } from '../core/services/api-client.service';
import { MonthlySummaryResponse } from '../models/monthly-summary.models';

@Injectable({ providedIn: 'root' })
export class MonthlySummaryService {
  constructor(private readonly apiClient: ApiClientService) {}

  getSummary(year: number, month: number): Observable<MonthlySummaryResponse> {
    return this.apiClient.get('/api/monthly-summaries', { year, month });
  }
}
