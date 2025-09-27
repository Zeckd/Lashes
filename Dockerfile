# Этап сборки
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Скопировать pom.xml и подкачать зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Скопировать весь проект (а не только src!)
COPY . .

# Собрать jar
RUN mvn clean package -DskipTests

# Этап запуска
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Скопировать jar из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Создать пользователя
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
