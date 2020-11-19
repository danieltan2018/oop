DEPLOYMENT GUIDE

Configuration file is located at /src/main/resources/application.properties
The following variables can be set:
- PORTNET API Key
- Time interval to fetch API for 7 days in advance
- Time interval to fetch API for current day's schedule
- Database URL and credentials
- Email addresses allowed to register for an account
- Email to send change notifications / password reset from
- Email SMTP server credentials

For all deployments:
Ensure there is an SMTP provider to send emails from.
Change the host, port, username and password accordingly
- Example for Gmail:
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=abc@gmail.com
    spring.mail.password=abcd1234

For online deployment:
Start a server with Docker and Docker-Compose installed.
Run the command: docker-compose up
Backend, frontend and database will be automatically deployed.
The application will be available at the server's public IP address.

For local deployment:
- Database:
    Ensure a MySQL database server is running. Set the URL and credentials in the configuration file.
    Run the deploy.sql script in this folder on the MySQL server.
- Backend:
    Ensure Java JDK 11 is installed.
    Navigate to the main folder and run the command: mvnw spring-boot:run
- Frontend:
    Navigate to frontend/index.html OR copy frontend folder to a local http server.