FROM gcr.io/google-appengine/openjdk
RUN wget -O /rcloadenv https://storage.googleapis.com/rcloadenv/v0.1.0/rcloadenv0.1.0.linux-amd64 && chmod +x /rcloadenv
COPY app.jar app.jar
ENTRYPOINT [ "sh", "-c", "/rcloadenv $RCLOADENV_CONFIG -- /docker-entrypoint.bash java -jar /app.jar" ]
CMD []
