# Agregador de Investimentos

Este projeto é um sistema de agregação de investimentos desenvolvido em **Java 21** com **Spring Boot**, que permite gerenciar usuários, contas e ações (stocks). Ele também se integra à API da **Brapi** para obter informações sobre os preços das ações em tempo real.

## 📌 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **JUnit 5** e **Mockito** (para testes)
- **MySQL** (banco de dados)
- **Brapi API** (integração para preços de ações)
- **Docker** e **Docker Compose** (para ambiente de desenvolvimento)
- **Spring Cloud OpenFeign** (para comunicação com APIs externas)

## 📁 Estrutura do Projeto

O projeto segue uma estrutura baseada em boas práticas da arquitetura em camadas:

- **Controller**: Responsável por expor as APIs REST
- **Service**: Contém a lógica de negócio
- **Repository**: Comunicação com o banco de dados
- **Entity**: Representação das tabelas do banco
- **DTO**: Transferência de dados entre as camadas
- **Client**: Comunicação com serviços externos (exemplo: Brapi API)

## ⚙️ Funcionalidades

- **Gerenciamento de Usuários**
  - Criar usuário
  - Listar usuários
  - Obter usuário por ID

- **Gerenciamento de Contas**
  - Criar conta
  - Listar contas
  - Obter conta por ID
  - Associar conta a um usuário

- **Gerenciamento de Ações (Stocks)**
  - Criar uma ação
  - Listar ações de uma conta
  - Obter o valor de uma ação via Brapi API

## 🚀 Como Executar o Projeto

### 1️⃣ Pré-requisitos

- Java 21 instalado
- Docker e Docker Compose instalados
- Maven instalado

### 2️⃣ Subindo o Banco de Dados com Docker

Execute o seguinte comando para iniciar o MySQL com Docker Compose:

```bash
docker-compose up -d
```

Isso iniciará um contêiner MySQL acessível na porta **3306**.

### 3️⃣ Configuração do Banco de Dados no Spring Boot

Atualize o arquivo `application.properties` com as seguintes configurações:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_example
spring.datasource.username=springuser
spring.datasource.password=ThePassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4️⃣ Executando o Projeto

```bash
mvn spring-boot:run
```

A API estará disponível em: [http://localhost:8080](http://localhost:8080)

## 🧪 Testes

Para executar os testes unitários, utilize o seguinte comando:

```bash
mvn test
```

## 📌 Contribuições

Fique à vontade para abrir issues e pull requests! 😊

## 📄 Licença

Este projeto está sob a licença MIT.

