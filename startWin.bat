@echo off

echo starting,wait...
echo path:C:\Users\Administrator\git\star-fsccserver

set base=C:\Users\Administrator\git\star-fsccserver
set class=%base%\bin
set libs=%base%\lib
set classespath=%class%;%libs%\aopalliance-1.0.jar;%libs%\c3p0-0.9.1.1.jar;%libs%\commons-dbcp2-2.1.jar;%libs%\commons-dbutils-1.6.jar;%libs%\commons-logging-1.2.jar;%libs%\commons-pool2-2.3.jar;%libs%\gson-2.6.2.jar;%libs%\log4j-api-2.5.jar;%libs%\log4j-core-2.5.jar;%libs%\log4j-slf4j-impl-2.5.jar;%libs%\mysql-connector-java-5.1.35.jar;%libs%\quartz-2.2.1.jar;%libs%\slf4j-api-1.7.12.jar;%libs%\spring-aop-4.2.3.RELEASE.jar;%libs%\spring-beans-4.2.3.RELEASE.jar;%libs%\spring-context-4.2.3.RELEASE.jar;%libs%\spring-core-4.2.3.RELEASE.jar;%libs%\spring-expression-4.2.3.RELEASE.jar

java -classpath %classespath% com.starunion.java.fsccserver.beginning.FsCcServer

@pause