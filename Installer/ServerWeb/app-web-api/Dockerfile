  
FROM ubuntu:bionic

LABEL maintainer="Philipp B. Costa"

#support lib and app
RUN apt-get update && apt-get install -y --no-install-recommends \
		ca-certificates \
		libexpat1 \
		libffi6 \
		libsqlite3-0 \
		libssl1.1 \
		psmisc \
		nano \
		git \
		procps \
		apt-utils \
		software-properties-common \
		gnupg2 \
		dirmngr \
	&& rm -rf /var/lib/apt/lists/*

#install java
RUN add-apt-repository ppa:linuxuprising/java
RUN echo oracle-java12-installer shared/accepted-oracle-license-v1-2 select true | /usr/bin/debconf-set-selections
RUN apt-get update && apt-get install -y oracle-java12-installer

RUN apt-get update && apt-get install -y maven

#transfer git clone, deploy and start script to inside docker
COPY ./run.sh /tmp

RUN touch /var/log/watchdog.log && chmod 777 /var/log/watchdog.log

EXPOSE 15000 15000

#made a entrypoint for loop application
ENTRYPOINT /tmp/run.sh && tail -f /var/log/watchdog.log

