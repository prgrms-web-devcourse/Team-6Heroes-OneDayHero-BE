#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PROFILE=$(find_idle_profile)

CONTAINER_ID=$(docker container ls -f "name=${IDLE_PROFILE}" -q)

echo "> 컨테이너 ID는 무엇?? ${CONTAINER_ID}"
echo "> 현재 프로필은 무엇?? ${IDLE_PROFILE}"

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 ${IDLE_PROFILE} 을 제거합니다."
  echo "> docker rm ${IDLE_PROFILE}"
  sudo docker rm ${IDLE_PROFILE}
  sleep 5
else
  echo "> docker stop ${IDLE_PROFILE}"
  sudo docker stop ${IDLE_PROFILE}
  echo "> docker rm ${IDLE_PROFILE}"
  sudo docker rm ${IDLE_PROFILE}
  sleep 5
fi