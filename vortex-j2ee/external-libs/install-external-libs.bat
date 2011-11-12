@ECHO off
ECHO This libs couldn't be found in Maven repos.
ECHO To install them go to the local dir and run this shell from the command line
ECHO on

CALL mvn install:install-file -DgroupId=org.kubek2k.mockito.spring -DartifactId=springockito -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar -Dfile=springockito-1.0.0-SNAPSHOT.jar -DpomFile=springockito-1.0.0-SNAPSHOT.pom

