FROM alpine:latest as build

#ADD https://download.java.net/java/early_access/panama/70/openjdk-13-foreign+70_linux-x64_bin.tar.gz /opt/jdk/
#RUN tar -xzvf /opt/jdk/openjdk-13-foreign+70_linux-x64_bin.tar.gz -C /opt/jdk/
#
#RUN ["/opt/jdk/jdk-13/bin/jlink", "--compress=2", \
#     "--module-path", "/opt/jdk/jdk-13/jmods/", \
#     "--add-modules", "java.base", \
#     "--add-modules", "jdk.unsupported", \
#     "--output", "/jlinked"]
#
#FROM alpine:latest
#COPY --from=build /jlinked /opt/jdk/
#CMD ["/opt/jdk/bin/java", "--version"]
