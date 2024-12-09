# **Votação**

Projeto elaborado para solucionar [esse desafio](https://github.com/dbserver/desafio-votacao-fullstack) para uma vaga fullstack com perfil junior.

**Tecnologias**

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JDBC](https://spring.io/projects/spring-data-jdbc)
- Angular
- Javafaker

## Como Executar

- Clonar repositório git:

<aside> 💡 git clone https://github.com/larisarapio/desafio-votacao-fullstack </aside>

- Escolha o arquivo frontend ou backend

# Frontend

- Verifique se possui node instalado com o comando `node -v`
- Se caso não possuir dê uma olhada aqui https://nodejs.org/en/learn/getting-started/how-to-install-nodejs
- Quando já tiver o node instalado
- Digite o comando `npm install` e quando finalizar `npm start`
- Por fim, acesse `http://localhost:4200/home`

---

## O que foi feito

- [x]  Interface de usuário para gerenciar e participar das votações
- [x]  Configuração de rotas e estrutura inicial do projeto.
- [x]  Planejamento de telas
- [x]  Layout responsivo

# Backend

- Verifique se possui o maven e o java instalado
    - comandos `- java -version`     `mvn -version`
    - Caso não tenha →  https://www.oracle.com/br/java/technologies/downloads/ https://maven.apache.org/download.cgi
- Execute a aplicação Spring Boot
- Acessar aplicação em `http://localhost:8080`.

---

## O que foi feito

- [x]  Criado as entidades iniciais: `Pauta`, `Sessao`, `Voto`, e `Associado`
- [x]  Configuração dos relacionamentos e persistência com JPA.
- [x]  Tentativa do desafio do CPF válido/inválido com api externa (não finalizado 100%)
- [x]  Teste o fluxo completo

---

# Escolhas

**Arquitetura** - Tentei aplicar um pouco do livro https://www.amazon.com.br/Clean-Architecture-Craftsmans-Software-Structure/dp/0134494164

**Organização -**  - Eu gosto bastante de começar aos poucos, estruturando e realizando testes simples manualmente. Esses testes, por mais básicos que pareçam, me dão a certeza de que cada etapa está funcionando. Com o tempo, esse processo contribui para melhorar a performance do código e permite que eu avance com mais confiança.

Além disso, costumo organizar meu progresso utilizando um checklist (como o que montei acima), marcando tudo o que já foi concluído. Acredito que, assim como na vida, a programação exige organização para tornar as coisas mais fáceis e eficientes.

**Dificuldades** - O tempo sem duvidas foi pro crucial, mas tentei aproveitar o máximo enquanto fazia, pois são nesses momentos que aprendemos muito.  

# Telas
- mobile

![image](https://github.com/user-attachments/assets/b558b7e7-bcd6-4abb-b8e4-de12fe45105c)

- desktop

![image](https://github.com/user-attachments/assets/37651a43-c4b2-4775-8f1f-b4fd963b4fea)


