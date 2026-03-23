import { ReactNode } from "react";
import { AppBar, Toolbar, Typography, Container, Box } from "@mui/material";
import { HowToVote } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";

interface LayoutProps {
  children: ReactNode;
}

function Layout({ children }: LayoutProps) {
  const navigate = useNavigate();

  return (
    <Box sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <AppBar position="static" elevation={1}>
        <Toolbar>
          <HowToVote sx={{ mr: 2 }} />
          <Typography
            variant="h6"
            sx={{ cursor: "pointer", fontWeight: 500 }}
            onClick={() => navigate("/")}
          >
            Sistema de Votação
          </Typography>
        </Toolbar>
      </AppBar>
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4, flex: 1 }}>
        {children}
      </Container>
      <Box
        component="footer"
        sx={{ py: 2, textAlign: "center", bgcolor: "grey.100" }}
      >
        <Typography variant="body2" color="text.secondary">
          Votação Cooperativa
        </Typography>
      </Box>
    </Box>
  );
}

export default Layout;
