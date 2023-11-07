#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)
REPOSITORY=/home/ec2-user/onedayhero/
LOCATION=/yml/application.yml
NGINX_NAME=nginx

echo "> Build 파일 복사"
echo "> cp $REPOSITORY*.jar $REPOSITORY"

cp -f $REPOSITORY*.jar $REPOSITORY      # 새로운 jar file 계속 덮어쓰기

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

if [ "$(docker ps -q -f name=$NGINX_NAME)" ]; then
  echo "Docker 컨테이너 '$NGINX_NAME'가 이미 실행 중입니다."
else
  # Docker 컨테이너 실행
  echo "> docker run -it --name nginx -d -v /etc/nginx/:/etc/nginx/ -p 80:80 nginx"
  # sudo docker run -it --name nginx -d -v /etc/nginx/:/etc/nginx/ -p 80:80 nginx
  echo "> docker start nginx"
  sudo docker start nginx
  echo "Docker 컨테이너 '$NGINX_NAME'가 실행되었습니다."
  sleep 3
fi


cd $REPOSITORY

files=$(ls)
echo "$files"

# docker 이미지를 연결하고 환경변수 전달
echo "> docker build -t spring ./"
sudo docker build -t spring ./
echo "> docker run -it --name ${IDLE_PROFILE} -d -e active=${IDLE_PROFILE} -e location=${LOCATION} -p ${IDLE_PORT}:${IDLE_PORT} spring"
sudo docker run -it --name "$IDLE_PROFILE" -d -e active="$IDLE_PROFILE" -e location=$LOCATION -v /home/ec2-user/yml/application.yml:/yml/application.yml -p "$IDLE_PORT":"$IDLE_PORT" spring