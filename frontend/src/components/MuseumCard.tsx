import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import PlaceIcon from '@mui/icons-material/Place';
import ScheduleIcon from '@mui/icons-material/Schedule';
import { Link as RouterLink } from 'react-router-dom';
import MuseumCover from './MuseumCover';
import type { MuseumSummary } from '../api/types';

interface Props {
  museum: MuseumSummary;
  onToggleFavorite?: (museum: MuseumSummary) => void;
}

export default function MuseumCard({ museum, onToggleFavorite }: Props) {
  const price = museum.freeEntry ? 'Free' : `€${Number(museum.adultPriceEur).toFixed(0)}`;

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', position: 'relative' }}>
      {onToggleFavorite && (
        <Tooltip title={museum.favorite ? 'Remove from saved' : 'Save this museum'}>
          <IconButton
            onClick={() => onToggleFavorite(museum)}
            aria-label={museum.favorite ? 'Remove from saved' : 'Save this museum'}
            sx={{
              position: 'absolute',
              top: 8,
              right: 8,
              zIndex: 2,
              bgcolor: 'rgba(255,255,255,0.92)',
              '&:hover': { bgcolor: '#fff' },
            }}
          >
            {museum.favorite ? <FavoriteIcon color="secondary" fontSize="small" /> : <FavoriteBorderIcon fontSize="small" />}
          </IconButton>
        </Tooltip>
      )}

      <CardActionArea
        component={RouterLink}
        to={`/museums/${museum.slug}`}
        sx={{ height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'stretch' }}
      >
        {museum.imageUrl ? (
          <Box sx={{ position: 'relative' }}>
            <CardMedia
              component="img"
              height="170"
              image={museum.imageUrl}
              alt=""
              loading="lazy"
              sx={{ objectFit: 'cover', bgcolor: 'grey.200' }}
            />
            {museum.imageCredit && (
              // CC BY-SA wants the photographer named wherever the photo appears,
              // so the credit rides on the card too, quietly.
              <Typography
                sx={{
                  position: 'absolute',
                  right: 6,
                  bottom: 4,
                  px: 0.75,
                  fontSize: 10,
                  lineHeight: 1.6,
                  color: 'rgba(255,255,255,.92)',
                  bgcolor: 'rgba(0,0,0,.42)',
                  borderRadius: 1,
                  maxWidth: '85%',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap',
                }}
              >
                {museum.imageCredit.photographer} · {museum.imageCredit.license}
              </Typography>
            )}
          </Box>
        ) : (
          <MuseumCover museum={museum} />
        )}
        <CardContent sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 1 }}>
          <Stack direction="row" spacing={1} sx={{ alignItems: 'center', flexWrap: 'wrap', gap: 0.5 }}>
            <Chip
              label={price}
              size="small"
              color={museum.freeEntry ? 'success' : 'default'}
              variant={museum.freeEntry ? 'filled' : 'outlined'}
            />
            {museum.museumCard && <Chip label="Museum Card" size="small" variant="outlined" />}
            {museum.distanceKm != null && (
              <Chip label={`${museum.distanceKm.toFixed(1)} km`} size="small" variant="outlined" />
            )}
          </Stack>

          <Typography variant="h6" sx={{ lineHeight: 1.25 }}>
            {museum.name}
          </Typography>

          <Typography variant="body2" color="text.secondary" sx={{ flex: 1 }}>
            {museum.shortDescription}
          </Typography>

          <Stack direction="row" spacing={2} sx={{ color: 'text.secondary', fontSize: 13, flexWrap: 'wrap' }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
              <PlaceIcon sx={{ fontSize: 16 }} /> {museum.district}
            </Box>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: museum.openToday ? 'success.main' : 'text.secondary' }}>
              <ScheduleIcon sx={{ fontSize: 16 }} /> {museum.todayHours}
            </Box>
          </Stack>

          <Stack direction="row" spacing={0.5} sx={{ flexWrap: 'wrap', gap: 0.5, mt: 0.5 }}>
            {museum.themes.slice(0, 3).map((theme) => (
              <Chip key={theme.value} label={theme.label} size="small" sx={{ bgcolor: 'background.default' }} />
            ))}
          </Stack>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
