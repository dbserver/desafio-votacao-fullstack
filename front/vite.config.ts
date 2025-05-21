import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@api': '/src/api',
      '@assets': '/src/assets',
      '@components': '/src/components',
      '@constants': '/src/constants',
      '@context': '/src/context',
      '@customTypes': '/src/types',
      '@hooks': '/src/hooks',
      '@pages': '/src/pages',
      '@themes': '/src/themes',
      '@utils': '/src/utils',
    },
  },
});
