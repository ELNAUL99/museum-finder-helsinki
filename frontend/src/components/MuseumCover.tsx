import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import type { MuseumSummary } from '../api/types';

/**
 * A generated cover, used whenever a museum has no licensed photograph on file.
 * The palette is derived from the slug, so each museum keeps the same colours on every
 * visit and the grid reads as a set rather than as missing images.
 */
const PALETTES: [string, string][] = [
  ['#12355b', '#3c5a80'],
  ['#1f4d46', '#3d7d6f'],
  ['#5b2333', '#8c4257'],
  ['#3a2d5c', '#66568f'],
  ['#7a4419', '#c8763c'],
  ['#1d3f52', '#417089'],
  ['#4a4a2f', '#7d7a4c'],
  ['#432c5c', '#6f4a8e'],
];

function hash(value: string): number {
  let h = 0;
  for (let i = 0; i < value.length; i += 1) {
    h = (h * 31 + value.charCodeAt(i)) >>> 0;
  }
  return h;
}

function initials(name: string): string {
  const words = name.replace(/[^\p{L}\s]/gu, ' ').split(/\s+/).filter((w) => w.length > 2);
  return words.slice(0, 2).map((w) => w[0].toUpperCase()).join('') || name.slice(0, 2).toUpperCase();
}

interface Props {
  museum: MuseumSummary;
  height?: number | string;
  compact?: boolean;
}

export default function MuseumCover({ museum, height = 170, compact = false }: Props) {
  const seed = hash(museum.slug);
  const [from, to] = PALETTES[seed % PALETTES.length];
  const angle = 120 + (seed % 5) * 15;

  return (
    <Box
      aria-hidden
      sx={{
        height,
        position: 'relative',
        overflow: 'hidden',
        background: `linear-gradient(${angle}deg, ${from}, ${to})`,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      {/* Concentric arcs: enough texture to look designed, cheap enough to be inline. */}
      <Box
        sx={{
          position: 'absolute',
          inset: 0,
          opacity: 0.16,
          background: `radial-gradient(circle at ${20 + (seed % 60)}% ${30 + (seed % 40)}%,
            rgba(255,255,255,.9) 0 8%, transparent 8.5% 18%,
            rgba(255,255,255,.7) 18.5% 26%, transparent 26.5% 40%,
            rgba(255,255,255,.5) 40.5% 52%, transparent 52.5%)`,
        }}
      />
      <Typography
        sx={{
          fontFamily: '"Fraunces", Georgia, serif',
          fontSize: compact ? 44 : 64,
          fontWeight: 600,
          color: 'rgba(255,255,255,.92)',
          letterSpacing: '0.02em',
          zIndex: 1,
        }}
      >
        {initials(museum.name)}
      </Typography>
      <Typography
        sx={{
          position: 'absolute',
          bottom: 10,
          left: 14,
          color: 'rgba(255,255,255,.75)',
          fontSize: 12,
          letterSpacing: '0.08em',
          textTransform: 'uppercase',
          zIndex: 1,
        }}
      >
        {museum.themes[0]?.label ?? museum.district}
      </Typography>
    </Box>
  );
}
