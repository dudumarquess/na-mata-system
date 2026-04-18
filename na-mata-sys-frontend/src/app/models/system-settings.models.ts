export interface UpdateSystemSettingsRequest {
  defaultAppFeePercentage: number;
}

export interface SystemSettingsResponse {
  id: number;
  defaultAppFeePercentage: number;
}
