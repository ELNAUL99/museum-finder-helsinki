import { useEffect, useState } from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import { Link as RouterLink } from 'react-router-dom';
import MuseumCard from '../components/MuseumCard';
import { api } from '../api/client';
import type { MuseumSummary } from '../api/types';
import { useAuth } from '../auth/AuthContext';

export default function FavoritesPage() {
  const { user, ready } = useAuth();
  const [museums, setMuseums] = useState<MuseumSummary[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!user) return;
    api
      .favorites()
      .then(setMuseums)
      .catch((e) => setError(e instanceof Error ? e.message : 'Could not load your saved museums'));
  }, [user]);

  const remove = async (museum: MuseumSummary) => {
    await api.removeFavorite(museum.id);
    setMuseums((current) => current.filter((m) => m.id !== museum.id));
  };

  if (!ready) return null;

  if (!user) {
    return (
      <Container maxWidth="sm" sx={{ py: 10, textAlign: 'center' }}>
        <Typography variant="h4" gutterBottom>
          Sign in to keep a list
        </Typography>
        <Typography color="text.secondary" sx={{ mb: 3 }}>
          Saved museums follow your account, so the list is there on the tram too.
        </Typography>
        <Button variant="contained" component={RouterLink} to="/login">
          Sign in
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 6 }}>
      <Typography variant="h2" sx={{ fontSize: 36, mb: 1 }}>
        Saved museums
      </Typography>
      <Typography color="text.secondary" sx={{ mb: 4 }}>
        {museums.length === 0 ? 'Nothing saved yet.' : `${museums.length} saved.`}
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

      {museums.length === 0 ? (
        <Button variant="contained" component={RouterLink} to="/">
          Find something to visit
        </Button>
      ) : (
        <Box sx={{ display: 'grid', gap: 3, gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr', md: '1fr 1fr 1fr' } }}>
          {museums.map((museum) => (
            <MuseumCard key={museum.id} museum={museum} onToggleFavorite={remove} />
          ))}
        </Box>
      )}
    </Container>
  );
}
