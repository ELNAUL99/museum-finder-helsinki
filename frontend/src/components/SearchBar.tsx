import { useState } from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import IconButton from '@mui/material/IconButton';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import CircularProgress from '@mui/material/CircularProgress';
import SearchIcon from '@mui/icons-material/Search';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';

interface Props {
  onSearch: (question: string) => void;
  loading: boolean;
  examples: string[];
  aiEnabled: boolean;
  initialValue?: string;
}

export default function SearchBar({ onSearch, loading, examples, aiEnabled, initialValue = '' }: Props) {
  const [value, setValue] = useState(initialValue);

  const submit = (event?: React.FormEvent) => {
    event?.preventDefault();
    onSearch(value.trim());
  };

  return (
    <Box>
      <Paper
        component="form"
        onSubmit={submit}
        elevation={0}
        sx={{
          display: 'flex',
          alignItems: 'center',
          px: 2,
          py: 0.5,
          borderRadius: 999,
          border: 2,
          borderColor: 'primary.main',
          bgcolor: 'background.paper',
        }}
      >
        <SearchIcon sx={{ color: 'primary.main', mr: 1.5 }} />
        <InputBase
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder={aiEnabled ? 'Ask for what you want - "free art near Kamppi, open Sunday"' : 'Search museums, districts, themes'}
          sx={{ flex: 1, py: 1.25, fontSize: 17 }}
          inputProps={{ 'aria-label': 'Search Helsinki museums' }}
        />
        <IconButton type="submit" color="primary" disabled={loading} aria-label="Search">
          {loading ? <CircularProgress size={22} /> : <SearchIcon />}
        </IconButton>
      </Paper>

      <Stack direction="row" spacing={1} sx={{ mt: 2, flexWrap: 'wrap', gap: 1 }}>
        {aiEnabled && (
          <Chip
            icon={<AutoAwesomeIcon />}
            label="AI search on"
            size="small"
            color="secondary"
            variant="outlined"
            sx={{ mr: 0.5 }}
          />
        )}
        {examples.map((example) => (
          <Chip
            key={example}
            label={example}
            size="small"
            onClick={() => {
              setValue(example);
              onSearch(example);
            }}
            sx={{ bgcolor: 'background.paper' }}
          />
        ))}
      </Stack>
      {!aiEnabled && (
        <Typography variant="caption" color="text.secondary" sx={{ mt: 1, display: 'block' }}>
          Running without an Anthropic API key - queries are parsed by the built-in keyword rules.
        </Typography>
      )}
    </Box>
  );
}
