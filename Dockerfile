# Etapa de construcción
FROM maven:3.9.0-openjdk-21 AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos de configuración y el código fuente
COPY pom.xml .
COPY src ./src

# Construye el JAR de la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR construido desde la etapa anterior
COPY --from=builder /app/target/my-backend.jar my-backend.jar

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "my-backend.jar"]
