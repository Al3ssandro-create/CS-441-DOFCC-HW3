# Use an official Scala runtime as a parent image
FROM hseeberger/scala-sbt:8u222_1.3.5_2.13.1 as build

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the project
RUN sbt clean compile

# Run the project
CMD ["sbt", "run"]
