# Digital Buzz - News Content Management System

A comprehensive, production-ready News Content Management System built with Spring Boot 3.x, implementing modern Java best practices and enterprise-level architecture.

## 🚀 Features

### Core Functionality
- **User Management**: Complete user authentication and authorization with JWT
- **News Articles**: Create, read, update, delete, publish, and archive news articles
- **Categories**: Organize articles into categories
- **Tags**: Tag articles for better discoverability  
- **Comments**: User comments on articles with approval workflow
- **Search**: Full-text search across articles
- **Pagination**: Efficient pagination and sorting for list endpoints

### Security
- JWT-based authentication
- BCrypt password hashing
- Role-based access control (RBAC) with 4 roles: ADMIN, EDITOR, AUTHOR, READER
- CORS configuration
- Secure headers (X-Frame-Options, X-Content-Type-Options)

### Middleware
- Request/Response logging with correlation IDs
- Request tracing for distributed systems
- Global exception handling
- Bean validation

### API Documentation
- Interactive Swagger UI
- OpenAPI 3.0 specification
- Comprehensive endpoint documentation

## 🛠 Technology Stack

- **Java**: 17
- **Framework**: Spring Boot 3.2.2
- **Security**: Spring Security with JWT
- **Database**: MySQL 8.0 (H2 for dev/test)
- **ORM**: Spring Data JPA with Hibernate
- **Database Migration**: Flyway
- **DTO Mapping**: MapStruct
- **API Documentation**: SpringDoc OpenAPI
- **Build Tool**: Maven 3.9+
- **Containerization**: Docker & Docker Compose

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- MySQL 8.0 (for production)
- Docker & Docker Compose (optional, for containerized deployment)

## 🔧 Installation & Setup

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/SriHarsha-Godavarthi/digital-buzz.git
   cd digital-buzz
   ```

2. **Configure database** (optional - uses H2 by default in dev profile)
   
   Edit `src/main/resources/application-prod.yml` if using MySQL:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/digitalbuzz
       username: your_username
       password: your_password
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The application will start on `http://localhost:8080/api`

5. **Access Swagger UI**
   
   Navigate to: `http://localhost:8080/api/swagger-ui.html`

### Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

   This will start:
   - MySQL database on port 3306
   - Spring Boot application on port 8080

2. **Access the application**
   
   API: `http://localhost:8080/api`
   
   Swagger UI: `http://localhost:8080/api/swagger-ui.html`

3. **Stop the containers**
   ```bash
   docker-compose down
   ```

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | User login | Public |
| POST | `/api/auth/logout` | User logout | Authenticated |

### User Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/users/me` | Get current user profile | Authenticated |
| PUT | `/api/users/me` | Update current user profile | Authenticated |
| GET | `/api/users` | List all users | Admin |
| PUT | `/api/users/{id}/role` | Update user role | Admin |

### Articles

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/articles` | Create article | Author/Editor/Admin |
| GET | `/api/articles` | List articles (paginated) | Public |
| GET | `/api/articles/{id}` | Get article by ID | Public |
| PUT | `/api/articles/{id}` | Update article | Owner/Editor/Admin |
| DELETE | `/api/articles/{id}` | Delete article | Owner/Admin |
| PATCH | `/api/articles/{id}/publish` | Publish article | Editor/Admin |
| PATCH | `/api/articles/{id}/archive` | Archive article | Owner/Editor/Admin |
| GET | `/api/articles/search?keyword={keyword}` | Search articles | Public |

### Categories

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/categories` | Create category | Admin |
| GET | `/api/categories` | List all categories | Public |
| GET | `/api/categories/{id}` | Get category by ID | Public |
| PUT | `/api/categories/{id}` | Update category | Admin |
| DELETE | `/api/categories/{id}` | Delete category | Admin |
| GET | `/api/categories/{id}/articles` | Get articles by category | Public |

### Tags

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/tags` | Create tag | Admin/Editor |
| GET | `/api/tags` | List all tags | Public |
| GET | `/api/tags/{id}` | Get tag by ID | Public |
| PUT | `/api/tags/{id}` | Update tag | Admin/Editor |
| DELETE | `/api/tags/{id}` | Delete tag | Admin |

### Comments

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/articles/{id}/comments` | Add comment | Authenticated |
| GET | `/api/articles/{id}/comments` | Get article comments | Public |
| PUT | `/api/comments/{id}` | Update comment | Owner |
| DELETE | `/api/comments/{id}` | Delete comment | Owner/Editor/Admin |
| PATCH | `/api/comments/{id}/approve` | Approve comment | Editor/Admin |

## 🧪 Testing

Run all tests:
```bash
./mvnw test
```

Run tests with coverage:
```bash
./mvnw clean test jacoco:report
```

View coverage report:
```bash
open target/site/jacoco/index.html
```

## 🗄 Database Schema

The application uses Flyway for database migrations. Migrations are located in `src/main/resources/db/migration/`.

### Main Tables
- `users` - User accounts and profiles
- `categories` - Article categories
- `tags` - Article tags
- `news_articles` - News articles
- `article_tags` - Many-to-many relationship between articles and tags
- `comments` - User comments on articles

## 🔐 Default Users

The seed data includes the following test users (password: `password123` for all):

| Username | Email | Role | Description |
|----------|-------|------|-------------|
| admin | admin@digitalbuzz.com | ADMIN | Full system access |
| editor1 | editor1@digitalbuzz.com | EDITOR | Can publish articles and approve comments |
| author1 | author1@digitalbuzz.com | AUTHOR | Can create and manage own articles |
| reader1 | reader1@digitalbuzz.com | READER | Can read articles and comment |

## ⚙️ Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile (dev/test/prod) | dev |
| `DB_URL` | Database URL | jdbc:mysql://localhost:3306/digitalbuzz |
| `DB_USERNAME` | Database username | root |
| `DB_PASSWORD` | Database password | root |
| `JWT_SECRET` | JWT signing secret | (see application.yml) |

### Profiles

- **dev**: Uses H2 in-memory database, detailed logging
- **test**: Uses H2 in-memory database for testing
- **prod**: Uses MySQL database, optimized for production

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/digitalbuzz/
│   │   ├── DigitalBuzzApplication.java
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST API controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access layer
│   │   ├── model/               # Domain entities
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── middleware/          # Filters and interceptors
│   │   ├── exception/           # Exception handling
│   │   └── util/                # Utility classes
│   └── resources/
│       ├── application.yml      # Main configuration
│       ├── application-dev.yml  # Development config
│       ├── application-prod.yml # Production config
│       ├── logback-spring.xml   # Logging configuration
│       └── db/migration/        # Flyway migrations
└── test/                        # Unit and integration tests
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **SriHarsha Godavarthi** - *Initial work*

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- All open-source contributors whose libraries made this possible

## 📞 Support

For support, email support@digitalbuzz.com or open an issue in the repository.

---

**Built with ❤️ using Spring Boot**