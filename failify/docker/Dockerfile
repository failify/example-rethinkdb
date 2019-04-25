FROM debian:jessie

RUN apt update && apt install -y libc6 libcurl3 libgcc1 libprotobuf9 libssl1.0.0 libstdc++6 libtinfo5 zlib1g
# Failify dependencies
RUN apt install -y iptables iproute2