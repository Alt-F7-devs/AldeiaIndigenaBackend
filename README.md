# SEI — Sistemas para Escolas Indígenas

API REST para o gerenciamento de uma escola de ensino indígena Guarani (**Educom Guarani**), com foco no ensino lúdico da língua Guarani para os jovens da escola.

---

## 📖 Sobre o projeto / Motivação

O **SEI (Sistemas para Escolas Indígenas)** nasceu para apoiar a **Educom Guarani**, uma iniciativa de educação indígena Guarani. A proposta é oferecer um **meio lúdico de ensino da língua Guarani** para os jovens da escola, aproximando tecnologia e cultura.

Pensando de forma simples, sem termos técnicos:

- A **escola** tem uma secretaria que organiza tudo. No sistema, essa secretaria é a conta **Admin**.
- A secretaria cadastra os **Professores**, que atuam nas salas de aula.
- A secretaria também cadastra os **Alunos**, que aprendem e têm a **presença contabilizada**.
- São criadas **salas de aula** (que existem de verdade, no mundo real), e nelas os alunos são adicionados.
- Dentro de uma sala é possível **adicionar jogos** e **simular uma partida** que ensina Guarani, registrando quem esteve presente.

> ⚠️ **Sobre os jogos:** neste momento os jogos são apenas um **mock** (simulação). Ainda **não existem jogos jogáveis de verdade**. É possível cadastrar um jogo, vinculá-lo a uma sala e simular uma partida feita na sala com os alunos, contabilizando a presença. A API possui outras funcionalidades, mas essas são as principais.

---

## 🧰 Tecnologias / Stack

Extraído do `pom.xml` e da estrutura do projeto:

| Categoria | Tecnologia |
|---|---|
| Linguagem | **Java 21** |
| Framework | **Spring Boot 4.0.5** |
| Build | **Maven** |
| Web | Spring Boot Starter Web |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | **PostgreSQL** |
| Migrations | **Flyway** (core + PostgreSQL) |
| Segurança | Spring Security (sessão HTTP + CSRF) + Spring Session |
| Validação | Hibernate Validator |
| Documentação | **SpringDoc OpenAPI** (Swagger UI) |
| Produtividade | Lombok, Spring Boot DevTools |
| Observabilidade | Spring Boot Actuator |
| Qualidade / Cobertura | JaCoCo + integração SonarCloud |
| Containerização | **Docker** (`eclipse-temurin:21-jre-alpine`) |

A aplicação sobe por padrão na porta **8080**.

---

## 🏛️ Arquitetura do projeto

O projeto segue o padrão **MVC em camadas**, com responsabilidades bem separadas. O pacote raiz é `com.altf7.sei`.

```
src/main/java/com/altf7/sei
├── controller/   → Camada de entrada (REST). Recebe as requisições HTTP e devolve respostas.
├── service/      → Regras de negócio. Onde a lógica realmente acontece.
├── repository/   → Acesso a dados (Spring Data JPA). Conversa com o banco.
├── entity/       → Entidades JPA. Representam as tabelas do banco.
├── dto/          → Objetos de transporte (request/response). Não expõem a entidade diretamente.
├── validator/    → Validações específicas (CPF, CGM, senha, credenciais, etc.).
├── exception/    → Exceções de domínio (ex.: AlunoInvalidException, NotFoundException).
├── handler/      → GlobalExceptionHandler (tratamento centralizado de erros).
├── config/       → Configurações (ex.: CORS).
├── SecurityConfig.java → Configuração de segurança, rotas e permissões.
└── SeiApplication.java → Classe principal (ponto de entrada).
```

**Fluxo de uma requisição:**

```
Cliente → Controller → Service → Repository → Banco de Dados
                ↑           ↓
              DTO       Entity
```

- **Controller** recebe a requisição, valida o básico e delega para o Service.
- **Service** aplica as regras de negócio e usa os Repositories.
- **Repository** persiste/consulta os dados.
- **Entity** mapeia as tabelas; **DTO** define o que entra e o que sai da API.
- Erros são capturados de forma centralizada pelo **GlobalExceptionHandler**.

---

## ✅ Pré-requisitos

Para rodar localmente você precisa de:

