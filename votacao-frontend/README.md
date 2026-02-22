# Votação - Frontend

Frontend da aplicação de votação (single-page app) construída com Vite e React.

**Tecnologias usadas:**
- **React**: biblioteca para UI.
- **Vite**: bundler / dev server rápido.
- **Tailwind CSS**: utilitários CSS para estilos responsivos.
- **React Router**: roteamento de páginas.
- **Axios**: chamadas HTTP para a API.
- **React Toastify**: notificações toast.
- **ESLint**: linting.

**Requisitos:**
- Node.js instalado (recomenda-se Node 16+).
- npm (ou yarn) disponível.

**Instalação e execução (desenvolvimento):**

```bash
# instalar dependências
npm install

# iniciar servidor de desenvolvimento
npm run dev
```

O servidor por padrão usa o Vite; abra o endereço mostrado no terminal (normalmente http://localhost:5173).

**Build para produção:**

```bash
npm run build

# verificar build localmente
npm run preview
```

**Outros scripts úteis:**

```bash
# rodar linter
npm run lint
```

**Estrutura básica do projeto (apenas arquivos relevantes):**
- `index.html` - HTML de entrada.
- `src/main.jsx` - ponto de entrada React.
- `src/App.jsx` - rotas e composição global.
- `src/components/` - componentes reutilizáveis (ex.: `Navbar.jsx`).
- `src/pages/` - páginas principais (`Home.tsx`, `Sessao.tsx`, `Resultado.tsx`).
- `src/api/` - cliente API (`votacaoApi.ts`).
