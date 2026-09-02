import { useEffect, useState } from 'react';
import { useParams, Link as RouterLink, useNavigate } from 'react-router-dom';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Divider from '@mui/material/Divider';
import Skeleton from '@mui/material/Skeleton';
import Alert from '@mui/material/Alert';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import LanguageIcon from '@mui/icons-material/Language';
import PhoneIcon from '@mui/icons-material/Phone';
import PlaceIcon from '@mui/icons-material/Place';
import MuseumMap from '../components/LazyMuseumMap';
import MuseumCover from '../components/MuseumCover';
import { api } from '../api/client';
import type { MuseumDetail } from '../api/types';
import { useAuth } from '../auth/AuthContext';

export default function MuseumPage() {
  const { slug } = useParams<{ slug: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [detail, setDetail] = useState<MuseumDetail | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!slug) return;
    setDetail(null);
    api
      .museum(slug)
      .then(setDetail)
      .catch((e) => setError(e instanceof Error ? e.message : 'Could not load this museum'));
  }, [slug]);

  const toggleFavorite = async () => {
    if (!detail) return;
    if (!user) {
      navigate('/login');
      return;
    }
    const museum = detail.summary;
    if (museum.favorite) await api.removeFavorite(museum.id);
    else await api.addFavorite(museum.id);
    setDetail({ ...detail, summary: { ...museum, favorite: !museum.favorite } });
  };

  if (error) {
    return (
      <Container maxWidth="md" sx={{ py: 6 }}>
        <Alert severity="error">{error}</Alert>
        <Button component={RouterLink} to="/" startIcon={<ArrowBackIcon />} sx={{ mt: 2 }}>
          Back to search
        </Button>
      </Container>
    );
  }

  if (!detail) {
    return (
      <Container maxWidth="lg" sx={{ py: 6 }}>
        <Skeleton variant="rounded" height={320} />
        <Skeleton variant="text" height={60} sx={{ mt: 3, maxWidth: 460 }} />
        <Skeleton variant="text" height={24} sx={{ maxWidth: 700 }} />
      </Container>
    );
  }

  const m = detail.summary;
  const running = detail.exhibitions.filter((e) => e.running);
  const past = detail.exhibitions.filter((e) => !e.running);

  return (
    <>
      <Box sx={{ position: 'relative', height: { xs: 220, md: 340 }, display: 'flex', alignItems: 'flex-end' }}>
        <Box sx={{ position: 'absolute', inset: 0 }}>
          {m.imageUrl ? (
            <Box
              sx={{
                height: '100%',
                backgroundImage: `url(${m.imageUrlLarge ?? m.imageUrl})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
              }}
            />
          ) : (
            <MuseumCover museum={m} height="100%" />
          )}
          <Box sx={{ position: 'absolute', inset: 0, bgcolor: 'rgba(10,35,64,.45)' }} />
          {m.imageCredit && (
            <Typography
              sx={{
                position: 'absolute',
                right: 10,
                bottom: 6,
                fontSize: 11,
                color: 'rgba(255,255,255,.8)',
                '& a': { color: 'inherit', textDecoration: 'underline' },
              }}
            >
              Photo:{' '}
              {m.imageCredit.sourceUrl ? (
                <a href={m.imageCredit.sourceUrl} target="_blank" rel="noreferrer">
                  {m.imageCredit.photographer}
                </a>
              ) : (
                m.imageCredit.photographer
              )}{' '}
              ·{' '}
              {m.imageCredit.licenseUrl ? (
                <a href={m.imageCredit.licenseUrl} target="_blank" rel="noreferrer">
                  {m.imageCredit.license}
                </a>
              ) : (
                m.imageCredit.license
              )}{' '}
              via Wikimedia Commons
            </Typography>
          )}
        </Box>
        <Container maxWidth="lg" sx={{ pb: 3, position: 'relative' }}>
          <Button component={RouterLink} to="/" startIcon={<ArrowBackIcon />} sx={{ color: '#fff', mb: 1 }}>
            All museums
          </Button>
          <Typography variant="h1" sx={{ color: '#fff', fontSize: { xs: 30, md: 44 } }}>
            {m.name}
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,.9)' }}>
            {m.address}, {detail.postalCode} {m.district}
          </Typography>
        </Container>
      </Box>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box sx={{ display: 'grid', gap: 4, gridTemplateColumns: { xs: '1fr', md: '2fr 1fr' } }}>
          <Box>
            <Stack direction="row" spacing={1} sx={{ flexWrap: 'wrap', gap: 1, mb: 3 }}>
              <Chip
                label={m.freeEntry ? 'Free entry' : `€${Number(m.adultPriceEur).toFixed(0)} adult`}
                color={m.freeEntry ? 'success' : 'default'}
              />
              {m.museumCard && <Chip label="Museum Card accepted" variant="outlined" />}
              {m.wheelchairAccessible && <Chip label="Step-free access" variant="outlined" />}
              {m.familyFriendly && <Chip label="Good with children" variant="outlined" />}
              {m.hasCafe && <Chip label="Cafe" variant="outlined" />}
              {m.hasShop && <Chip label="Shop" variant="outlined" />}
              {m.themes.map((t) => (
                <Chip key={t.value} label={t.label} size="small" sx={{ bgcolor: 'background.default' }} />
              ))}
            </Stack>

            <Typography variant="body1" sx={{ fontSize: 18, lineHeight: 1.7, mb: 4 }}>
              {detail.description}
            </Typography>

            {m.freeEntryNote && (
              <Alert severity="success" variant="outlined" sx={{ mb: 4 }}>
                {m.freeEntryNote}
              </Alert>
            )}

            <Typography variant="h4" gutterBottom>
              On show now
            </Typography>
            <Stack spacing={2} sx={{ mb: 4 }}>
              {running.length === 0 && <Typography color="text.secondary">Nothing listed for today.</Typography>}
              {running.map((exhibition) => (
                <Paper key={exhibition.id} variant="outlined" sx={{ p: 2.5, display: 'flex', gap: 2 }}>
                  {exhibition.imageUrl && (
                    <Box
                      component="img"
                      src={exhibition.imageUrl}
                      alt=""
                      sx={{ width: 120, height: 90, objectFit: 'cover', borderRadius: 2, display: { xs: 'none', sm: 'block' } }}
                    />
                  )}
                  <Box>
                    <Stack direction="row" spacing={1} sx={{ alignItems: 'center', mb: 0.5 }}>
                      <Typography variant="h6">{exhibition.title}</Typography>
                      {exhibition.permanent && <Chip label="Permanent" size="small" />}
                    </Stack>
                    <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mb: 1 }}>
                      {exhibition.permanent
                        ? 'On permanent display'
                        : `${exhibition.startDate} – ${exhibition.endDate ?? 'open ended'}`}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {exhibition.description}
                    </Typography>
                  </Box>
                </Paper>
              ))}
            </Stack>

            {past.length > 0 && (
              <>
                <Typography variant="h5" gutterBottom>
                  Also in the programme
                </Typography>
                <Stack spacing={1}>
                  {past.map((exhibition) => (
                    <Typography key={exhibition.id} variant="body2" color="text.secondary">
                      {exhibition.title} · {exhibition.startDate} – {exhibition.endDate ?? '…'}
                    </Typography>
                  ))}
                </Stack>
              </>
            )}
          </Box>

          <Box>
            <Paper variant="outlined" sx={{ p: 2.5, mb: 3, position: 'sticky', top: 88 }}>
              <Button
                fullWidth
                variant={m.favorite ? 'outlined' : 'contained'}
                startIcon={m.favorite ? <FavoriteIcon /> : <FavoriteBorderIcon />}
                onClick={toggleFavorite}
                sx={{ mb: 2 }}
              >
                {m.favorite ? 'Saved' : 'Save this museum'}
              </Button>

              <Typography variant="subtitle2" gutterBottom>
                Opening hours
              </Typography>
              <Table size="small" sx={{ mb: 2 }}>
                <TableBody>
                  {detail.openingHours.map((hours) => (
                    <TableRow key={hours.day} sx={{ '& td': { border: 0, py: 0.5, px: 0 } }}>
                      <TableCell sx={{ color: 'text.secondary' }}>{hours.dayName}</TableCell>
                      <TableCell align="right">
                        {hours.closed ? 'Closed' : `${hours.opensAt?.slice(0, 5)}–${hours.closesAt?.slice(0, 5)}`}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>

              <Divider sx={{ my: 2 }} />

              <Stack spacing={1.25}>
                <Stack direction="row" spacing={1} sx={{ alignItems: 'flex-start' }}>
                  <PlaceIcon fontSize="small" color="action" />
                  <Typography variant="body2">
                    {m.address}, {detail.postalCode} {m.district}
                  </Typography>
                </Stack>
                {detail.phone && (
                  <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                    <PhoneIcon fontSize="small" color="action" />
                    <Typography variant="body2">{detail.phone}</Typography>
                  </Stack>
                )}
                {detail.website && (
                  <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                    <LanguageIcon fontSize="small" color="action" />
                    <Typography
                      variant="body2"
                      component="a"
                      href={detail.website}
                      target="_blank"
                      rel="noreferrer"
                      sx={{ color: 'primary.main' }}
                    >
                      {detail.website.replace(/^https?:\/\//, '')}
                    </Typography>
                  </Stack>
                )}
              </Stack>
            </Paper>

            <MuseumMap museums={[m]} height={280} center={[m.latitude, m.longitude]} zoom={15} />
          </Box>
        </Box>
      </Container>
    </>
  );
}
