# Lashes Studio - Система записи на прием

Современная веб-система для записи на прием в салон красоты с регистрацией пользователей и админ-панелью.

## 🚀 Возможности

### Для пользователей:
- ✅ Регистрация и вход (email + пароль)
- ✅ Функция "Запомнить меня"
- ✅ Выбор даты и времени для записи
- ✅ Просмотр своих записей
- ✅ Современный адаптивный интерфейс

### Для администраторов:
- ✅ Отдельная админ-панель
- ✅ Просмотр всех записей
- ✅ Фильтрация по дате и статусу
- ✅ Управление статусами записей
- ✅ Удаление записей
- ✅ Статистика записей

## 🛠 Технологии

- **Backend**: Spring Boot 3.5.0, Spring Security, JWT
- **Database**: PostgreSQL
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Authentication**: JWT токены с функцией "Запомнить меня"
- **Deployment**: Docker, Google Cloud Platform

## 📋 Требования

- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Docker (опционально)

## 🚀 Быстрый старт

### Локальная разработка

1. **Клонируйте репозиторий**
   ```bash
   git clone <repository-url>
   cd lashes
   ```

2. **Настройте базу данных**
   ```bash
   # Создайте базу данных PostgreSQL
   createdb lashes
   ```

3. **Настройте переменные окружения**
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/lashes
   export SPRING_DATASOURCE_USERNAME=postgres
   export SPRING_DATASOURCE_PASSWORD=your_password
   export JWT_SECRET=your_jwt_secret_key
   ```

4. **Запустите приложение**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Откройте браузер**
   - Главная страница: http://localhost:8080
   - Регистрация: http://localhost:8080/register
   - Вход: http://localhost:8080/login

### Создание первого администратора

После запуска приложения создайте первого пользователя через регистрацию, затем в базе данных измените роль на ADMIN:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your_admin_email@example.com';
```

## 🐳 Docker

### Сборка и запуск

```bash
# Сборка JAR файла
./mvnw clean package -DskipTests

# Сборка Docker образа
docker build -t lashes-app .

# Запуск контейнера
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/lashes \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  -e JWT_SECRET=your_jwt_secret_key \
  lashes-app
```

## ☁️ Развертывание на Google Cloud

### Вариант 1: Google App Engine

1. **Установите Google Cloud SDK**
   ```bash
   # Скачайте и установите gcloud CLI
   # https://cloud.google.com/sdk/docs/install
   ```

2. **Настройте проект**
   ```bash
   gcloud init
   gcloud config set project YOUR_PROJECT_ID
   ```

3. **Настройте Cloud SQL**
   ```bash
   # Создайте Cloud SQL instance
   gcloud sql instances create lashes-db \
     --database-version=POSTGRES_13 \
     --tier=db-f1-micro \
     --region=us-central1
   
   # Создайте базу данных
   gcloud sql databases create lashes --instance=lashes-db
   ```

4. **Обновите app.yaml**
   ```yaml
   env_variables:
     SPRING_DATASOURCE_URL: "jdbc:postgresql://google/lashes?cloudSqlInstance=YOUR_PROJECT_ID:us-central1:lashes-db&socketFactory=com.google.cloud.sql.postgres.SocketFactory"
     SPRING_DATASOURCE_USERNAME: "postgres"
     SPRING_DATASOURCE_PASSWORD: "your_password"
   ```

5. **Разверните приложение**
   ```bash
   gcloud app deploy
   ```

### Вариант 2: Google Cloud Run

1. **Настройте Cloud Build**
   ```bash
   gcloud builds submit --config cloudbuild.yaml
   ```

2. **Или разверните вручную**
   ```bash
   # Сборка образа
   docker build -t gcr.io/YOUR_PROJECT_ID/lashes-app .
   
   # Загрузка в Container Registry
   docker push gcr.io/YOUR_PROJECT_ID/lashes-app
   
   # Развертывание на Cloud Run
   gcloud run deploy lashes-app \
     --image gcr.io/YOUR_PROJECT_ID/lashes-app \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated \
     --port 8080 \
     --set-env-vars SPRING_PROFILES_ACTIVE=production,JWT_SECRET=your_jwt_secret
   ```

## 🔧 Конфигурация

### Переменные окружения

| Переменная | Описание | По умолчанию |
|------------|----------|--------------|
| `SPRING_DATASOURCE_URL` | URL базы данных | `jdbc:postgresql://localhost:5432/lashes` |
| `SPRING_DATASOURCE_USERNAME` | Имя пользователя БД | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Пароль БД | `password` |
| `JWT_SECRET` | Секретный ключ для JWT | `mySecretKeyForJWTTokenGeneration123456789` |
| `PORT` | Порт приложения | `8080` |

### Настройка JWT

Для продакшена обязательно измените `JWT_SECRET` на безопасный ключ:

```bash
export JWT_SECRET=$(openssl rand -base64 32)
```

## 📱 API Endpoints

### Аутентификация
- `POST /api/auth/register` - Регистрация
- `POST /api/auth/login` - Вход
- `POST /api/auth/logout` - Выход
- `GET /api/auth/me` - Текущий пользователь

### Записи (требует аутентификации)
- `POST /api/appointments` - Создать запись
- `GET /api/appointments/my` - Мои записи
- `GET /api/appointments/taken-times` - Занятые времена

### Админ (требует роль ADMIN)
- `GET /api/admin/appointments` - Все записи
- `GET /api/admin/appointments/date/{date}` - Записи по дате
- `PUT /api/admin/appointments/{id}/status` - Изменить статус
- `DELETE /api/admin/appointments/{id}` - Удалить запись
- `GET /api/admin/appointments/statistics` - Статистика

## 🎨 Дизайн

Приложение использует современный дизайн с:
- Градиентными фонами
- Плавными анимациями
- Адаптивной версткой
- Неоновыми акцентами
- Удобным интерфейсом для мобильных устройств

## 🔒 Безопасность

- JWT токены для аутентификации
- Хеширование паролей с BCrypt
- Защищенные маршруты
- CORS настройки
- Валидация входных данных

## 📝 Лицензия

Этот проект создан для демонстрационных целей.

## 🤝 Поддержка

Если у вас есть вопросы или предложения, создайте issue в репозитории.

---

**Создано с ❤️ для Lashes Studio**
