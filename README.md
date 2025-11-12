# Projeto Extra: Sistema de Apostas Esportivas Online

Este projeto é um sistema de apostas esportivas online desenvolvido como parte de uma avaliação acadêmica. A aplicação foi construída utilizando Spring Boot e segue uma arquitetura de API RESTful, simulando o funcionamento básico de uma plataforma de apostas.

O sistema permite que usuários se cadastrem com perfis de `ADMIN` ou `APOSTADOR`, consultem eventos esportivos, realizem apostas, acompanhem resultados (simulados) e gerenciem uma carteira virtual.

## Tecnologias Utilizadas

O projeto foi construído com as seguintes tecnologias:
* **Java 17**
* **Spring Boot 3.2.5**
* **Spring Data JPA:** Para persistência de dados.
* **Spring Security:** Para autenticação e autorização.
* **JWT (JSON Web Tokens):** Para gerenciamento de sessões de usuário de forma segura.
* **H2 Database:** Banco de dados em memória para um ambiente de desenvolvimento simplificado.
* **Maven:** Para gerenciamento de dependências e do build do projeto.
* **Lombok:** Para reduzir código boilerplate em entidades e DTOs.
* **Springdoc (Swagger/OpenAPI):** Para documentação e teste interativo da API.

## Funcionalidades Implementadas

O sistema conta com os seguintes módulos e funcionalidades:

### 1. Usuário e Autenticação
* **Cadastro:** Usuários podem se registrar. Por padrão, todo novo registro cria um usuário com perfil `APOSTADOR` e uma carteira com saldo inicial de R$ 0,00.
* **Login:** A autenticação é feita via e-mail e senha, retornando um token JWT para ser usado em requisições subsequentes.
* **Perfis de Acesso:**
    * `APOSTADOR`: Pode gerenciar sua carteira, fazer e consultar suas apostas.
    * `ADMIN`: Possui acesso total ao sistema, incluindo o gerenciamento de esportes, times, eventos e relatórios.
* **Consulta de Perfil:** O usuário autenticado pode consultar seus próprios dados.

### 2. Módulos de Gerenciamento (Acesso Exclusivo ADMIN)
* **Esportes:** CRUD completo para gerenciar os esportes disponíveis na plataforma (ex: Futebol, Basquete). os times, sempre vinculados a um esporte.
* **Eventos Esportivos:** CRUD para criar e gerenciar eventos, definindo os times participantes, data, local e as *odds* (cotações) para cada resultado possível (vitória do time A, empate, vitória do time B). 

### 3. Módulos de Interação do Usuário
* **Listagem Pública de Eventos:** Qualquer pessoa (mesmo sem autenticação) pode visualizar os próximos eventos esportivos.
* **Apostas:** Usuários do tipo `APOSTADOR` podem:
    * Realizar apostas em eventos futuros, debitando o valor de sua carteira.
    * Visualizar o histórico de todas as suas apostas.
* **Simulação de Resultados:** Um `ADMIN` pode disparar a simulação do resultado de um evento. O sistema define um vencedor aleatoriamente e calcula automaticamente os ganhos e perdas de todas as apostas associadas.
* **Carteira Virtual:** O usuário pode:
    * Consultar seu saldo atual.
    * Adicionar saldo (simulado) à sua carteira.

### 4. Relatórios (Acesso Exclusivo ADMIN)
* **Volume de Apostas:** Endpoint que retorna o valor total apostado em cada evento.
* **Lucro/Prejuízo:** Endpoint que calcula o resultado financeiro da plataforma (total apostado - total pago em prêmios).

## Funcionalidades Extras Implementadas

Para atender ao requisito 8 do trabalho, duas novas funcionalidades foram adicionadas:

#### 1. Módulo de Promoções
* **O que é?** Permite que um `ADMIN` crie e gerencie promoções (ex: "Bônus de Boas-Vindas de R$ 10,00").
* **Como funciona?**
    * `ADMINs` podem criar ou deletar promoções através dos endpoints `POST /promocoes` e `DELETE /promocoes/{id}`.
    * Qualquer pessoa pode visualizar as promoções ativas em `GET /promocoes`.

#### 2. Histórico de Transações
* **O que é?** Um extrato financeiro completo para cada usuário, registrando todas as movimentações em sua carteira.
* **Como funciona?**
    * **Automático:** O sistema cria um registro de transação automaticamente sempre que:
        * Um `DEPOSITO` é feito (`POST /carteira/adicionar`).
        * Uma `APOSTA` é realizada (`POST /apostas`). 
        * Um `PREMIO` é recebido após uma aposta vencedora.
    * **Consulta:** O usuário autenticado pode consultar seu extrato detalhado e ordenado por data em `GET /transacoes/meu-extrato`.

