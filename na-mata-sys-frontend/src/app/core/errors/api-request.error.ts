import { ApiFieldError } from '../../models/api.models';

export class ApiRequestError extends Error {
  constructor(
    public override readonly message: string,
    public readonly errors: ApiFieldError[] = [],
    public readonly statusCode = 0
  ) {
    super(message);
    this.name = 'ApiRequestError';
  }
}
