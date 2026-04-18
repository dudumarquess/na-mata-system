import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { OpenMonthlyRecordRequest, MonthlyRecordResponse } from '../models/monthly-record.models';
import { ApiClientService } from '../core/services/api-client.service';

@Injectable({ providedIn: 'root' })
export class MonthlyRecordService {
  constructor(private readonly apiClient: ApiClientService) {}

  openMonth(request: OpenMonthlyRecordRequest): Observable<MonthlyRecordResponse> {
    return this.apiClient.post('/api/monthly-records/open', request);
  }
}