---

## Como Executar e Testar o Projeto

### Pré-requisitos
* Java 17 (ou superior)
* Apache Maven

### 1. Executando a Aplicação
1. Clone o repositório.
2. Abra um terminal na pasta raiz do projeto.
3. Execute o seguinte comando Maven para iniciar a aplicação:
    ```bash
    mvn spring-boot:run
    ```
4. A API estará rodando em `http://localhost:8080`.

### 2. Testando com o Swagger UI

A maneira mais fácil de testar a API é através da interface do Swagger.

1. Com a aplicação rodando, acesse a seguinte URL no seu navegador:
    [**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)
2. Você verá todos os endpoints da API agrupados por `Controller`.

### Guia Passo a Passo para Testes no Swagger

Siga este fluxo para uma experiência de teste completa.

#### **Como Criar um Usuário ADMIN**
Por padrão, o sistema cadastra todos os novos usuários como `APOSTADOR`. Para criar um `ADMIN`, o jeito mais fácil é alterar o perfil de um usuário diretamente no banco de dados H2.

1.  **Registre um usuário que será o Admin:**
    * No Swagger, vá em `auth-controller > POST /auth/register`.
    * Clique em "Try it out".
    * No corpo da requisição, insira os dados do futuro admin:
      ```json
      {
        "nome": "Admin User",
        "email": "admin@email.com",
        "senha": "password123"
      }
      ```
    * Clique em "Execute".

2.  **Acesse o Console do Banco de Dados H2:**
    * Abra uma nova aba no navegador e acesse: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    * As informações de login estão no seu arquivo `application.properties`. Preencha os campos da seguinte forma:
        * **JDBC URL:** `jdbc:h2:mem:betdb` 
        * **User Name:** `sa` 
        * **Password:** (deixe em branco)
    * Clique em "Connect".

3.  **Altere o Perfil do Usuário para ADMIN:**
    * Dentro do console H2, você verá a tabela `USUARIOS` à esquerda.
    * Execute o seguinte comando SQL para atualizar o perfil do usuário que você acabou de criar:
      ```sql
      UPDATE USUARIOS SET PERFIL = 'ADMIN' WHERE EMAIL = 'admin@email.com';
      ```
    * Clique em "Run". Pronto! O usuário `admin@email.com` agora é um administrador.

#### **Parte 1: Setup do Sistema (Como ADMIN)**
Primeiro, o `ADMIN` precisa popular o sistema com dados básicos.

1.  **Faça Login como ADMIN:**
    * No Swagger, vá em `auth-controller > POST /auth/login`.
    * Use as credenciais do admin (`admin@email.com`, `password123`).
    * **Copie o `token` JWT da resposta.**

2.  **Autorize suas Requisições:**
    * No topo da página do Swagger, clique no botão verde **"Authorize"**.
    * Na janela que abrir, digite `Bearer ` (com espaço no final) e cole o seu token na frente. Deve ficar assim: `Bearer eyJhbGciOiJI...`
    * Clique em "Authorize" e depois em "Close". Agora todas as suas requisições estarão autenticadas.

3.  **Crie um Esporte:**
    * Vá em `esporte-controller > POST /esportes`.
    * Corpo da requisição: `{"nome": "Futebol"}`.
    * Execute e confirme a criação.

4.  **Crie os Times:**
    * Vá em `time-controller > POST /times`.
    * Crie dois times, um de cada vez. Use o `esporteId` que foi retornado no passo anterior (provavelmente `1`).
        * Time 1: `{"nome": "Flamengo", "esporteId": 1}`
        * Time 2: `{"nome": "Palmeiras", "esporteId": 1}`

5.  **Crie um Evento Esportivo:**
    * Vá em `evento-controller > POST /eventos`.
    * Use os IDs do esporte e dos times criados.
      ```json
      {
        "esporteId": 1,
        "timeAId": 1,
        "timeBId": 2,
        "dataHora": "2025-12-10T20:00:00",
        "local": "Maracanã",
        "oddTimeA": 1.5,
        "oddTimeB": 2.8,
        "oddEmpate": 2.2
      }
      ```

