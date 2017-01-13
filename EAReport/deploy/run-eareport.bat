SET JAVA_32BIT_HOME=%JAVA_HOME%
SET EA_JAVAAPI_PATH="C:\Program Files (x86)\Sparx Systems\EA\Java API"

%JAVA_32BIT_HOME%\bin\java.exe -Djava.library.path=%EA_JAVAAPI_PATH% -javaagent:agents/aspectjweaver.jar -jar EAReport-0.0.1-SNAPSHOT-jar-with-dependencies.jar -classpath %EA_JAVAAPI_PATH%

PAUSE