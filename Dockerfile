ARG OPENJDK
FROM ${OPENJDK}
MAINTAINER Rik Brown <rik@rik.codes>
ADD dynamic-dns-updater-1.0-SNAPSHOT.tar /
ENTRYPOINT ["/dynamic-dns-updater-1.0-SNAPSHOT/bin/dynamic-dns-updater"]
