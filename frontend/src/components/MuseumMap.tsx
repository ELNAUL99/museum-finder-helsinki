import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { Link as RouterLink } from 'react-router-dom';
import type { MuseumSummary } from '../api/types';

/**
 * A div-based marker rather than Leaflet's default PNG: bundlers mangle the default
 * icon paths, and this keeps the map on-brand with no image assets at all.
 */
const pin = (free: boolean) =>
  L.divIcon({
    className: '',
    html: `<div style="
      width:22px;height:22px;border-radius:50% 50% 50% 0;
      transform:rotate(-45deg);
      background:${free ? '#2e7d5b' : '#12355b'};
      border:2px solid #fff;box-shadow:0 2px 6px rgba(0,0,0,.35);"></div>`,
    iconSize: [22, 22],
    iconAnchor: [11, 22],
    popupAnchor: [0, -20],
  });

interface Props {
  museums: MuseumSummary[];
  height?: number | string;
  center?: [number, number];
  zoom?: number;
}

export default function MuseumMap({ museums, height = 520, center, zoom = 12 }: Props) {
  const fallback: [number, number] = [60.1699, 24.9384];
  const mapCenter =
    center ??
    (museums.length > 0
      ? [
          museums.reduce((sum, m) => sum + m.latitude, 0) / museums.length,
          museums.reduce((sum, m) => sum + m.longitude, 0) / museums.length,
        ]
      : fallback);

  return (
    <Box sx={{ height, borderRadius: 3, overflow: 'hidden', border: 1, borderColor: 'divider' }}>
      <MapContainer center={mapCenter as [number, number]} zoom={zoom} style={{ height: '100%', width: '100%' }} scrollWheelZoom>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {museums.map((museum) => (
          <Marker key={museum.id} position={[museum.latitude, museum.longitude]} icon={pin(museum.freeEntry)}>
            <Popup>
              <Typography variant="subtitle2" component={RouterLink} to={`/museums/${museum.slug}`} sx={{ textDecoration: 'none' }}>
                {museum.name}
              </Typography>
              <Typography variant="caption" component="div" color="text.secondary">
                {museum.district} · {museum.freeEntry ? 'Free' : `€${Number(museum.adultPriceEur).toFixed(0)}`} · {museum.todayHours}
              </Typography>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </Box>
  );
}
