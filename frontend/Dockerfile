# --- Stage 1: Build ---
FROM node:20-alpine AS builder

WORKDIR /app

# Copia os manifestos de dependências do npm
COPY package.json package-lock.json ./

# Instala as dependências de forma limpa e determinística
RUN npm ci

# Copia o restante dos arquivos do projeto
COPY . .

# Base URL da API, injetada em tempo de build (o Vite resolve variáveis VITE_* ao gerar os assets)
ARG VITE_API_BASE_URL=http://localhost:8080/v1
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

# Executa o build de produção
RUN npm run build

# --- Stage 2: Serve com Nginx ---
FROM nginx:alpine AS runner

# Copia os arquivos estáticos gerados no build para a pasta do Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Configuração simples para rotas do SPA (Single Page Application)
RUN echo $'server {\n\
    listen 80;\n\
    location / {\n\
        root /usr/share/nginx/html;\n\
        index index.html index.htm;\n\
        try_files $uri $uri/ /index.html;\n\
    }\n\
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]