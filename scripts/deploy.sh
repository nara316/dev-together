#!/bin/bash
 CURRENT_PID=$(pgrep -f .jar)
 echo "$CURRENT_PID"
 if [ -z $CURRENT_PID ]; then
         echo "no process"
 else
         echo "kill $CURRENT_PID"
         kill -9 $CURRENT_PID
         sleep 3
 fi

 JAR_PATH=$(find /home/ubuntu/dev-together/build/libs/ -name 'dev-together-0.0.1-SNAPSHOT.jar' | head -n 1)
 echo "jar path : $JAR_PATH"
 chmod +x $JAR_PATH
 nohup java -jar $JAR_PATH >> /home/ubuntu/dev-together/deploy.log 2>> /home/ubuntu/dev-together/deploy_err.log &
 echo "jar faild deploy success"