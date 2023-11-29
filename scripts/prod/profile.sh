#!/usr/bin/env bash

function find_idle_profile()
{
    RESPONSE_CODE=$(sudo curl -s -o /dev/null -w "%{http_code}" https://onedayhero.kro.kr/profile)

    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=prod-blue
    else
        CURRENT_PROFILE=$(sudo curl -s https://onedayhero.kro.kr/profile)
    fi

    if [ ${CURRENT_PROFILE} == prod-blue ]
    then
      IDLE_PROFILE=prod-green
    else
      IDLE_PROFILE=prod-blue
    fi

    echo "${IDLE_PROFILE}"
}
# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == prod-blue ]
    then
      echo "8083"
    else
      echo "8084"
    fi
}