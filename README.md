#  Sistema de Gerenciamento de Tarefas

## Abstrato

Este projeto consiste em uma aplicação web que permite aos usuários gerenciar listas e itens de forma eficiente. A aplicação oferece funcionalidades para criação, edição, remoção e organização de listas e itens, incluindo a capacidade de destacar itens prioritários.

## Objetivo

Desenvolver uma aplicação que permita aos usuários:

1. Criar e gerenciar listas com itens associados.
2. Adicionar, editar, remover e alterar o estado de itens dentro das listas.
3. Visualizar e organizar listas e itens de maneira intuitiva, com opções de filtragem.
4. Destacar itens dentro das listas para indicar prioridade.

## Requisitos Funcionais

1. **Criação de Listas**: O usuário pode criar e gerenciar listas, cada uma contendo itens associados.
2. **Gerenciamento de Itens**: O usuário pode adicionar, editar, remover e alterar o estado de itens dentro de cada lista.
3. **Visualização e Filtragem**: O usuário pode visualizar e organizar listas e itens de forma intuitiva, com opções de filtragem disponíveis.
4. **Prioridade de Itens**: O usuário pode destacar itens dentro das listas para indicar prioridade.

## Regras de Negócio

1. **Validação de Dados**: Os itens dentro de uma lista devem seguir critérios básicos de validação, como comprimento mínimo do título.
2. **Estado dos Itens**: Cada item possui um estado que pode ser alterado pelo usuário.
3. **Ordenação e Destaque**: Itens destacados devem ser priorizados na visualização.

## Endpoints da API

### Listas

#### 1. Criar uma nova lista

- **Endpoint**: `POST http://localhost:8080/api/listas`
- **Corpo da Requisição**:
    ```json
    {
      "nome": "Nome da Lista",
      "itens": [
        {
          "titulo": "Título do Item",
          "descricao": "Descrição do Item"
        }
      ]
    }
    ```
- **Resposta**: `201 Created`
    ```json
    {
      "id": 1,
      "nome": "Nome da Lista",
      "itens": [
        {
          "id": 1,
          "titulo": "Título do Item",
          "descricao": "Descrição do Item",
          "estado": "PENDENTE",
          "prioridade": false
          "dataCriacao": "2024-08-25",
          "dataConclusao": null
        }
      ]
    }
    ```

#### 2. Buscar todas as listas (com filtros opcionais)

- **Endpoint**: `GET http://localhost:8080/api/listas`
- **Parâmetros de Consulta**:
  - `dataInicio` (opcional): Filtra listas criadas a partir desta data (Formato: `yyyy-MM-dd`)
  - `dataFim` (opcional): Filtra listas criadas até esta data (Formato: `yyyy-MM-dd`)
  - `page` (opcional): Página de resultados (padrão: 0)
  - `size` (opcional): Número de itens por página (padrão: 10)
  - `sort` (opcional): Ordenação (padrão: "nome,ASC")
- **Resposta**: `200 OK`
    ```json
    {
      "content": [
        {
          "id": 1,
          "nome": "Nome da Lista",
          "itens": 10
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 10
      },
      "totalElements": 1,
      "totalPages": 1
    }
    ```

#### 3. Buscar uma lista por ID

- **Endpoint**: `GET http://localhost:8080/api/listas/{listaId}`
- **Resposta**: `200 OK`
    ```json
    {
      "id": 1,
      "nome": "Nome da Lista",
     "dataCriacao": "2024-08-25",
      "itens": [
        {
          "id": 1,
          "titulo": "Título do Item",
          "descricao": "Descrição do Item",
          "estado": "PENDENTE",
          "prioridade": false
          "dataCriacao": "2024-08-25",
          "dataConclusao": null
        }
      ]
    }
    ```

#### 4. Deletar uma lista

- **Endpoint**: `DELETE http://localhost:8080/api/listas/{listaId}`
- **Resposta**: `204 No Content`

### Itens

#### 1. Adicionar um item a uma lista

- **Endpoint**: `POST /api/listas/{listaId}/itens`
- **Corpo da Requisição**:
    ```json
    {
      "titulo": "Título do Item",
      "descricao": "Descrição do Item"
    }
    ```
- **Resposta**: `201 Created`
    ```json
    {
      "id": 1,
      "titulo": "Título do Item",
      "descricao": "Descrição do Item",
      "estado": "PENDENTE",
      "prioridade": false
      "dataCriacao": "2024-08-25",
      "dataConclusao": null
    }
    ```

#### 2. Atualizar um item de uma lista

- **Endpoint**: `PATCH http://localhost:8080/api/listas/{listaId}/itens/{itemId}`
- **Corpo da Requisição**:
    ```json
    {
      "titulo": "Novo Título do Item",
      "descricao": "Nova Descrição do Item"
    }
    ```
- **Resposta**: `200 OK`
    ```json
    {
      "id": 1,
      "titulo": "Novo Título do Item",
      "descricao": "Nova Descrição do Item",
      "estado": "PENDENTE",
      "prioridade": false
      "dataCriacao": "2024-08-25",
      "dataConclusao": null
    }
    ```

#### 3. Deletar um item de uma lista

- **Endpoint**: `DELETE http://localhost:8080/api/listas/{listaId}/itens/{itemId}`
- **Resposta**: `204 No Content`

#### 4. Alterar o estado de um item para "Em Progresso"

- **Endpoint**: `PATCH /api/listas/{listaId}/itens/{itemId}/status-progresso`
- **Resposta**: `204 No Content`

#### 5. Concluir um item

- **Endpoint**: `PATCH http://localhost:8080/api/listas/{listaId}/itens/{itemId}/status-confirmado`
- **Resposta**: `204 No Content`

#### 6. Alterar a prioridade de um item

- **Endpoint**: `PATCH http://localhost:8080/api/listas/{listaId}/itens/{itemId}/prioridade`
- **Resposta**: `204 No Content`

## Validação de Dados

- **ListaRequestDTO**:
  - `nome`: O campo nome e obrigatorio e deve ter entre 8 e 50 caracteres.
  - `itens`: Os itens da lista nao podem ser nulos, preencha com pelo menos um item.

- **ItemRequestDTO**:
  - `titulo`: O campo titulo e obrigatorio e deve ter entre 8 e 50 caracteres.
  - `descricao`: O campo descricao e obrigatorio e deve ter entre 8 e 200 caracteres.

## Como Usar

1. Clone o repositório para sua máquina local com o comando git clone git@github.com:JoelMaciel/GERENCIADOR-TAREFAS.git
2. Importe o projeto em sua IDE preferida.
3. Execute a aplicação.
4. Utilize as ferramentas como Postman, Insomnia ou cURL para interagir com os endpoints conforme descrito acima.

## Executando com Docker Compose

Para facilitar a configuração e execução do bando de dados, um arquivo `docker-compose.yml` foi incluído. Siga os passos abaixo para executar o projeto com Docker Compose:

1. Certifique-se de ter o Docker e o Docker Compose instalados em sua máquina.
2. Navegue até o diretório onde o arquivo `docker-compose.yml` está localizado.
3. Execute o comando abaixo para iniciar os serviços:
   ```sh
   docker-compose up -d
   
4. Se quiser ver os dados persistidos, abra um conexão do MySQL na porta 3306 com a base de dados gerenciadordb,  username: root e password: root
   
## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Flyway
- Spring Data JPA
- Lombok
- MySQL - Docker
- Validation
- Maven
- JUnit - REST Assured
