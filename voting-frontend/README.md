# 🗳️ Voting Frontend

Sistema de votação moderno e responsivo desenvolvido com React e TypeScript. Permite que usuários registrem votos em pautas compartilhadas com interface intuitiva e segura.

## 📋 Descrição

O **Voting Frontend** é a interface web para um sistema de votação distribuído. Permite:

- 📝 Visualizar pautas de votação em tempo real
- 🗳️ Registrar votos anônimos e seguros
- ⏱️ Acompanhar o tempo restante da sessão de votação
- 📊 Compartilhar links de votação via clipboard
- 🔒 Validações de segurança (CPF único por voto)
- 📱 Interface responsiva para desktop e mobile

## 🛠️ Tecnologias Utilizadas

- **React** (19.2.4) - Framework UI
- **TypeScript** (5.9.3) - Tipagem estática
- **Vite** (8.0.1) - Bundler e dev server
- **React Router DOM** (7.13.1) - Roteamento
- **Tailwind CSS** (4.2.2) - Estilização
- **Axios** (1.13.6) - Cliente HTTP
- **ESLint** (9.39.4) - Linting e qualidade de código

## 📋 Pré-requisitos

Antes de começar, certifique-se de que você tem instalado:

- **Node.js** (versão 16.0.0 ou superior)
- **npm** (versão 7.0.0 ou superior)

## 🚀 Instalação

1. **Clone o repositório ou extraia os arquivos:**

```bash
cd voting-frontend
```

2. **Instale as dependências:**

```bash
npm install
```

## 💻 Executando Localmente

### Modo Desenvolvimento

Para iniciar o servidor de desenvolvimento com Hot Module Replacement (HMR):

```bash
npm run dev
```

O aplicativo estará disponível em:
- **http://localhost:5173** (padrão Vite)
- Ou a URL exibida no terminal

### Build para Produção

Para compilar a aplicação para produção:

```bash
npm run build
```

Os arquivos otimizados serão gerados na pasta `dist/`.

### Preview de Produção

Para visualizar o build de produção localmente:

```bash
npm run preview
```

### Linting

Para verificar a qualidade do código:

```bash
npm run lint
```

## 📁 Estrutura do Projeto

```
src/
├── components/      # Componentes reutilizáveis
├── layouts/         # Layouts da aplicação
├── pages/           # Páginas principais
├── routes/          # Configuração de rotas
├── services/        # Serviços (API, lógica)
├── types/           # Tipos TypeScript
├── styles/          # Estilos globais
└── App.tsx          # Componente raiz
```

## 🔗 Endpoints da API

A aplicação se conecta com os seguintes serviços:

- **Obter Pauta**: `GET /api/agendas/:id`
- **Registrar Voto**: `POST /api/votes`
- **Obter Resultado**: `GET /api/votes/result/:agendaId`

> Configure a URL base da API no arquivo `src/services/api.ts`

## ⚙️ Configuração

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto (opcional):

```env
VITE_API_URL=http://localhost:8080
```

> Se não configurado, a aplicação usa a URL padrão definida em `src/services/api.ts`

## 🧪 Funcionalidades Principais

- ✅ Listagem de pautas
- ✅ Detalhes da pauta com timer em tempo real
- ✅ Registro de voto com validação de CPF
- ✅ Modal de confirmação de voto
- ✅ Compartilhamento de link de votação
- ✅ Exibição de resultado de votos
- ✅ Sessão de votação com tempo limite

