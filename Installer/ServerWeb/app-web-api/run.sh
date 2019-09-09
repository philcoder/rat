#!/bin/bash

#sent message to log
echo "clone project"

git clone https://github.com/philcoder/rat.git /home

sed -i s/localhost/mysql-db/g /home/machine-manager/src/main/resources/application.yml

cd /home/machine-manager
mvn package -Dmaven.test.skip=true > /var/log/watchdog.log

(java -jar /home/machine-manager/target/machine-manager-1.0.0-SNAPSHOT.jar >> /var/log/watchdog.log)&