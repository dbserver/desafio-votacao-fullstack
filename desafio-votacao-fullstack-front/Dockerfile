FROM node:20

WORKDIR /app

# Copia package.json e package-lock.json
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o restante dos arquivos da aplicação
COPY . .

# Constrói a aplicação
RUN npm run build

# Define o comando padrão para iniciar a aplicação
CMD ["npm", "run", "preview", "--", "--port", "9090", "--host", "0.0.0.0"]