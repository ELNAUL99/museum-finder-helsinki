import { Route, Routes } from 'react-router-dom';
import Box from '@mui/material/Box';
import TopBar from './components/TopBar';
import SearchPage from './pages/SearchPage';
import MuseumPage from './pages/MuseumPage';
import FavoritesPage from './pages/FavoritesPage';
import LoginPage from './pages/LoginPage';

export default function App() {
  return (
    <Box sx={{ minHeight: '100dvh', display: 'flex', flexDirection: 'column', bgcolor: 'background.default' }}>
      <TopBar />
      <Box component="main" sx={{ flex: 1 }}>
        <Routes>
          <Route path="/" element={<SearchPage />} />
          <Route path="/museums/:slug" element={<MuseumPage />} />
          <Route path="/favorites" element={<FavoritesPage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
      </Box>
      <Box
        component="footer"
        sx={{ py: 3, px: 2, textAlign: 'center', color: 'text.secondary', fontSize: 14, borderTop: 1, borderColor: 'divider' }}
      >
        Museum Finder Helsinki · opening hours and prices are indicative - check the museum's own site before you travel
      </Box>
    </Box>
  );
}
