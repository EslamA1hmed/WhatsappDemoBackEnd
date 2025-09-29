# المرحلة الأولى: بناء المشروع باستخدام Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# انسخ pom.xml ونزّل الـ dependencies الأول (يساعد في cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# انسخ باقي المشروع واعمل build
COPY src ./src
RUN mvn clean package -DskipTests

# المرحلة الثانية: تشغيل المشروع
FROM eclipse-temurin:17-jdk
WORKDIR /app

# انسخ الـ jar من مرحلة الـ build
COPY --from=build /app/target/*.jar app.jar

# شغل التطبيق
ENTRYPOINT ["java","-jar","app.jar"]

# Render بيشتغل على PORT من env
EXPOSE 8080
