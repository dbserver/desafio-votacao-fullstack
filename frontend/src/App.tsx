import {
  AppBar,
  Avatar,
  Box,
  Container,
  Stack,
  Toolbar,
  Typography,
} from "@mui/material";
import { HowToVoteRounded as HowToVoteRoundedIcon } from "@mui/icons-material";

import { Pautas } from "./pages/Pautas";

function App() {
  return (
    <Box sx={{ minHeight: "100vh" }}>
      <AppBar
        position="static"
        color="inherit"
        elevation={0}
        sx={{ borderBottom: 1, borderColor: "divider" }}
      >
        <Toolbar disableGutters>
          <Container maxWidth="lg">
            <Stack
              direction="row"
              alignItems="center"
              spacing={1.5}
              sx={{ minHeight: 72 }}
            >
              <Avatar
                variant="rounded"
                sx={{
                  width: 44,
                  height: 44,
                  bgcolor: "primary.main",
                }}
              >
                <HowToVoteRoundedIcon />
              </Avatar>

              <Box>
                <Typography fontWeight={700}>Sistema de Votação</Typography>

                <Typography variant="caption" color="text.secondary">
                  Gestão cooperativa
                </Typography>
              </Box>
            </Stack>
          </Container>
        </Toolbar>
      </AppBar>

      <Pautas />
    </Box>
  );
}

export default App;
