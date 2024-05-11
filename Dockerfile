FROM maven:3.8.6 AS build
WORKDIR /extractimagelinks
COPY pom.xml /extractimagelinks
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=build /blogapp/target/*.jar extractimagelinks.jar
EXPOSE 9090
CMD ["java","-jar","extractimagelinks.jar"]


