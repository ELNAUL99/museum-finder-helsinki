import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import type { SearchFilters } from '../api/types';

interface Chipish {
  key: string;
  label: string;
  clear: (filters: SearchFilters) => SearchFilters;
}

/** Turns the active filters into removable chips, so an AI guess can always be corrected. */
export function activeChips(filters: SearchFilters, themeLabels: Record<string, string>): Chipish[] {
  const chips: Chipish[] = [];

  filters.themes.forEach((theme) => {
    chips.push({
      key: `theme-${theme}`,
      label: themeLabels[theme] ?? theme,
      clear: (f) => ({ ...f, themes: f.themes.filter((t) => t !== theme) }),
    });
  });

  if (filters.freeOnly) chips.push({ key: 'free', label: 'Free entry', clear: (f) => ({ ...f, freeOnly: false }) });
  if (filters.museumCardOnly)
    chips.push({ key: 'card', label: 'Museum Card', clear: (f) => ({ ...f, museumCardOnly: false }) });
  if (filters.maxPriceEur >= 0)
    chips.push({ key: 'price', label: `Under €${filters.maxPriceEur}`, clear: (f) => ({ ...f, maxPriceEur: -1 }) });
  if (filters.openNow) chips.push({ key: 'now', label: 'Open now', clear: (f) => ({ ...f, openNow: false }) });
  if (filters.openOn !== 'ANY' && !filters.openNow)
    chips.push({
      key: 'day',
      label: `Open ${filters.openOn.toLowerCase()}`,
      clear: (f) => ({ ...f, openOn: 'ANY' }),
    });
  if (filters.nearPlace)
    chips.push({ key: 'place', label: `Near ${filters.nearPlace}`, clear: (f) => ({ ...f, nearPlace: '' }) });
  if (filters.wheelchairAccessible)
    chips.push({ key: 'access', label: 'Step-free', clear: (f) => ({ ...f, wheelchairAccessible: false }) });
  if (filters.familyFriendly)
    chips.push({ key: 'family', label: 'Good with kids', clear: (f) => ({ ...f, familyFriendly: false }) });
  if (filters.hasCafe) chips.push({ key: 'cafe', label: 'Has a cafe', clear: (f) => ({ ...f, hasCafe: false }) });

  filters.keywords.forEach((keyword) => {
    chips.push({
      key: `kw-${keyword}`,
      label: `"${keyword}"`,
      clear: (f) => ({ ...f, keywords: f.keywords.filter((k) => k !== keyword) }),
    });
  });

  return chips;
}

interface Props {
  filters: SearchFilters;
  themeLabels: Record<string, string>;
  onChange: (filters: SearchFilters) => void;
}

export default function FilterChips({ filters, themeLabels, onChange }: Props) {
  const chips = activeChips(filters, themeLabels);
  if (chips.length === 0) return null;

  return (
    <Stack direction="row" spacing={1} sx={{ flexWrap: 'wrap', gap: 1 }}>
      {chips.map((chip) => (
        <Chip
          key={chip.key}
          label={chip.label}
          onDelete={() => onChange(chip.clear(filters))}
          color="primary"
          variant="outlined"
          sx={{ bgcolor: 'background.paper' }}
        />
      ))}
    </Stack>
  );
}
