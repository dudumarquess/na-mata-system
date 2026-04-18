import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiClientService } from '../core/services/api-client.service';
import { SystemSettingsResponse, UpdateSystemSettingsRequest } from '../models/system-settings.models';

@Injectable({ providedIn: 'root' })
export class SystemSettingsService {
  constructor(private readonly apiClient: ApiClientService) {}

  getSettings(): Observable<SystemSettingsResponse> {
    return this.apiClient.get('/api/system-settings');
  }

  updateSettings(request: UpdateSystemSettingsRequest): Observable<SystemSettingsResponse> {
    return this.apiClient.put('/api/system-settings', request);
  }
}
