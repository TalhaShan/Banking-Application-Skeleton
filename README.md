# Banking-Application-Skeleton
Banking basic features like deposit,withdraw and transfer <br>
Java version : 17 <br>
Database: mysql <br>
Springboot version:3.0.6 <br>
Port: 8080
# Running locally
git clone https://github.com/TalhaShan/Banking-Application-Skeleton.git <br>
mvn clean install <br>
Open in Intellij Idea <br>
Configure database credentials in application.properties file

# Running in Intellij Idea <br>
If maven doesn't recognized do :<br>
settings-> build tools -> maven home path ->select maven bundle <br>
File Invalidate Cache restart


# OR Running locally
mvn clean install <br>
java -jar target/BankingApplication-*.jar <br>
setup db credentials in application.properties replace username and password <br>

# Running on Docker
docker build -t banking-application <br>
docker build -t banking-image <br>
docker-compose up 
# OR
docker run -p 8080:8080 banking-application <br>
# Testing
Use Banking.postman_collection.json in resources <br>
Refer screenshots in root directory in case of any help for testing and database structure