- **Java 21 (JDK)**
- **Maven** (ou use o wrapper `./mvnw` incluído no projeto)
- **PostgreSQL** acessível (local ou remoto)
- **Docker** e **Docker Compose** (opcional, para rodar em container)

---

## ⚙️ Variáveis de ambiente

A aplicação espera as seguintes **variáveis de ambiente** (apenas os **nomes** — nunca versione os valores reais). Há um arquivo `env.example` na raiz como referência.

| Variável | Descrição |
|---|---|
| `DB_USERNAME` | Usuário do banco de dados |
| `DB_PASSWORD` | Senha do banco de dados |
| `DB_URL` | URL de conexão JDBC do PostgreSQL (host, porta, banco e schema) |

> 🔒 **Segurança:** não comite o arquivo `.env`, `docker-compose.yml` com credenciais, nem valores de senha/secret. Use o `env.example` apenas como modelo, preenchendo os valores em um `.env` local fora do versionamento.

---

## 🚀 Como executar a aplicação

### Opção 1 — Localmente (Maven)

1. Crie um arquivo `.env` (baseado no `env.example`) e preencha `DB_USERNAME`, `DB_PASSWORD` e `DB_URL`, ou exporte essas variáveis no seu ambiente.
2. Garanta que o PostgreSQL esteja em execução e acessível pela `DB_URL`.
3. Compile e rode:

```bash
# Compilar e empacotar (gera o .jar em target/)
./mvnw clean package

# Rodar a aplicação
./mvnw spring-boot:run
```

Ou execute o `.jar` gerado diretamente:

```bash
java -jar target/sei-0.0.1-SNAPSHOT.jar
```

A API ficará disponível em: **http://localhost:8080**

> 💡 O **Flyway** roda automaticamente na inicialização e aplica as migrations, criando/atualizando o schema `sei_db`.

### Opção 2 — Via Docker

O `Dockerfile` empacota o `.jar` já construído e expõe a porta **8080**:

```bash
# 1) Gere o .jar primeiro
./mvnw clean package

# 2) Construa a imagem
docker build -t sei-api .

# 3) Rode o container, passando as variáveis de ambiente
docker run -p 8080:8080 \
  -e DB_USERNAME=... \
  -e DB_PASSWORD=... \
  -e DB_URL=... \
  sei-api
```

Se preferir usar o **Docker Compose** (orquestrando API + banco), suba com:

```bash
docker compose up --build
```

> As variáveis de ambiente (`DB_USERNAME`, `DB_PASSWORD`, `DB_URL`) devem ser fornecidas via arquivo `.env`/secrets — **nunca** diretamente no repositório.

---

## 👥 Hierarquia de usuários

O sistema possui três níveis de acesso, com papéis bem definidos. Em geral, o poder de gestão diminui conforme se desce na hierarquia:

```
ADMIN  >  PROFESSOR  >  ALUNO
```

| Nível | Quem é | O que pode fazer |
|---|---|---|
| **ADMIN** | A Secretaria da Escola | Controle total: cadastra/edita/remove **Professores** e **Alunos**, gerencia **Salas** e **Jogos**, e visualiza **frequência**. É o único que pode deletar uma sala. |
| **PROFESSOR** | Atua nas salas de aula virtuais | Cria e edita **Salas**, adiciona **Alunos** e **Jogos** às salas, gerencia **Jogos**, registra/remove **presença** e consulta **frequência**. Visualiza alunos e professores. |
| **ALUNO** | Aprende e tem presença contabilizada | Visualiza **Jogos** (geral e por sala) e **registra a própria presença** numa sala. |

A autenticação é baseada em **sessão (cookie)** com proteção **CSRF**.

> ℹ️ **Login do Admin:** não há um endpoint dedicado de login para o Admin. O Admin autentica-se pelo endpoint `POST /api/v1/auth/login/professor` (o serviço tenta primeiro autenticar como Admin e, em seguida, como Professor).

---

## 🔌 Endpoints da API

Base comum: `http://localhost:8080`. Todos os recursos ficam sob o prefixo `/api/v1`.

### 🔑 Autenticação — `/api/v1/auth`

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `POST` | `/api/v1/auth/login/professor` | Login de **Professor** ou **Admin** (campos: `cpf`, `senha`) | Público |
| `POST` | `/api/v1/auth/login/aluno` | Login de **Aluno** (campos: `cgm`, `senha`) | Público |
| `POST` | `/api/v1/auth/logout` | Encerra a sessão atual | Público (autenticado) |
| `GET`  | `/api/v1/csrf-token` | Retorna o token CSRF para uso nas requisições | Público |

