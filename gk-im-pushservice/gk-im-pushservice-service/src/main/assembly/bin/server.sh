#!/bin/bash
cd `dirname $0`
if [ "$1" = "start" ]; then
	ENV=$2
	if [ -z "$ENV" ]; then
		echo "ERROR:PLEASE SPEC ENV ARGS,SUCH AS dev,test,beta,release..  "
		echo "app exit"
		exit 1
	fi
	./start.sh $ENV
else
	if [ "$1" = "stop" ]; then
		./stop.sh
	else
		if [ "$1" = "debug" ]; then
			ENV=$2
			if [ -z "$ENV" ]; then
				echo "ERROR:PLEASE SPEC ENV ARGS,SUCH AS dev,test,beta,release..  "
				echo "app exit"
				exit 1
			fi
			./start.sh $ENV debug
		else
			if [ "$1" = "restart" ]; then
				ENV=$2
				if [ -z "$ENV" ]; then
					echo "ERROR:PLEASE SPEC ENV ARGS,SUCH AS dev,test,beta,release..  "
					echo "app exit"
					exit 1
				fi
				./restart.sh $ENV
			else
				if [ "$1" = "dump" ]; then
					./dump.sh
				else
					echo "ERROR: Please input argument: start or stop or debug or restart or dump"
				    exit 1
				fi
			fi
		fi
	fi
fi
