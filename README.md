## Estrutura do projeto:
Utilizado Spring Boot, conforme solicitado,e como persistência foi utilizado o Banco PostgreSQL.
</br>
A aplicação foi subida numa instância EC2 da AWS, com conexão com o Banco através de RDS.
</br>
O script para criar o banco usado neste projeto se encotra no diretório ``resources/script/script_create_database.sql``.
</br>
O FrontEnd da Aplicação foi todo feito em React, utilizando estilizações simples através dos componentes da Material UI.
</br>
</br>
É possível realizar o cadastro de Associados através dessa opção, e a opção acima vai permitir que o usuário acesse inserindo apenas um CPF já cadastrado.
![img.png](img/img.png)

No Menu abaixo, ficam as opções da Aplicação. Na primeira opção, podemos realizar a verificação de quais pautas estão cadastradas, e também é possível atribuir elas a uma Sessão, para que seja votada.
![img.png](img/img2.png)
![img.png](img/img3.png)
![img.png](img/img4.png)

Também é possível fazer o cadastro de Pautas e Sessões pelo mesmo menu.

![img.png](img/img5.png)
![img.png](img/img6.png)

Na opção de iniciar Sessões, é possível ver todas as Sessões cadastradas, assim como iniciar Sessões para votação e verificar como ficou a votação de cada Pauta quando ela se encerra.
![img.png](img/img8.png)
![img.png](img/img7.png)

A instância que roda do Backend vai ficar desligada para evitar consumo excessivo, então podem entrar em contato comigo para a realização dos testes.