Resposta de login (`LoginResponseDTO`): `{ "tipo": "ADMIN|PROFESSOR|ALUNO", "id": <Integer> }`.

### 🛡️ Admin — `/api/v1/admin`

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `POST` | `/api/v1/admin` | Cria um usuário **Admin** (campos: `login`, `senha`) | Público |
| `POST` | `/api/v1/admin/aluno` | Cria um **Aluno** | ADMIN |
| `GET` | `/api/v1/admin/aluno` | Lista todos os Alunos | ADMIN |
| `GET` | `/api/v1/admin/aluno/{id_aluno}` | Busca Aluno por id | ADMIN |
| `PATCH` | `/api/v1/admin/aluno/{id_aluno}` | Atualiza dados do Aluno | ADMIN |
| `DELETE` | `/api/v1/admin/aluno/{id_aluno}` | Remove o Aluno | ADMIN |
| `POST` | `/api/v1/admin/professor` | Cria um **Professor** | ADMIN |
| `GET` | `/api/v1/admin/professor` | Lista todos os Professores | ADMIN |
| `GET` | `/api/v1/admin/professor/{id_professor}` | Busca Professor por id | ADMIN |
| `PATCH` | `/api/v1/admin/professor/{id_professor}` | Atualiza dados do Professor | ADMIN |
| `DELETE` | `/api/v1/admin/professor/{id_professor}` | Remove o Professor | ADMIN |

### 🎓 Aluno — `/api/v1/aluno`

> Caminho alternativo de gestão de alunos (além de `/api/v1/admin/aluno`).
> Campos do corpo (`AlunoRequestDTO`): `nome`, `senha`, `cgm`, `admin_login`.

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `POST` | `/api/v1/aluno` | Cria um Aluno | ADMIN |
| `GET` | `/api/v1/aluno` | Lista todos os Alunos | ADMIN, PROFESSOR |
| `GET` | `/api/v1/aluno/{id_aluno}` | Busca Aluno por id | ADMIN, PROFESSOR |
| `PATCH` | `/api/v1/aluno/{id_aluno}` | Atualiza dados do Aluno | ADMIN |
| `DELETE` | `/api/v1/aluno/{id_aluno}` | Remove o Aluno | ADMIN |

### 👨‍🏫 Professor — `/api/v1/professor`

> Caminho alternativo de gestão de professores (além de `/api/v1/admin/professor`).
> Campos do corpo (`ProfessorRequestDTO`): `nome`, `cpf`, `senha`, `admin_login`.

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `POST` | `/api/v1/professor` | Cria um Professor | ADMIN |
| `GET` | `/api/v1/professor` | Lista todos os Professores | ADMIN, PROFESSOR |
| `GET` | `/api/v1/professor/{id_professor}` | Busca Professor por id | ADMIN, PROFESSOR |
| `PATCH` | `/api/v1/professor/{id_professor}` | Atualiza dados do Professor | ADMIN |
| `DELETE` | `/api/v1/professor/{id_professor}` | Remove o Professor | ADMIN |

### 🏫 Sala — `/api/v1/sala`

