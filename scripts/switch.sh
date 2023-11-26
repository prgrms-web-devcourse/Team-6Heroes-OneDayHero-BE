#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    timestamp=$(date "+%Y/%m/%d %H:%M")
    echo "$timestamp"

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://43.200.91.152:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> docker exec -d nginx nginx -s reload"
    echo "> application server upload 대기"
    sleep 30
    sudo docker exec -d nginx nginx -s reload
}