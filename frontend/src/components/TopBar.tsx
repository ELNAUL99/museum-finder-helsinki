import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import FavoriteIcon from '@mui/icons-material/FavoriteBorder';
import MuseumIcon from '@mui/icons-material/AccountBalance';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export default function TopBar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <AppBar position="sticky" color="inherit" sx={{ borderBottom: 1, borderColor: 'divider', bgcolor: 'background.paper' }}>
      <Toolbar sx={{ maxWidth: 1240, width: '100%', mx: 'auto', gap: 2 }}>
        <Box
          component={RouterLink}
          to="/"
          sx={{ display: 'flex', alignItems: 'center', gap: 1, textDecoration: 'none', color: 'primary.main' }}
        >
          <MuseumIcon />
          <Typography variant="h6" sx={{ fontFamily: '"Fraunces", serif', letterSpacing: '-0.01em' }}>
            Museum Finder
            <Box component="span" sx={{ color: 'secondary.main' }}> Helsinki</Box>
          </Typography>
        </Box>
        <Box sx={{ flex: 1 }} />
        <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
          <Button component={RouterLink} to="/favorites" startIcon={<FavoriteIcon />} color="inherit">
            Saved
          </Button>
          {user ? (
            <>
              <Typography variant="body2" color="text.secondary" sx={{ display: { xs: 'none', sm: 'block' } }}>
                {user.displayName}
              </Typography>
              <Button onClick={logout} color="inherit">
                Sign out
              </Button>
            </>
          ) : (
            <Button variant="contained" onClick={() => navigate('/login')}>
              Sign in
            </Button>
          )}
        </Stack>
      </Toolbar>
    </AppBar>
  );
}
