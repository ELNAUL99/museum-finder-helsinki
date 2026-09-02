import { useCallback, useEffect, useMemo, useState } from 'react';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Alert from '@mui/material/Alert';
import Skeleton from '@mui/material/Skeleton';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import TuneIcon from '@mui/icons-material/Tune';
import GridViewIcon from '@mui/icons-material/GridView';
import MapIcon from '@mui/icons-material/Map';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import FilterChips from '../components/FilterChips';
import FilterDrawer from '../components/FilterDrawer';
import MuseumCard from '../components/MuseumCard';
import MuseumMap from '../components/LazyMuseumMap';
import { api } from '../api/client';
import { emptyFilters } from '../api/types';
import type { Meta, MuseumSummary, SearchFilters, SearchResponse } from '../api/types';
import { useAuth } from '../auth/AuthContext';

export default function SearchPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [meta, setMeta] = useState<Meta | null>(null);
  const [response, setResponse] = useState<SearchResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [view, setView] = useState<'grid' | 'map'>('grid');

  const themeLabels = useMemo(
    () => Object.fromEntries((meta?.themes ?? []).map((t) => [t.value, t.label])),
    [meta],
  );

  useEffect(() => {
    api.meta().then(setMeta).catch(() => setMeta(null));
  }, []);

  const runFilters = useCallback(async (filters: SearchFilters, question = '') => {
    setLoading(true);
    setError(null);
    try {
      setResponse(await api.searchByFilters(filters, question));
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Search failed');
    } finally {
      setLoading(false);
    }
  }, []);

  const runQuestion = useCallback(async (question: string) => {
    setLoading(true);
    setError(null);
    try {
      setResponse(question ? await api.searchByQuestion(question) : await api.searchByFilters(emptyFilters()));
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Search failed');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    runFilters(emptyFilters());
  }, [runFilters, user]);

  const toggleFavorite = async (museum: MuseumSummary) => {
    if (!user) {
      navigate('/login');
      return;
    }
    try {
      if (museum.favorite) await api.removeFavorite(museum.id);
      else await api.addFavorite(museum.id);
      setResponse((current) =>
        current
          ? {
              ...current,
              results: current.results.map((m) => (m.id === museum.id ? { ...m, favorite: !m.favorite } : m)),
            }
          : current,
      );
    } catch {
      setError('Could not update your saved museums.');
    }
  };

  const filters = response?.filters ?? emptyFilters();
  const results = response?.results ?? [];

  return (
    <Container maxWidth="lg" sx={{ py: { xs: 4, md: 6 } }}>
      <Box sx={{ maxWidth: 760, mb: 4 }}>
        <Typography variant="h1" sx={{ fontSize: { xs: 34, md: 48 }, mb: 1.5 }}>
          Every museum in Helsinki,
          <Box component="span" sx={{ color: 'secondary.main' }}> asked for in your own words</Box>
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 3, fontSize: 18 }}>
          Thirty-four museums, from the Ateneum to a free tram depot. Describe the afternoon you want and the
          filters get worked out for you - then correct anything that came out wrong.
        </Typography>
        <SearchBar
          onSearch={runQuestion}
          loading={loading}
          examples={meta?.examples ?? []}
          aiEnabled={meta?.aiSearchEnabled ?? false}
          provider={meta?.aiProvider}
        />
      </Box>

      {response?.filters.interpretation && response.interpretedBy !== 'filters' && (
        <Alert
          icon={<AutoAwesomeIcon fontSize="inherit" />}
          severity="info"
          variant="outlined"
          sx={{ mb: 2, bgcolor: 'background.paper' }}
        >
          {response.filters.interpretation}
          <Typography variant="caption" component="div" color="text.secondary">
            Read by{' '}
            {response.interpretedBy === 'claude'
              ? 'Claude'
              : response.interpretedBy === 'mistral'
                ? 'Mistral'
                : 'the built-in keyword rules'}{' '}
            · remove a chip below to change it
          </Typography>
        </Alert>
      )}

      {response?.note && (
        <Alert severity="warning" variant="outlined" sx={{ mb: 2, bgcolor: 'background.paper' }}>
          {response.note}
        </Alert>
      )}

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Stack
        direction={{ xs: 'column', sm: 'row' }}
        spacing={2}
        sx={{ alignItems: { sm: 'center' }, justifyContent: 'space-between', mb: 2 }}
      >
        <Box sx={{ flex: 1 }}>
          <FilterChips filters={filters} themeLabels={themeLabels} onChange={(next) => runFilters(next)} />
        </Box>
        <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
          <Button startIcon={<TuneIcon />} variant="outlined" onClick={() => setDrawerOpen(true)}>
            Filters
          </Button>
          <ToggleButtonGroup
            size="small"
            exclusive
            value={view}
            onChange={(_, next) => next && setView(next)}
            aria-label="Result view"
          >
            <ToggleButton value="grid" aria-label="Grid view">
              <GridViewIcon fontSize="small" />
            </ToggleButton>
            <ToggleButton value="map" aria-label="Map view">
              <MapIcon fontSize="small" />
            </ToggleButton>
          </ToggleButtonGroup>
        </Stack>
      </Stack>

      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
        {loading ? 'Searching…' : `${results.length} ${results.length === 1 ? 'museum' : 'museums'}`}
      </Typography>

      {loading ? (
        <Box sx={{ display: 'grid', gap: 3, gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr', md: '1fr 1fr 1fr' } }}>
          {[0, 1, 2, 3, 4, 5].map((i) => (
            <Skeleton key={i} variant="rounded" height={360} />
          ))}
        </Box>
      ) : results.length === 0 ? (
        <Box sx={{ py: 8, textAlign: 'center' }}>
          <Typography variant="h5" gutterBottom>
            Nothing matches all of that
          </Typography>
          <Typography color="text.secondary" sx={{ mb: 2 }}>
            Try removing a chip - "open now" and "free" together rule out most of the city after five o'clock.
          </Typography>
          <Button variant="contained" onClick={() => runFilters(emptyFilters())}>
            Show every museum
          </Button>
        </Box>
      ) : view === 'map' ? (
        <MuseumMap museums={results} />
      ) : (
        <Box sx={{ display: 'grid', gap: 3, gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr', md: '1fr 1fr 1fr' } }}>
          {results.map((museum) => (
            <MuseumCard key={museum.id} museum={museum} onToggleFavorite={toggleFavorite} />
          ))}
        </Box>
      )}

      <FilterDrawer
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        meta={meta}
        filters={filters}
        onChange={(next) => runFilters(next)}
      />
    </Container>
  );
}
