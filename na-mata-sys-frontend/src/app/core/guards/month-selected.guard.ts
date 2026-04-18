import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { MonthContextStore } from '../state/month-context.store';

export const monthSelectedGuard: CanActivateFn = () => {
  const monthStore = inject(MonthContextStore);
  const router = inject(Router);

  if (monthStore.hasSelection()) {
    return true;
  }

  return router.createUrlTree(['/mes']);
};
