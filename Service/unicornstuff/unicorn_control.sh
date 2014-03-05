#!/bin/sh

case "$1" in 
	start)
  		unicorn -c unicorn.rb -D;
		;;
	stop)
  		kill `cat tmp/pids/unicorn.pid`
		;;
	*)
		echo "Usage: $0 start|stop";
esac
