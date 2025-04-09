# 프로젝트 자바버전에 맞는 openjdk 이미지
FROM eclipse-temurin:21-jdk

# jar 이름 변경
COPY build/libs/server-0.0.1-SNAPSHOT.jar tico.jar

# 컨테이너 내에서 애플리케이션이 8080포트를 사용할 것이다
EXPOSE 8080

# timezone을 Asia/Seoul로 설정하여 jara를 실행, jar파일 실행
ENTRYPOINT exec java -jar -Duser.timezone=Asia/Seoul tico.jar