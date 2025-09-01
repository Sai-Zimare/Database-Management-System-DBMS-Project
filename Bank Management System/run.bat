@echo off
echo Compiling all Java files...
javac -cp ".;D:\DBMS_Project\mysql-connector-j-9.3.0\mysql-connector-j-9.3.0\mysql-connector-j-9.3.0.jar" *.java
echo Compilation complete!

echo Running MainApplication...
java -cp ".;D:\DBMS_Project\mysql-connector-j-9.3.0\mysql-connector-j-9.3.0\mysql-connector-j-9.3.0.jar" MainApplication

pause
