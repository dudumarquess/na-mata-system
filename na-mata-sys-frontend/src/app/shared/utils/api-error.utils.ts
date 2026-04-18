import { ApiFieldError } from '../../models/api.models';
import { ApiRequestError } from '../../core/errors/api-request.error';

export type FieldErrorMap = Record<string, string>;

export function toFieldErrorMap(errors: ApiFieldError[] = []): FieldErrorMap {
  return errors.reduce<FieldErrorMap>((acc, error) => {
    if (error.field && !acc[error.field]) {
      acc[error.field] = error.message;
    }

    return acc;
  }, {});
}

export function getFriendlyErrorMessage(error: unknown, fallback = 'Não foi possível completar a ação.'): string {
  if (error instanceof ApiRequestError) {
    return error.message || fallback;
  }

  return fallback;
}
