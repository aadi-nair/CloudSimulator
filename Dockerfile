FROM openjdk:17-jdk-slim

# copy jar file (run 'sbt assembly' before building the docker image)
ADD target/scala-3.1.3/CloudSimulator-assembly-0.1.0-SNAPSHOT.jar simulations.jar

# copy network topologies
ADD src/main/resources/ src/main/resources/

# run the simulations
ENTRYPOINT ["java","-jar","/simulations.jar"]
