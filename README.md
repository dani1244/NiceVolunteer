# NiceVolunteers - Plataforma de Voluntariado UA

[![Java CI](https://github.com/your-org/nicevolunteers/actions/workflows/ci.yml/badge.svg)](https://github.com/your-org/nicevolunteers/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nicevolunteers&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=nicevolunteers)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=nicevolunteers&metric=coverage)](https://sonarcloud.io/summary/new_code?id=nicevolunteers)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=nicevolunteers&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=nicevolunteers)

## Sobre o Projeto

Plataforma digital de voluntariado orientada para a comunidade académica da Universidade de Aveiro. Funciona como um marketplace que conecta promotores de oportunidades com voluntários, implementando um sistema de pontos e recompensas.

**Projeto académico para TQS (Testes e Qualidade de Software) - Recurso 2025/2026**

## Arquitetura e Tecnologias

### Backend
- **Spring Boot 3.2.2** - Framework principal
- **Java 21** - Linguagem de programação
- **Spring Data JPA** - Persistência
- **H2 Database** - Base de dados em memória
- **Spring Security** - Autenticação
- **Spring Boot Actuator** - Monitorização
- **Micrometer + Prometheus** - Métricas

### Frontend
- **React 19** - Framework UI
- **React Router v7** - Navegação
- **Axios** - Cliente HTTP
- **Vite** - Build tool

### Qualidade e Testes
- **JUnit 5** - Testes unitários
- **Mockito** - Mocking framework
- **Cucumber** - BDD testing
- **JaCoCo** - Code coverage
- **SonarCloud** - Análise estática
- **Testcontainers** - Testes de integração

## Estratégia de Testes e QA

### Pirâmide de Testes

```
         /\
        /  \  E2E (Cucumber BDD)
       /____\
      /      \  Integration Tests
     /________\
    /          \  Unit Tests (JUnit + Mockito)
   /____________\
```

### TDD (Test Driven Development)

Seguimos o ciclo **Red-Green-Refactor**:
1. Red: Escrever teste que falha
2. Green: Implementar código mínimo para passar
3. Refactor: Melhorar código mantendo testes verdes

### Cobertura de Testes

#### Resultados Atuais
- **Total de Testes**: 30
- **Sucesso**: 30 (100%)
- **Falhas**: 0
- **Line Coverage**: 70%+

#### Unit Tests
- `VolunteerServiceTest` - Lógica de registo de voluntários
- `AuthServiceTest` - Autenticação e login
- `OpportunityServiceTest` - Gestão de oportunidades
- `PointsServiceTest` - Sistema de pontos
- `RecommendationServiceTest` - Recomendações personalizadas
- `EmailTest` - Validação de value objects
- `PasswordTest` - Validação de passwords

#### Integration Tests
- `VolunteerRepositoryTest` - Spring Data JPA
- `VolunteerControllerTest` - MockMvc + REST API

#### BDD Tests (Cucumber/Gherkin)
- `VolunteerRegistrationSteps` - Registo de voluntários
- `LoginSteps` - Fluxo de autenticação

### Métricas de Qualidade

| Métrica | Target | Status |
|---------|--------|--------|
| Line Coverage | >= 70% | PASS |
| Branch Coverage | >= 60% | PASS |
| Code Smells | < 50 | PASS |
| Bugs | 0 | PASS |
| Vulnerabilities | 0 | PASS |
| Duplicated Lines | < 3% | PASS |
| Cyclomatic Complexity | < 10 | PASS |

### Quality Gates (CI/CD)

```yaml
1. Build & Compile
2. Unit Tests (JUnit + Mockito)
3. Integration Tests
4. BDD Tests (Cucumber)
5. Code Coverage Analysis (JaCoCo)
6. Static Code Analysis (SonarCloud)
7. Security Vulnerability Scan
8. Quality Gate Check
```

## Como Executar

### Pré-requisitos
- Java 21
- Maven 3.8+
- Node.js 20+ (para frontend)

### Backend

```bash
# Executar testes
./mvnw test

# Ver relatório de cobertura
./mvnw test jacoco:report
# Abrir: target/site/jacoco/index.html

# Executar aplicação
./mvnw spring-boot:run
```

Servidor disponível em: `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Aplicação disponível em: `http://localhost:5174`

### Testes

```bash
# Executar todos os testes + coverage
./mvnw clean verify

# Apenas unit tests
./mvnw test

# Apenas BDD tests
./mvnw test -Dtest=CucumberTestRunner

# Gerar relatório JaCoCo
./mvnw jacoco:report
```

## Monitorização (Observability)

### Actuator Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Métricas Prometheus
curl http://localhost:8080/actuator/prometheus

# Info da aplicação
curl http://localhost:8080/actuator/info

# Métricas HTTP
curl http://localhost:8080/actuator/metrics
```

### Métricas Disponíveis
- `http.server.requests` - Latência das requests
- `jvm.memory.used` - Uso de memória
- `jvm.gc.pause` - Garbage collection
- `volunteer.registrations.total` - Total de registos
- `opportunities.applications.total` - Candidaturas

## Estrutura do Projeto

```
NiceVolunteer/
├── src/
│   ├── main/
│   │   ├── java/pt/ua/nicevolunteers/
│   │   │   ├── auth/              # Autenticação
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── dto/
│   │   │   │   └── exception/
│   │   │   ├── volunteer/         # Domínio principal
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   └── domain/
│   │   │   ├── config/            # Configurações
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── monitoring/        # Métricas
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-test.properties
│   └── test/
│       ├── java/pt/ua/nicevolunteers/
│       │   ├── volunteer/
│       │   │   ├── service/       # Unit tests
│       │   │   ├── controller/    # Integration tests
│       │   │   ├── repository/    # Repository tests
│       │   │   ├── domain/        # Value object tests
│       │   │   └── bdd/           # BDD step definitions
│       │   ├── auth/              # Auth tests
│       │   └── config/            # Test configurations
│       └── resources/
│           └── features/          # Cucumber .feature files
├── frontend/                      # React application
├── .github/
│   └── workflows/
│       └── ci.yml                 # CI/CD pipeline
├── sonar-project.properties       # SonarCloud config
└── pom.xml                        # Maven configuration
```

## Práticas de Qualidade Implementadas

### Clean Code
- Nomenclatura clara e consistente
- Métodos pequenos e focados (< 20 linhas)
- Sem duplicação de código
- Comentários apenas quando necessário
- Princípio DRY (Don't Repeat Yourself)

### SOLID Principles
- **S**ingle Responsibility Principle
- **O**pen/Closed Principle
- **L**iskov Substitution Principle
- **I**nterface Segregation Principle
- **D**ependency Inversion Principle

### Testing Best Practices
- **AAA Pattern**: Arrange, Act, Assert
- **Given-When-Then**: BDD scenarios
- **Test Isolation**: Cada teste independente
- **Meaningful Names**: Nomes descritivos
- **One Assertion per Test**: Quando possível
- **Mock External Dependencies**: Mockito

### CI/CD Best Practices
- Automated testing em cada commit
- Quality gates obrigatórios
- Coverage reports automáticos
- SonarCloud integration
- Fail fast strategy

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login de utilizador

### Volunteers
- `POST /api/volunteers` - Registar voluntário
- `POST /api/volunteers/register` - Alias de registo
- `GET /api/volunteers/{id}` - Obter voluntário
- `GET /api/volunteers/{id}/points` - Pontos do voluntário
- `GET /api/volunteers/{id}/applications` - Candidaturas

### Opportunities
- `GET /api/opportunities/open` - Listar abertas
- `GET /api/opportunities/{id}` - Detalhes
- `POST /api/opportunities/{id}/apply` - Candidatar-se

### Monitoring
- `GET /actuator/health` - Health check
- `GET /actuator/prometheus` - Métricas
- `GET /actuator/metrics` - Endpoint de métricas

## Documentação Adicional

- **Manual de Qualidade**: Práticas e processos de QA
- **Relatório Técnico**: Decisões arquiteturais
- **Frontend README**: [frontend/README.md](frontend/FRONTEND_README.md)

## Configuração do SonarCloud

Para ativar análise no SonarCloud:

1. Criar conta em https://sonarcloud.io
2. Criar novo projeto
3. Adicionar `SONAR_TOKEN` aos secrets do GitHub
4. Atualizar `sonar.organization` em `sonar-project.properties`

## Contribuir

Este é um projeto académico individual para avaliação de Recurso em TQS.

## Autor

Projeto desenvolvido para a disciplina de **Testes e Qualidade de Software** - Universidade de Aveiro, 2025/2026

---

**Foco em Qualidade, Testes e Boas Práticas**
