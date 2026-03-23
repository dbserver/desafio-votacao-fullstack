import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      main: "#1565c0",
    },
    secondary: {
      main: "#c62828",
    },
    background: {
      default: "#f5f5f5",
    },
  },
  typography: {
    fontFamily: "Roboto, sans-serif",
  },
});

export default theme;
