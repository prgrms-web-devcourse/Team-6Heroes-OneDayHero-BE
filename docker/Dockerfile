FROM openjdk:17

WORKDIR /home/ec2-user/onedayhero

COPY ./onedayhero-api-0.0.1-SNAPSHOT.jar .

CMD java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=${active} -Dspring.config.location=${location} onedayhero-api-0.0.1-SNAPSHOT.jar
