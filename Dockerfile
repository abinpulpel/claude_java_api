FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean test-compile -DskipTests

FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /workspace
COPY --from=build /root/.m2 /root/.m2
COPY --from=build /workspace /workspace

ENV ENV=qa

ENTRYPOINT ["sh", "-c", "mvn test -Denv=$ENV"]
