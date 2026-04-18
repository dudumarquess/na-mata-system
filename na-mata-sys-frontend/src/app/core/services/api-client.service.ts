import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, throwError } from 'rxjs';

import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../models/api.models';
import { ApiRequestError } from '../errors/api-request.error';

@Injectable({ providedIn: 'root' })
export class ApiClientService {
  constructor(private readonly http: HttpClient) {}

  get<T>(path: string, query?: Record<string, string | number | boolean | null | undefined>): Observable<T> {
    const params = this.toParams(query);
    return this.unwrap(this.http.get<ApiResponse<T>>(this.toUrl(path), { params }));
  }

  post<T>(path: string, body: unknown): Observable<T> {
    return this.unwrap(this.http.post<ApiResponse<T>>(this.toUrl(path), body));
  }

  put<T>(path: string, body: unknown): Observable<T> {
    return this.unwrap(this.http.put<ApiResponse<T>>(this.toUrl(path), body));
  }

  delete(path: string): Observable<void> {
    return this.unwrap<null>(this.http.delete<ApiResponse<null>>(this.toUrl(path))).pipe(map(() => undefined));
  }

  private unwrap<T>(request: Observable<ApiResponse<T>>): Observable<T> {
    return request.pipe(
      map((response) => {
        if (response?.success) {
          return response.data as T;
        }

        throw new ApiRequestError(
          response?.message || 'Não foi possível completar o pedido.',
          response?.errors || []
        );
      }),
      catchError((error: unknown) => {
        if (error instanceof ApiRequestError) {
          return throwError(() => error);
        }

        if (error instanceof HttpErrorResponse) {
          const payload = error.error as ApiResponse<T> | undefined;

          if (payload && typeof payload === 'object' && 'success' in payload) {
            return throwError(
              () =>
                new ApiRequestError(
                  payload.message || 'Não foi possível completar o pedido.',
                  payload.errors || [],
                  error.status
                )
            );
          }

          const message =
            error.status === 0
              ? 'Sem ligação ao servidor. Verifique a internet e tente novamente.'
              : 'Ocorreu um erro inesperado. Tente novamente.';

          return throwError(() => new ApiRequestError(message, [], error.status));
        }

        return throwError(() => new ApiRequestError('Ocorreu um erro inesperado.'));
      })
    );
  }

  private toParams(query?: Record<string, string | number | boolean | null | undefined>): HttpParams {
    if (!query) {
      return new HttpParams();
    }

    let params = new HttpParams();

    Object.entries(query).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.set(key, String(value));
      }
    });

    return params;
  }

  private toUrl(path: string): string {
    const base = environment.apiBaseUrl.replace(/\/+$/, '');
    const cleanPath = path.startsWith('/') ? path : `/${path}`;
    return `${base}${cleanPath}`;
  }
}
