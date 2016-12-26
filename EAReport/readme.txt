- using -Djava.library.path option, maven was corrupting bin file. Fixed that, but -D seems better
- using maven fix for .odt files (was corrupting)
- sometimes getting aspect errors, like weaving not working. sometimes works fine though. Usually works eventually after doing 
mvn clean install and run applicatin a couple of times.
- using maps as models to bridge EA Api.
- when executing the jar, include the EA .dll in JVM library path with -D option
- since EA is 32-bit it cannot be loaded in a 64-bit process, therefore use 32 bit JVM
- example command: "C:\Program Files (x86)\Java\jdk1.8.0_77\bin\java.exe" -Djava.library.path="C:\Program Files (x86)\Sparx Systems\EA\Java API" -jar EnterpriseArchitectJOD-0.0.1-SNAPSHOT-jar-with-dependencies.jar
- must specify a properties file (eajod.properties by default) 
- changed repo name again