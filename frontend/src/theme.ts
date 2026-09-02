import { createTheme } from '@mui/material/styles';

/**
 * Helsinki palette: harbour blue, granite grey, and the warm ochre of the
 * Ateneum facade for anything that needs to be noticed.
 */
export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#12355b', light: '#3c5a80', dark: '#0a2340', contrastText: '#ffffff' },
    secondary: { main: '#c8763c', light: '#dd9a6a', dark: '#9c5729', contrastText: '#ffffff' },
    success: { main: '#2e7d5b' },
    background: { default: '#f6f4f0', paper: '#ffffff' },
    text: { primary: '#17202a', secondary: '#5a6472' },
    divider: 'rgba(18, 53, 91, 0.12)',
  },
  shape: { borderRadius: 12 },
  typography: {
    fontFamily: '"Inter", system-ui, -apple-system, "Segoe UI", sans-serif',
    h1: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 600, letterSpacing: '-0.02em' },
    h2: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 600, letterSpacing: '-0.015em' },
    h3: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 600 },
    h4: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 600 },
    h5: { fontFamily: '"Fraunces", Georgia, serif', fontWeight: 600 },
    h6: { fontWeight: 600 },
    button: { textTransform: 'none', fontWeight: 600 },
  },
  components: {
    MuiCard: {
      styleOverrides: {
        root: {
          border: '1px solid rgba(18, 53, 91, 0.10)',
          boxShadow: '0 1px 2px rgba(18, 53, 91, 0.04)',
          transition: 'box-shadow 160ms ease, transform 160ms ease',
          '&:hover': {
            boxShadow: '0 12px 28px rgba(18, 53, 91, 0.12)',
            transform: 'translateY(-2px)',
          },
        },
      },
    },
    MuiChip: { styleOverrides: { root: { fontWeight: 500 } } },
    MuiAppBar: { defaultProps: { elevation: 0 } },
  },
});