> Campos do corpo (`SalaRequestDTO`): `admin_id`, `jogo_id_jogo`, `professor_id_professor`, `num_sa`.

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `POST` | `/api/v1/sala` | Cria uma Sala | ADMIN, PROFESSOR |
| `GET` | `/api/v1/sala` | Lista todas as Salas | ADMIN, PROFESSOR |
| `GET` | `/api/v1/sala/{id_sala}` | Busca Sala por id | ADMIN, PROFESSOR |
| `PATCH` | `/api/v1/sala/{id_sala}` | Edição parcial da Sala | ADMIN, PROFESSOR |
| `DELETE` | `/api/v1/sala/{id_sala}` | Deleta a Sala | ADMIN |
| `POST` | `/api/v1/sala/{id_sala}/aluno/{cgm}` | Adiciona um Aluno (por CGM) à Sala | ADMIN, PROFESSOR |
| `GET` | `/api/v1/sala/{id_sala}/alunos` | Lista os Alunos da Sala | ADMIN, PROFESSOR |
| `GET` | `/api/v1/sala/aluno/{id_aluno}` | Lista Aluno por id (no contexto de sala) | ADMIN, PROFESSOR |
| `DELETE` | `/api/v1/sala/{id_sala}/professor` | Desvincula o Professor da Sala | ADMIN, PROFESSOR |
| `POST` | `/api/v1/sala/{id_sala}/jogos/{id_jogo}` | Adiciona um Jogo à Sala | ADMIN, PROFESSOR |
| `GET` | `/api/v1/sala/jogos` | Lista jogos (contexto de sala) | ADMIN, PROFESSOR, ALUNO |
| `GET` | `/api/v1/sala/jogos/{id}` | Busca jogo por id (contexto de sala) | ADMIN, PROFESSOR, ALUNO |
| `GET` | `/api/v1/sala/professor?id_professor={id}` | Lista as Salas de um Professor | ADMIN, PROFESSOR |

### 🎮 Jogo — `/api/v1/jogos`

> Campos do corpo (`JogoRequestDTO`): `nome`, `admin_login`.
> Os jogos são, no momento, um **mock** (sem partidas reais jogáveis).

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `GET` | `/api/v1/jogos` | Lista todos os Jogos | ADMIN, PROFESSOR, ALUNO |
| `GET` | `/api/v1/jogos/{id}` | Busca Jogo por id | ADMIN, PROFESSOR, ALUNO |
| `POST` | `/api/v1/jogos` | Cria um Jogo | ADMIN, PROFESSOR |
| `PUT` | `/api/v1/jogos/{id}` | Atualiza um Jogo | ADMIN, PROFESSOR |
| `DELETE` | `/api/v1/jogos/{id}` | Remove um Jogo | ADMIN, PROFESSOR |

### 📋 Presença — `/api/v1/presencas`

> Resposta de frequência (`PresencaResponseDTO`): `nomeAluno`, `cgm`, `numSa`, `percentualFrequencia`, `status`.

| Método | Rota | Descrição | Quem pode acessar |
|---|---|---|---|
| `POST` | `/api/v1/presencas/{cgm}/sala/{idSala}` | Registra a presença de um Aluno (por CGM) numa Sala | ADMIN, PROFESSOR, ALUNO |
| `DELETE` | `/api/v1/presencas/{cgm}/sala/{idSala}` | Remove a presença de um Aluno numa Sala | ADMIN, PROFESSOR |
| `GET` | `/api/v1/presencas/frequencia/{idAluno}` | Calcula a frequência de um Aluno | ADMIN, PROFESSOR |
| `GET` | `/api/v1/presencas/frequencia` | Calcula a frequência de todos os Alunos | ADMIN, PROFESSOR |

---

## 🧪 Como usar o Swagger para testar os endpoints

A documentação interativa é gerada automaticamente pelo **SpringDoc OpenAPI**.

- **Swagger UI:** http://localhost:8080/swagger-ui.html (ou `http://localhost:8080/swagger-ui/index.html`)
- **OpenAPI (JSON):** http://localhost:8080/v3/api-docs

### Fluxo de autenticação no Swagger

A API usa **sessão por cookie + CSRF**, então o fluxo de teste é:

1. Abra o **Swagger UI** no navegador.
2. **Obtenha o token CSRF**: chame `GET /api/v1/csrf-token`. O token também é gravado em um cookie.
3. **Autentique-se**: chame `POST /api/v1/auth/login/professor` (Admin/Professor) ou `POST /api/v1/auth/login/aluno` (Aluno) com as credenciais. A sessão (cookie) é criada automaticamente pelo navegador.
4. **Dispare as demais requisições**: como o navegador reenvia o cookie de sessão, os endpoints protegidos passam a responder conforme o seu papel (ADMIN/PROFESSOR/ALUNO).
5. Para requisições que **alteram estado** (POST/PUT/PATCH/DELETE), envie o **token CSRF** obtido no passo 2 no cabeçalho `X-XSRF-TOKEN`.
6. Ao terminar, chame `POST /api/v1/auth/logout` para encerrar a sessão.

> 💡 As rotas de login e a documentação (`/swagger-ui/**`, `/v3/api-docs/**`) são liberadas e isentas de CSRF.

