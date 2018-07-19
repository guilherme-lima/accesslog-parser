# PARSER
Spring Boot project to read a log file, save in database and process the information with the parameters passed through command line.
### Prerequisites
You are going to need Docker, Maven and JDK 8.
### Getting Started

###### Create your MySQL container
```
docker run -it --name mysql --net=host -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=parser -p 3306:3306 -d mysql
```
###### Pack JAR application
```
mvn clean package -U
```
#### Run it!
```
java -jar target/parser.jar java -jar target/parser.jar --starDate=2017
```
PS: Optionally, you can pass --accesslog=/path/of/file too.

#### Observations
I decided to read and save one line at a time in case we use a very large log file. It will take longer to execute and will save memory, but it is possible to read the whole file at once, show the IPs to be blocked and save in database later. It will be faster, but will use more memory.

Alternatively, Spring Batch could be used to do it, but I decided not to use a external framework this time to have an easier to read and smaller code.

# SQL Queries
1:
```
SELECT
    @startDate := '2017-01-01 15:00:00.000',
    @endDate := '2017-01-01 15:59:59.999',
    @threshold := 25;

SELECT
    ip
FROM parser.AccessLog 
WHERE date BETWEEN @startDate AND @endDate
GROUP BY ip HAVING COUNT(ip) > @threshold;
```

2:
``` 
SELECT @ip := '192.168.11.231';

SELECT
    request
FROM parser.AccessLog 
WHERE ip = @ip;
```