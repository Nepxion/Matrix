call mvn clean install -DskipTests
call mvn install:install-file -DgroupId=nepxion -DartifactId=matrix -Dversion=1.0.0 -Dfile=target/matrix-1.0.0.jar -Dpackaging=jar -DgeneratePom=true

pause

