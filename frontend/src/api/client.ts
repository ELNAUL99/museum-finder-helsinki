import type {
  AuthResponse, Meta, MuseumDetail, MuseumSummary, SearchFilters, SearchResponse, User,
} from './types';

const BASE = import.meta.env.VITE_API_BASE_URL ?? '';
const TOKEN_KEY = 'museumfinder.token';

export const tokenStore = {
  get: (): string | null => {
    try {
      return localStorage.getItem(TOKEN_KEY);
    } catch {
      return null;
    }
  },
  set: (token: string | null) => {
    try {
      if (token) localStorage.setItem(TOKEN_KEY, token);
      else localStorage.removeItem(TOKEN_KEY);
    } catch {
      /* private browsing - the session simply will not persist */
    }
  },
};

export class ApiError extends Error {
  constructor(readonly status: number, message: string, readonly fieldErrors: Record<string, string> = {}) {
    super(message);
  }
}

async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const token = tokenStore.get();
  const response = await fetch(`${BASE}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...init.headers,
    },
  });

  if (response.status === 204) return undefined as T;

  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    let fields: Record<string, string> = {};
    try {
      const body = await response.json();
      if (body?.message) message = body.message;
      if (body?.fieldErrors) fields = body.fieldErrors;
    } catch {
      /* no JSON body */
    }
    throw new ApiError(response.status, message, fields);
  }

  return (await response.json()) as T;
}

export const api = {
  meta: () => request<Meta>('/api/meta'),
  museums: () => request<MuseumSummary[]>('/api/museums'),
  museum: (slug: string) => request<MuseumDetail>(`/api/museums/${slug}`),
  searchByQuestion: (q: string) =>
    request<SearchResponse>('/api/search', { method: 'POST', body: JSON.stringify({ q }) }),
  searchByFilters: (filters: SearchFilters, q = '') =>
    request<SearchResponse>('/api/search', { method: 'POST', body: JSON.stringify({ q, filters }) }),
  register: (email: string, password: string, displayName: string) =>
    request<AuthResponse>('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password, displayName }),
    }),
  login: (email: string, password: string) =>
    request<AuthResponse>('/api/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) }),
  me: () => request<User>('/api/auth/me'),
  favorites: () => request<MuseumSummary[]>('/api/favorites'),
  addFavorite: (museumId: number) => request<void>(`/api/favorites/${museumId}`, { method: 'PUT' }),
  removeFavorite: (museumId: number) => request<void>(`/api/favorites/${museumId}`, { method: 'DELETE' }),
};
