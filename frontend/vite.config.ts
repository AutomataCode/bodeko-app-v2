import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import { fileURLToPath, URL } from 'node:url';

/** Redirige las peticiones /api al backend durante el desarrollo. */
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    plugins: [react()],
    resolve: { alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) } },
    server: {
      port: 3000,
      strictPort: true,
      proxy: {
        '/api': { target: env.API_PROXY_TARGET || 'http://localhost:8080', changeOrigin: true },
      },
    },
  };
});
