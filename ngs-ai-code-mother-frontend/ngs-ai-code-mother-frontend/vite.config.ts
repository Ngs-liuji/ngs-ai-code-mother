import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5174, // 修改前端访问端口为 5174，vite默认端口为5173
    proxy: {
      '/api': {
        target: 'http://localhost:8123',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
