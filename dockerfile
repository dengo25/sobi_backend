# #  1단계: 빌드 스테이지
# FROM openjdk:17-jdk-slim AS build
#
# # 필요한 도구 설치 (zip 등)
# RUN apt-get update && apt-get install -y zip unzip
#
# # 작업 디렉토리 생성
# WORKDIR /app
#
# # Gradle Wrapper 복사 및 캐시 최적화
# COPY gradle /app/gradle
# COPY gradlew build.gradle settings.gradle /app/
# RUN chmod +x gradlew
#
#
# # 전체 프로젝트 복사
# COPY . /app
#
# # 테스트 생략하고 빌드
# # 빌드 실행 (QueryDSL 포함, APT 적용됨)
# RUN ./gradlew clean build -x test
#
#
# # 2단계: 실행 스테이지
# FROM openjdk:17-jdk-slim
#
# # 시간대 설정
# ENV TZ=Asia/Seoul
#
# # 작업 디렉토리
# WORKDIR /app
#
# # JAR 복사
# COPY --from=build /app/build/libs/*.jar app.jar
#
# # 포트 오픈 (필요시 8080)
# EXPOSE 8080
#
# # 실행 명령
# ENTRYPOINT ["java", "-jar", "app.jar"]


# 1단계: 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 2단계: 실행
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]