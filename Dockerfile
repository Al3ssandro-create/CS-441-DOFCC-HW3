# Start with a Java 8 base image
FROM openjdk:8

# Define the versions of Scala and SBT that you want to use
ENV SCALA_VERSION 2.13.12
ENV SBT_VERSION 1.9.7

# Install Scala
RUN \
  wget https://downloads.lightbend.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz && \
  tar xvf scala-$SCALA_VERSION.tgz && \
  mv scala-$SCALA_VERSION /usr/share/scala && \
  rm scala-$SCALA_VERSION.tgz && \
  ln -s /usr/share/scala/bin/* /usr/bin/

# Update the package list and install SBT
RUN \
  apt-get update && \
  apt-get install -y apt-transport-https gnupg2 curl && \
  echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee -a /etc/apt/sources.list.d/sbt.list && \
  echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee -a /etc/apt/sources.list.d/sbt_old.list && \
  curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
  apt-get update && \
  apt-get install sbt=$SBT_VERSION

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the project
RUN sbt clean compile

# Run the project
CMD ["sbt", "run"]
