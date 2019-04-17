FROM debian:jessie

RUN apt update && apt install -y wget gnupg
RUN echo "deb http://download.rethinkdb.com/apt jessie main" | tee /etc/apt/sources.list.d/rethinkdb.list
RUN wget -qO- https://download.rethinkdb.com/apt/pubkey.gpg | apt-key add -
RUN apt update && apt install -y rethinkdb iptables iproute2