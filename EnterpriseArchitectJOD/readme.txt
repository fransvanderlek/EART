- using -Djava.library.path option, maven was corrupting bin file. Fixed that, but -D seems better
- using maven fix for .odt files (was corrupting)
- sometimes getting aspect errors, like weaving not working. sometimes works fine though. Usually works eventually after doing 
mvn clean install and run applicatin a couple of times.
- using maps as models to bridge EA Api.
 