---

## 🧭 Como testar os métodos (passo a passo)

Um roteiro prático ponta a ponta. Ajuste os ids/valores conforme o retorno de cada chamada.

1. **Autenticar como Admin**
   `POST /api/v1/auth/login/professor` com `{ "cpf": "<login do admin>", "senha": "<senha>" }`.
   > Já existe um **Admin inicial** criado pela migration V1 (login `1`), útil para o primeiro acesso.

2. **Criar um Professor**
   `POST /api/v1/admin/professor` com `{ "nome": "...", "cpf": "...", "senha": "...", "admin_login": 1 }`.

3. **Criar um Aluno**
   `POST /api/v1/admin/aluno` com `{ "nome": "...", "senha": "...", "cgm": "...", "admin_login": 1 }`.

4. **Criar um Jogo** (mock)
   `POST /api/v1/jogos` com `{ "nome": "Jogo das Palavras Guarani", "admin_login": 1 }`.

5. **Criar uma Sala**
   `POST /api/v1/sala` com `{ "admin_id": 1, "jogo_id_jogo": <id>, "professor_id_professor": <id>, "num_sa": "Sala 01" }`.

6. **Adicionar o Aluno à Sala**
   `POST /api/v1/sala/{id_sala}/aluno/{cgm}`.

7. **(Opcional) Adicionar/trocar o Jogo da Sala**
   `POST /api/v1/sala/{id_sala}/jogos/{id_jogo}`.

8. **Simular a partida e registrar presença**
   `POST /api/v1/presencas/{cgm}/sala/{idSala}` — registra que o aluno participou daquela sala/partida.

9. **Consultar a frequência**
   `GET /api/v1/presencas/frequencia/{idAluno}` (individual) ou `GET /api/v1/presencas/frequencia` (todos).

> Lembre-se: em cada requisição que altera dados, envie o cabeçalho `X-XSRF-TOKEN` com o token obtido em `GET /api/v1/csrf-token`.

---

## 🗄️ Banco de dados e migrations

O schema do banco é gerenciado pelo **Flyway**, com versionamento automático aplicado na inicialização da aplicação.

- **Local das migrations:** `src/main/resources/db/migration`
- **Schema:** `sei_db`
- **Convenção de nomes:** `V<versão>__<descrição>.sql` (ex.: `V1__sei_db_inital_schema.sql`).

Migrations existentes:

| Versão | Arquivo | O que faz |
|---|---|---|
| V1 | `V1__sei_db_inital_schema.sql` | Cria o schema `sei_db` e as tabelas `admin`, `aluno`, `professor`, `jogo`, `sala`, `presenca` (com índices e FKs). Insere o **Admin inicial** (login `1`). |
| V2 | `V2__sei_db_correction_atributes.sql` | Correção de atributos das tabelas. |
| V3 | `V3__sei_db_id_and_password_size_fix.sql` | Ajustes em ids e no tamanho do campo de senha. |
| V4 | `V4__sei_db_refactor_type_data.sql` | Refatoração de tipos de dados. |
| V5 | `V5__sei_db_add_columns.sql` | Adição de colunas. |
| V6 | `V6__sei_db_professor_nullable.sql` | Torna o professor opcional (nullable) na sala. |

**Modelo de dados (resumo):**

- `admin` — a secretaria; referenciada por aluno, professor, jogo e sala.
- `aluno` — possui `cgm` único; vinculado a uma `sala` (opcional).
- `professor` — possui `cpf` único; pode estar vinculado a salas.
- `jogo` — vinculado a um admin; associável a salas.
- `sala` — possui `num_sa` único, data de criação, e referências para professor, admin e jogo.
- `presenca` — tabela de associação **aluno × sala** (chave composta), garantindo que a mesma combinação não se repita.

> O Flyway roda automaticamente ao subir a aplicação. Para um banco limpo, basta apontar a `DB_URL` para uma base vazia que as migrations criam toda a estrutura.

---

## 📌 Observação final

Este projeto é de **cunho educativo** até o presente momento. Os jogos são, por ora, um **mock** — ainda não há partidas reais jogáveis, apenas a simulação de partidas e o registro de presença.

O projeto possui **permissão da Educom Guarani** para utilizar seus meios de imagem e seus fluxos de uso na construção desta API.