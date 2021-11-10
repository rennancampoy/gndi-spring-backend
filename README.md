# gndi-spring-backend

Exemplo de serviço de rotas para cadastro de usuário feito em Kotlin e Spring Boot.

Pré-requisitos: Docker.

## Instruções:

1. Clonar o repositório.
2. Executar o arquivo start.sh (Linux) ou start.bat (Windows) para iniciar o serviço de backend.

## Rotas:
 ### Cadastro (POST localhost:8080/auth/signup)
  #### Enviar body no seguinte formato.
  ```
  {
    "username": "teste",
    "password": "teste123",
    "email": "teste@teste.com",
    "role": ["gndi"] // Podendo ser "gndi", "accredited" ou "professional"
 }
```
---

 ### Login (POST localhost:8080/auth/signin)
  #### Enviar body no seguinte formato. Serão retornados no response o token e o token de renovação.
  ```
  {
    "username": "teste",
    "password": "teste123"
 }
```
---
### Renovar Token de Acesso (POST localhost:8080/auth/refreshToken)
#### Enviar body no seguinte formato. Será retornado o novo token de acesso.
  ```
  {
    "refreshToken": "TokenDeRenovação"
 }
```
---
### Ver todos os usuários (GET localhost:8080/user)
#### Inserir token de acesso no header Authorization no formato Bearer Token.
---
### Teste de validação de usuário com role GNDI (GET localhost:8080/test/gndi)
#### Inserir token de acesso no header Authorization no formato Bearer Token. Caso o role no cadastro seja algum outro, a request não será autorizada.
