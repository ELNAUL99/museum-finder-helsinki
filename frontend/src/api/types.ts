export interface ThemeDto {
  value: string;
  label: string;
}

export interface MuseumSummary {
  id: number;
  slug: string;
  name: string;
  shortDescription: string;
  district: string;
  address: string;
  imageUrl?: string;
  adultPriceEur: number;
  freeEntry: boolean;
  freeEntryNote?: string;
  museumCard: boolean;
  wheelchairAccessible: boolean;
  familyFriendly: boolean;
  hasCafe: boolean;
  hasShop: boolean;
  latitude: number;
  longitude: number;
  themes: ThemeDto[];
  openToday: boolean;
  todayHours: string;
  distanceKm?: number;
  matchedKeywords: string[];
  favorite: boolean;
}

export interface DayHours {
  day: number;
  dayName: string;
  closed: boolean;
  opensAt?: string;
  closesAt?: string;
}

export interface Exhibition {
  id: number;
  title: string;
  description: string;
  startDate: string;
  endDate?: string;
  imageUrl?: string;
  permanent: boolean;
  running: boolean;
}

export interface MuseumDetail {
  summary: MuseumSummary;
  description: string;
  postalCode: string;
  website?: string;
  phone?: string;
  email?: string;
  openingHours: DayHours[];
  exhibitions: Exhibition[];
}

export type DayFilter =
  | 'ANY' | 'TODAY' | 'TOMORROW' | 'WEEKEND'
  | 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

export type SortOrder = 'RELEVANCE' | 'DISTANCE' | 'PRICE_ASC' | 'NAME';

export interface SearchFilters {
  themes: string[];
  freeOnly: boolean;
  museumCardOnly: boolean;
  maxPriceEur: number;
  openOn: DayFilter;
  openNow: boolean;
  nearPlace: string;
  radiusKm: number;
  wheelchairAccessible: boolean;
  familyFriendly: boolean;
  hasCafe: boolean;
  keywords: string[];
  sort: SortOrder;
  interpretation: string;
}

export interface SearchResponse {
  query: string;
  filters: SearchFilters;
  interpretedBy: AiProvider | 'filters';
  total: number;
  results: MuseumSummary[];
  note?: string;
}

export type AiProvider = 'claude' | 'mistral' | 'keyword';

export interface Meta {
  themes: ThemeDto[];
  places: string[];
  aiSearchEnabled: boolean;
  aiProvider: AiProvider;
  examples: string[];
}

export interface User {
  id: number;
  email: string;
  displayName: string;
}

export interface AuthResponse {
  token: string;
  expiresInSeconds: number;
  user: User;
}

export const emptyFilters = (): SearchFilters => ({
  themes: [],
  freeOnly: false,
  museumCardOnly: false,
  maxPriceEur: -1,
  openOn: 'ANY',
  openNow: false,
  nearPlace: '',
  radiusKm: 0,
  wheelchairAccessible: false,
  familyFriendly: false,
  hasCafe: false,
  keywords: [],
  sort: 'RELEVANCE',
  interpretation: '',
});
