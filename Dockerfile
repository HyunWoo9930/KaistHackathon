# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Install Python and pip
RUN apt-get update && apt-get install -y python3 python3-venv python3-pip

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY build/libs/Factorial-0.0.1-SNAPSHOT.jar .

# Copy Python script and setup virtual environment
COPY src/main/resources/news_crawling2.py /app/
RUN python3 -m venv /app/venv
RUN /app/venv/bin/pip install requests beautifulsoup4 lxml pandas tqdm

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "Factorial-0.0.1-SNAPSHOT.jar"]
