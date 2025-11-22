## 🧠 Desafio Técnico – Sistema de Gestão de Projetos e Demandas

### 📘 Contexto
Sua missão é desenvolver uma **API RESTful em Java com Spring Boot** para gerenciar **projetos e tarefas (demandas)** de uma empresa.  
O sistema será utilizado por um time de desenvolvimento para organizar suas entregas, acompanhar o status das tarefas e realizar análises simples.

---

## 🎯 Requisitos Técnicos

### 🧱 1. Modelagem de Domínio

A modelagem pode ser modificada pelo inscrito. Porém, precisa ser justificado o motivo.

#### `Project`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | UUID | Identificador |
| `name` | String (3–100) | **Obrigatório** |
| `description` | String | Opcional |
| `startDate` | Date | Início do projeto |
| `endDate` | Date | Opcional |

#### `Task`
| Campo | Tipo | Descrição           |
|-------|------|---------------------|
| `id` | UUID | Identificador       |
| `title` | String (5–150) | **Obrigatório**     |
| `description` | String | Detalhes da tarefa  |
| `status` | Enum | TODO / DOING / DONE |
| `priority` | Enum | LOW / MEDIUM / HIGH |
| `dueDate` | Date | Data limite         |
| `projectId` | FK(Project) | Projeto associado   |

---

### 🌐 2. Endpoints REST

#### Autenticação e Usuários
| Método | Endpoint         | Descrição                                   |
|---------|------------------|---------------------------------------------|
| **POST** | `/auth/login`    | Autenticar usuário (retorna JWT token)      |
| **POST** | `/users`         | Registrar novo usuário      |


#### Projetos
| Método | Endpoint                    | Descrição                                              |
|---------|-----------------------------|--------------------------------------------------------|
| **POST** | `/projects`                 | Criar novo projeto (`name` e `startDate` obrigatórios) |
| **GET** | `/projects`                 | Listar todos os projetos (paginação)                   |
| **GET** | `/projects/{id}`            | Listar projeto por id                                  |
| **DELETE** | `/projects/owner/{ownerId}` | Lista todos os projetos por owner                      |
| **PUT** | `/projects/{id}`            | Atualizar projeto (apenas campos não nulos)            |

#### Tarefas
| Método | Endpoint                    | Descrição                                     |
|---------|-----------------------------|-----------------------------------------------|
| **POST** | `/tasks`                    | Criar nova tarefa vinculada a um projeto      |
| **GET** | `/tasks`                    | Listar todas as tarefas com filtros opcionais |
| **GET** | `/tasks/{id}`               | Listar tarefa por id                           |
| **GET** | `/tasks/project/{projectId}` | Listar todas as tarefas por projeto           |
| **PUT** | `/tasks/{id}`               | Atualizar tarefa (apenas campos não nulos)    |
| **DELETE** | `/tasks/{id}`               | Remover tarefa                                |

---

## ✅ Requisitos Obrigatórios
- 🧑‍💻 **Java 17+** e **Spring Boot 3+**  
- 🧠 **Spring Data JPA**  
- 🗄️ Banco Relacional (**PostgreSQL** ou **H2**)  
- ✔️ **Bean Validation**  
- 🧪 **Testes Automatizados**  
  - Unitários (Services mockados)  
  - Integração (Controllers com MockMvc ou Testcontainers)  
- ⚠️ Tratamento de erros com `@ControllerAdvice`  
- 📦 Uso de **DTOs** (`record` ou classes simples)  
- 📘 **README** explicando como rodar o projeto

---

## 🏅 Diferenciais (Pontos Extras)
- 🧭 Documentação **Swagger / OpenAPI**  
- 🔐 Autenticação simples com **JWT** ou Basic Auth  
- 🐳 Configuração de **Docker** / **docker-compose**  
- ⚡ Uso de **MapStruct** para mapeamento de DTOs  
- 🔍 Testes de API com **RestAssured**

---

## 🛠️ Tags
`#Java` `#SpringBoot` `#Backend` `#DesafioTecnico`  
`#API` `#RestAPI` `#Docker` `#Kubernetes`  
`#PostgreSQL` `#Oracle` `#JPA` `#Swagger`  
`#RestAssured` `#CleanCode` `#SoftwareEngineering`

---

### 💡 Dica
> Foque em **organização, boas práticas e clareza do código**.  
> Um bom README e commits bem descritos também serão avaliados. 😉

---

### 🧾 Licença
Este projeto foi desenvolvido exclusivamente para o **processo seletivo SIS Innov & Tech** e não deve ser utilizado para fins comerciais.

---
