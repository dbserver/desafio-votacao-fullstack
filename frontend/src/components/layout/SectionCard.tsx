import type { ReactNode } from "react";
import { Box, Divider, Paper, Typography } from "@mui/material";

interface SectionCardProps {
  eyebrow: string;
  title: string;
  description?: string;
  children: ReactNode;
  sticky?: boolean;
}

export function SectionCard({
  eyebrow,
  title,
  description,
  children,
  sticky = false,
}: SectionCardProps) {
  return (
    <Paper
      variant="outlined"
      sx={
        sticky
          ? {
              position: { md: "sticky" },
              top: { md: 24 },
            }
          : undefined
      }
    >
      <Box sx={{ p: 3 }}>
        <Typography variant="overline" color="primary.main">
          {eyebrow}
        </Typography>

        <Typography variant="h6">{title}</Typography>

        {description && (
          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            {description}
          </Typography>
        )}
      </Box>

      <Divider />

      <Box sx={{ p: 3 }}>{children}</Box>
    </Paper>
  );
}
