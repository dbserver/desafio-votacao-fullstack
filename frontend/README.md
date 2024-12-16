# Documentação do Frontend

## Visão Geral do Projeto

Este projeto é uma aplicação web construída usando React e TypeScript.
Com o intuito de criar um fronted simples para a API de votação em pautas.

## Tecnologias Utilizadas

- **TypeScript**: Um superconjunto tipado de JavaScript que compila para JavaScript puro.
- **React**: Uma biblioteca JavaScript para construção de interfaces de usuário.
- **Material-UI**: Um popular framework de UI para React.
- **React Router**: Uma biblioteca para roteamento em aplicações React.
- **npm**: Um gerenciador de pacotes para JavaScript.

## Estrutura do Projeto

- `src/`: Contém o código fonte do aplicativo.
    - `axios/`: Configuração para o Axios manipular solicitações da API.
    - `ui/`: UI componentes reutilizáveis.
    - `pages/`: Páginas da aplicação.
    - `App.tsx`: Compnente aplicação principal.
    - `index.tsx`: Ponto de entrada da aplicação.

## Alguns CPFs para teste

- 086.492.750-95
- 984.928.230-46
- 582.106.150-41
- 263.220.420-84
- 333.535.550-48

Caso seja necessário mais CPFs para teste, pode-se utilizar o site 
https://www.4devs.com.br/gerador_de_cpf

Pois adicionei um serviço para validar o CPF informado.

## Como executar o frontend do projeto

### Primeiro instalar as dependências
```
npm install
```
### Depois executar o projeto
```
npm run dev
```
### Abrir o navegador e acessar o endereço:
```
http://localhost:5173/
```

## Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução

Foi utilizado o react router dom para a navegação entre as páginas,
o material-ui para a estilização dos componentes e o axios para a comunicação com a API.

O material-ui além de fácil utilização também é responsivo, o que facilita a adaptação da aplicação para diferentes 
dispositivos.

Foi usado o diretório /ui para armazenar componentes que seriam reutilizados em diferentes partes da aplicação.
Também poderia ser utilizado para armazenar o conteúdo dos index.tsx, mas decidi contra isso por questões de não ser
necessário nesse projeto. Além de não ter sido necessário criar outros diretórios além do /Util para organização do código.

Criei um diretório Pages para armazenar os diretórios de cada página por 
para facilitar a organização e compreensão do projeto.

Escolhi deixar que o ID dos diversos itens nesse projeto sejam inseridos diretamente pelo
o usuário para facilitar a utilização e teste da aplicação.

Os únicos lugares no código que se decidiu por não ser em inglês, foi 
nos readme's, o que vai ser exibido para o usuário e o valor do voto
nos botões ("radio group") para escolha de voto. Isso foi feito para 
facilitar o transporte de dado para o backend.

O atributo inputProps em Session/index.tsx está marcado como obsoleto. Apesar da pesquisa, 
nenhuma solução alternativa foi encontrada. A documentação do Material-UI sugere que
ele ainda é utilizável, pois nenhuma substituição foi fornecida.

"
You might also have noticed that some native HTML input properties are missing from the TextField component.
This is on purpose. The component takes care of the most used properties. Then, it's up to the user to use
the underlying component shown in the following demo. Still, you can use inputProps 
(and InputProps, InputLabelProps properties) if you want to avoid some boilerplate.
" (https://mui.com/material-ui/react-text-field/#components)
