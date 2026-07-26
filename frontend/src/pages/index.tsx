import { useState } from "react";
import {
  AppBar,
  Box,
  Container,
  Stack,
  Tab,
  Tabs,
  Toolbar,
  Typography,
} from "@mui/material";
import {
  GroupsRounded as GroupsRoundedIcon,
  HowToVoteRounded as HowToVoteRoundedIcon,
} from "@mui/icons-material";

import { Associados } from "./components/Associados";
import { Pautas } from "./components/Pautas";

type Aba = "associados" | "pautas";

export function Home() {
  const [aba, setAba] = useState<Aba>("associados");

  return (
    <>
      <AppBar
        position="sticky"
        color="inherit"
        elevation={0}
        sx={{
          borderBottom: 1,
          borderColor: "divider",
        }}
      >
        <Container maxWidth="lg">
          <Toolbar disableGutters sx={{ minHeight: 72 }}>
            <Stack
              direction="row"
              alignItems="center"
              spacing={1}
              sx={{ mr: { xs: 1, md: 4 } }}
            >
              <HowToVoteRoundedIcon color="primary" />

              <Typography
                variant="h6"
                component="span"
                fontWeight={700}
                sx={{
                  display: {
                    xs: "none",
                    sm: "block",
                  },
                }}
              >
                Sistema de Votação
              </Typography>
            </Stack>

            <Tabs
              value={aba}
              onChange={(_, novaAba: Aba) => setAba(novaAba)}
              aria-label="Navegação principal"
              sx={{ ml: "auto" }}
            >
              <Tab
                value="associados"
                icon={<GroupsRoundedIcon />}
                iconPosition="start"
                label="Associados"
              />

              <Tab
                value="pautas"
                icon={<HowToVoteRoundedIcon />}
                iconPosition="start"
                label="Pautas"
              />
            </Tabs>
          </Toolbar>
        </Container>
      </AppBar>

      <Box component="main">
        {aba === "associados" ? <Associados /> : <Pautas />}
      </Box>
    </>
  );
}
