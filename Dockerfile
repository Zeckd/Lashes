# Используем Java 17
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN ./mvnw dependency:go-offline || true

# Копируем остальной проект
COPY . .

# Собираем проект
RUN ./mvnw clean package -DskipTests

# Запускаем JAR
CMD ["java", "-jar", "target/lashes-0.0.1-SNAPSHOT.jar"]
