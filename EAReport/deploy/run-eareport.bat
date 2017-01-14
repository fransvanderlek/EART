SET JAVA_32BIT_HOME=%JAVA_HOME%
SET EA_JAVAAPI_PATH="C:\Program Files (x86)\Sparx Systems\EA\Java API"

"%JAVA_32BIT_HOME%\bin\java.exe" -Djava.library.path="%EA_JAVAAPI_PATH%" -javaagent:agents/aspectjweaver.jar -classpath "EAReport-0.0.1-SNAPSHOT-jar-with-dependencies.jar;%EA_JAVAAPI_PATH%\eaapi.jar" org.iisiplusone.eareport.main_runner.EaDocShell

PAUSE