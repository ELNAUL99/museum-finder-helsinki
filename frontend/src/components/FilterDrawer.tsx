import Drawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import FormControlLabel from '@mui/material/FormControlLabel';
import Switch from '@mui/material/Switch';
import Slider from '@mui/material/Slider';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import type { DayFilter, Meta, SearchFilters, SortOrder } from '../api/types';
import { emptyFilters } from '../api/types';

const DAYS: DayFilter[] = [
  'ANY', 'TODAY', 'TOMORROW', 'WEEKEND',
  'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY',
];

const SORTS: { value: SortOrder; label: string }[] = [
  { value: 'RELEVANCE', label: 'Best match' },
  { value: 'DISTANCE', label: 'Closest first' },
  { value: 'PRICE_ASC', label: 'Cheapest first' },
  { value: 'NAME', label: 'A to Z' },
];

interface Props {
  open: boolean;
  onClose: () => void;
  meta: Meta | null;
  filters: SearchFilters;
  onChange: (filters: SearchFilters) => void;
}

export default function FilterDrawer({ open, onClose, meta, filters, onChange }: Props) {
  const set = <K extends keyof SearchFilters>(key: K, value: SearchFilters[K]) =>
    onChange({ ...filters, [key]: value });

  const toggleTheme = (theme: string) =>
    set('themes', filters.themes.includes(theme) ? filters.themes.filter((t) => t !== theme) : [...filters.themes, theme]);

  return (
    <Drawer anchor="right" open={open} onClose={onClose} slotProps={{ paper: { sx: { width: { xs: '100%', sm: 400 } } } }}>
      <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', gap: 2.5 }}>
        <Stack direction="row" sx={{ alignItems: 'center' }}>
          <Typography variant="h5" sx={{ flex: 1 }}>
            Filters
          </Typography>
          <IconButton onClick={onClose} aria-label="Close filters">
            <CloseIcon />
          </IconButton>
        </Stack>

        <Box>
          <Typography variant="subtitle2" gutterBottom>
            Subject
          </Typography>
          <Stack direction="row" sx={{ flexWrap: 'wrap', gap: 0.75 }}>
            {(meta?.themes ?? []).map((theme) => (
              <Chip
                key={theme.value}
                label={theme.label}
                size="small"
                color={filters.themes.includes(theme.value) ? 'primary' : 'default'}
                variant={filters.themes.includes(theme.value) ? 'filled' : 'outlined'}
                onClick={() => toggleTheme(theme.value)}
              />
            ))}
          </Stack>
        </Box>

        <Divider />

        <TextField
          select
          size="small"
          label="Near"
          value={filters.nearPlace}
          onChange={(e) => set('nearPlace', e.target.value)}
        >
          <MenuItem value="">Anywhere in Helsinki</MenuItem>
          {(meta?.places ?? []).map((place) => (
            <MenuItem key={place} value={place}>
              {place}
            </MenuItem>
          ))}
        </TextField>

        {filters.nearPlace && (
          <Box>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              Within {filters.radiusKm > 0 ? filters.radiusKm : 2} km
            </Typography>
            <Slider
              value={filters.radiusKm > 0 ? filters.radiusKm : 2}
              onChange={(_, value) => set('radiusKm', value as number)}
              min={0.5}
              max={10}
              step={0.5}
              marks={[{ value: 2, label: '2 km' }, { value: 10, label: '10 km' }]}
            />
          </Box>
        )}

        <TextField
          select
          size="small"
          label="Open on"
          value={filters.openOn}
          onChange={(e) => set('openOn', e.target.value as DayFilter)}
        >
          {DAYS.map((day) => (
            <MenuItem key={day} value={day}>
              {day === 'ANY' ? 'Any day' : day.charAt(0) + day.slice(1).toLowerCase()}
            </MenuItem>
          ))}
        </TextField>

        <Box>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            {filters.maxPriceEur >= 0 ? `Ticket up to €${filters.maxPriceEur}` : 'Any ticket price'}
          </Typography>
          <Slider
            value={filters.maxPriceEur >= 0 ? filters.maxPriceEur : 35}
            onChange={(_, value) => set('maxPriceEur', (value as number) >= 35 ? -1 : (value as number))}
            min={0}
            max={35}
            step={1}
            marks={[{ value: 0, label: '€0' }, { value: 35, label: 'Any' }]}
          />
        </Box>

        <Divider />

        <Stack>
          <FormControlLabel
            control={<Switch checked={filters.openNow} onChange={(e) => set('openNow', e.target.checked)} />}
            label="Open right now"
          />
          <FormControlLabel
            control={<Switch checked={filters.freeOnly} onChange={(e) => set('freeOnly', e.target.checked)} />}
            label="Free entry only"
          />
          <FormControlLabel
            control={<Switch checked={filters.museumCardOnly} onChange={(e) => set('museumCardOnly', e.target.checked)} />}
            label="Accepts the Museum Card"
          />
          <FormControlLabel
            control={
              <Switch
                checked={filters.wheelchairAccessible}
                onChange={(e) => set('wheelchairAccessible', e.target.checked)}
              />
            }
            label="Step-free access"
          />
          <FormControlLabel
            control={<Switch checked={filters.familyFriendly} onChange={(e) => set('familyFriendly', e.target.checked)} />}
            label="Good with children"
          />
          <FormControlLabel
            control={<Switch checked={filters.hasCafe} onChange={(e) => set('hasCafe', e.target.checked)} />}
            label="Has a cafe"
          />
        </Stack>

        <TextField
          select
          size="small"
          label="Sort by"
          value={filters.sort}
          onChange={(e) => set('sort', e.target.value as SortOrder)}
        >
          {SORTS.map((sort) => (
            <MenuItem key={sort.value} value={sort.value}>
              {sort.label}
            </MenuItem>
          ))}
        </TextField>

        <Button variant="outlined" onClick={() => onChange(emptyFilters())}>
          Clear everything
        </Button>
      </Box>
    </Drawer>
  );
}
