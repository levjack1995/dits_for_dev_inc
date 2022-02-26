# dits_for_dev_inc
DITS is a dev incbator testing system. It is a system for testing student , in this application we habe 2 roles: user and admin.
User can pass tests and see its statistics.
Admin can CRUD users, tests and see statistics by topics, tests, questions , answers and users.
For authorizing and authentication it uses Spring Security. 

Getting Started
=====================
##### 1. git clone https://github.com/levjack1995/dits_for_dev_inc.git
##### 2. start your application from your Idea
##### 3. open http://localhost:8080/

* Credentials for user : user - user.
* Credentials for admin : admin - admin

Using technologies
=====================
##### 1. Java 11
##### 2. PostgreSQL
##### 3. Thymeleaf
##### 4. Spring
##### 5. Hibernate
##### 6. H2-database for testing
##### 7. Bootstrap

Files path
=====================
This path - templates - uses by default in Spring boot application (It is classpath for spring boot)
1. src/main/resources/static - you can find static resources such as : images , css , js 
2. src/main/resources/templates - you can find html pages 
3. src/main/resources - you can find application configurations
4. src/main/java - it is a path for main logic of your application
5. src/main/test - it is a path, where maven will start unit testing
