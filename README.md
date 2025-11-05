# Desafio Votação Fullstack

Este repositório contém uma aplicação fullstack para votação de pautas. O projeto é dividido em:

- **Backend**: API desenvolvida em Java com Spring Boot e Gradle.
- **Frontend**: Aplicação web desenvolvida com Next.js.

---

## 🚀 Como rodar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/AdrianaLedo/desafio-votacao-fullstack.git
cd desafio-votacao-fullstack
```

---

## 🔧 Backend (Java + Spring Boot)

### Pré-requisitos

- Java 17+
- Gradle (ou utilize o wrapper `./gradlew`)

### Instalação e execução

```bash
cd votacao_backend
./gradlew bootRun
```

A API será executada em `http://localhost:8080`.

### 📄 Documentação da API (Swagger)

Após o backend estar rodando, acesse a documentação da API:

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## 💻 Frontend (Next.js)

### Pré-requisitos

- Preferência Node.js 20+
- npm ou yarn

### Instalação e execução

```bash
cd votacao_frontend
npm install  # ou yarn
npm run dev  # ou yarn dev
```

O frontend estará disponível em: [http://localhost:3000](http://localhost:3000)

---

## ✅ Funcionalidades principais

- Criação de pautas e sessões de votação
- Votação com CPF
- Listagem de sessões abertas
- Resultado de votações


---

## 🛠 Tecnologias utilizadas

### Backend
- Java 17
- Spring Boot
- Gradle
- Swagger

### Frontend
- React / Next.js
- Tailwind CSS
- React Query
- React Hook Form