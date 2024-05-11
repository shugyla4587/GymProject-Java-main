# Используем базовый образ OpenJDK с поддержкой JAR
FROM openjdk:17

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR файл вашего Spring Boot приложения
COPY target/*.jar app.jar

# Определяем команду запуска JAR файла
CMD ["java", "-jar", "app.jar"]
