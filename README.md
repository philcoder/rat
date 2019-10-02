Remote Administration Tool (RAT)
====================================================================

This guide will provide you all the steps to run Remote Administration Tool (RAT) web application (project called machine-manager) and install RAT client on Windows (project called MachineWatcher).


### System Requeriments:

1. Windows 10 64bits for execute RAT clients and environment development for Spring Boot and .NET Framework
2. Components docker and docker-compose already installed on server or deploy machine web project
3. HDD +2 GB available on server or deploy for build docker images

### Technologies used:

1. Web side: Spring Boot v2.1.7 components (web, data-jpa, quartz, thymeleaf, junit) and Java SDK 12 64bits
2. Database: MySQL v8.0.13
3. RAT client: Batch script, .NET Framework v4.7.2 and SelfHost Library
4. IDEs: Visual Studio Community 2019 (VS2019), Spring Tool Suite (STS) and Postman (REST Troubleshooting)
5. OS: Windows 10 64 bits for deploy in clients and develop env. 
6. Docker image using Ubuntu 18.04 LTS and others dependencies for build and deploy web project.


### How to Execute projects for Developer

Web side: Open STS and import project maven select folder (machine-manager) inside **'rat'** directory

RAT client: Open VS2019 and import MachineWatcher folder (MachineWatcher) inside **'rat'** directory


### How to Execute deploy and execute web project (machine-manager)

You can to execute using STS run after import project to IDE or clone this project to server or another computer as you wish. On console from **'rat'** directory navigate to **'Installer\ServerWeb'** and execute follow commands:

For startup all services
```
docker-compose -f docker-compose.yml up -d --build
```

For stop all services
```
docker-compose -f docker-compose.yml down
```

For stop and cleanup volumes 
```
docker-compose -f docker-compose.yml down --volumes
```

Access web service: http://localhost:15000/manager/web/home

**PS: Logs from maven build and spring boot startup, enter inside 'app-web-api' docker instance and execute: tail -f /var/log/watchdog.log**

### How to Deploy RAT client (MachineWatcher) for monitoring Windows Machine

After project clone from git, you can zipped **'ClientWin'** folder located inside **'rat\Installer'**. Bring and extracted this folder to Windows 10 client that you want to enable monitoring.

Before you install the windows service you can configure client through the **config.ini** file.
```
#web server address and port 
manager.address=192.168.200.100
manager.port=15000

#machine watcher config
machine.watcher.listen.port=20000
```
**PS: The user need to know the web server address (manager.address) and the port (manager.port) after machine-manager is deployed and online.**

For install and running process on client machine, execute as administrator level the **install.bat**, some infos will appear but nothing to do and terminal window will be close alone.

For uninstall RAT client, go to inside **'Client Win'** folder and run as administrator level, the **uninstall.bat**, some info will appear but nothing to do and terminal window will be close alone.

PS: This client was tested only on Windows 10 64 bits and the executable was compiled only to 64 bits version.

### Future Works

1. Replace simple front-end to a SPA framework (e.g.: Vue.js)
2. Migrate REST Api from Spring Boot to Eclipse MicroProfile (using Fault Tolerance, Health Check, JWT Auth e etc.)
3. Create full micro service projects from "monolithic architecture"
