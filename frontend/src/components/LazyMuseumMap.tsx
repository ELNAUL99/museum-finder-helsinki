import { Suspense, lazy } from 'react';
import Skeleton from '@mui/material/Skeleton';
import type { ComponentProps } from 'react';
import type MuseumMap from './MuseumMap';

/** Leaflet is a third of the bundle, so it only loads when a map is actually shown. */
const Map = lazy(() => import('./MuseumMap'));

export default function LazyMuseumMap(props: ComponentProps<typeof MuseumMap>) {
  return (
    <Suspense fallback={<Skeleton variant="rounded" height={props.height ?? 520} />}>
      <Map {...props} />
    </Suspense>
  );
}
