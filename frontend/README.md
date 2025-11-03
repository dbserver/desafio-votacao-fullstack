# Desafio Front-End: Aplicação de Votação em React

## Descrição do Projeto
Este projeto é um front-end em **React** que consome uma API de votação para gerenciar pautas, sessões e votos.  
A aplicação permite criar pautas, abrir sessões, registrar votos e consultar resultados de forma simples e responsiva.

O projeto utiliza **Vite**, **React Router**, **Axios** e **React Toastify** para exibir notificações de sucesso ou erro.

---

## Funcionalidades
- **Criar Pauta**: Permite cadastrar uma nova pauta com título e descrição.  
- **Abrir Sessão**: Inicia uma sessão de votação para uma pauta existente, definindo início e fim.  
- **Registrar Voto**: Permite votar “SIM” ou “NÃO” em uma sessão específica.  
- **Consultar Resultado**: Exibe o total de votos “SIM” e “NÃO” de uma sessão.

---

## Tecnologias Utilizadas
- React 19
- Vite
- Axios
- React Router DOM
- React Toastify
- CSS3 
- Jest e Testing Library para testes

---

## Estrutura do Projeto
votacao-frontend/
├── public/ # Arquivos públicos
├── src/
│ ├── api/ # Configuração do Axios
│ │ └── api.jsx
│ ├── components/ # Componentes reutilizáveis
│ │ └── ResultadoCard.jsx
│ ├── pages/ # Páginas da aplicação
│ │ ├── FormPauta.jsx
│ │ ├── FormSessao.jsx
│ │ ├── FormVoto.jsx
│ │ └── ResultadoPage.jsx
│ ├── utils/ # Funções auxiliares (ex: toast.js)
│ ├── App.jsx
│ └── main.jsx
├── package.json
├── vite.config.js
└── README.md

yaml
Copy code

---

## Instalação e Execução
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/votacao-frontend.git
Entre na pasta do projeto:

bash
Copy code
cd votacao-frontend
Instale as dependências:

bash
Copy code
npm install

Inicie o servidor de desenvolvimento:

bash
Copy code
npm run dev
Abra a aplicação no navegador:

arduino
Copy code
http://localhost:5173
Uso
Acesse a página principal e utilize os formulários para criar pautas, abrir sessões e registrar votos.

Consulte os resultados em tempo real usando a página de resultados.

Boas Práticas
Componentes reutilizáveis para maior manutenção.

Notificações de sucesso e erro usando React Toastify.

Proxy configurado para facilitar integração com o back-end.

Suporte a responsividade e layout limpo.

Testes
Testes podem ser executados com:

bash
Copy code
npm run test