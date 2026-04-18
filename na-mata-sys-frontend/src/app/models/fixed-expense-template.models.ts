export interface CreateFixedExpenseTemplateRequest {
  name: string;
  defaultAmount: number;
  defaultDay: number;
  active: boolean;
  autoLaunchEnabled: boolean;
}

export interface UpdateFixedExpenseTemplateRequest {
  name: string;
  defaultAmount: number;
  defaultDay: number;
  active: boolean;
  autoLaunchEnabled: boolean;
}

export interface FixedExpenseTemplateResponse {
  id: number;
  name: string;
  defaultAmount: number;
  defaultDay: number;
  active: boolean;
  autoLaunchEnabled: boolean;
}

export interface FixedExpenseTemplateListResponse {
  templates: FixedExpenseTemplateResponse[];
}