#### **Parte 2: Ações do Usuário (Como APOSTADOR)**
Agora, vamos simular um usuário comum.

1.  **Registre o APOSTADOR:**
    * Vá em `auth-controller > POST /auth/register` e crie um novo usuário (ex: `apostador@email.com`, senha `password123`).

2.  **Faça Login como APOSTADOR:**
    * Use o endpoint de login com as credenciais do apostador.
    * Copie o novo token e **atualize a autorização no Swagger** (clique em "Authorize", "Logout", e cole o novo token com `Bearer `).

3.  **Consulte os Eventos Públicos:**
    * Vá em `evento-controller > GET /eventos/publicos`.
    * Execute e veja que o evento "Flamengo x Palmeiras" aparece na lista.

4.  **Adicione Saldo à Carteira:**
    * Vá em `carteira-controller > POST /carteira/adicionar`.
    * Informe o `valor` que deseja depositar (ex: `100`) e execute.
    * Verifique se o saldo foi atualizado em `carteira-controller > GET /carteira/saldo`.

5.  **Faça uma Aposta:**
    * Vá em `aposta-controller > POST /apostas`.
    * Aposte R$ 10,00 na vitória do Flamengo (`TIME_A`).
      ```json
      {
        "eventoId": 1,
        "valor": 10,
        "escolha": "TIME_A"
      }
      ```
    * Execute. Verifique seu saldo em `GET /carteira/saldo` e veja que ele foi debitado.

#### **Parte 3: Finalização do Evento (Como ADMIN)**
1.  **Re-autorize como ADMIN:** Volte a usar o token do admin no botão "Authorize".
2.  **Simule o Resultado do Evento:**
    * Vá em `aposta-controller > POST /apostas/simular/{eventoId}`.
    * Informe o `eventoId` como `1` e execute. O sistema definirá o resultado aleatoriamente.

#### **Parte 4: Verificação de Resultados (Como APOSTADOR)**
1.  **Re-autorize como APOSTADOR:** Volte a usar o token do apostador.
2.  **Consulte suas Apostas:**
    * Vá em `aposta-controller > GET /apostas` e veja que sua aposta agora tem os campos `acertou` e `retorno` preenchidos.
3.  **Consulte o Saldo Final:**
    * Vá em `carteira-controller > GET /carteira/saldo`. Se você ganhou a aposta, seu saldo terá aumentado com o prêmio.
4.  **Verifique o Histórico de Transações:**
    * Vá em `transacao-controller > GET /transacoes/meu-extrato`. Você verá todo o histórico: o `DEPOSITO` de 100, a `APOSTA` de 10 e, se ganhou, o `PREMIO` recebido.

#### **Parte 5: Teste das Funções Extras**
1.  **(Como ADMIN) Crie uma Promoção:**
    * Autorize-se como ADMIN.
    * Vá em `promocao-controller > POST /promocoes`.
      ```json
      {
        "titulo": "Bônus de Natal",
        "descricao": "Ganhe R$5 em apostas!",
        "valorBonus": 5.0,
        "dataValidade": "2025-12-25"
      }
      ```
    * Execute.
2.  **(Como APOSTADOR ou anônimo) Veja as Promoções:**
    * Não precisa de autorização para este passo.
    * Vá em `promocao-controller > GET /promocoes` e veja a promoção criada.

#### **Parte 6: Teste dos Relatórios (Como ADMIN)**
1.  **Autorize-se como ADMIN.**
2.  **Verifique os Relatórios:**
    * `relatorio-controller > GET /relatorios/volume-apostas`: Veja o total apostado no evento de ID 1.
    * `relatorio-controller > GET /relatorios/lucro-prejuizo`: Veja o resultado financeiro da plataforma com base nas apostas feitas e prêmios pagos.

---

## Conclusão

Este projeto cumpre com sucesso todos os requisitos propostos, resultando em uma API RESTful completa e funcional para um sistema de apostas esportivas. Utilizando tecnologias modernas como Spring Boot, Spring Security com JWT e Spring Data JPA, a aplicação demonstra uma arquitetura robusta, segura e escalável.

Além das funcionalidades básicas, foram implementados dois módulos extras (Promoções e Histórico de Transações) que agregam valor e complexidade ao sistema, tornando-o mais próximo de uma aplicação real. A documentação interativa via Swagger/OpenAPI garante que a API seja fácil de entender e testar. O resultado final é uma aplicação bem estruturada que serve como uma excelente demonstração prática dos princípios de desenvolvimento de software back-end.
