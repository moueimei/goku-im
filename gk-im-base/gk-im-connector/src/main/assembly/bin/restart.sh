#!/bin/bash
ENV=$1
if [ -z "$ENV" ]; then
    echo "ERROR:PLEASE SPEC ENV ARGS,SUCH AS dev,test,beta,release..  "
    echo "app exit"
    exit 1
fi
cd `dirname $0`
./stop.sh
./start.sh $ENV $2