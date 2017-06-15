Kiel Corpus to TEI converter (kc2tei)
=====================================

A program to convert Kiel Corpus annotation files into TEI files based on ISO 24624:2016(E)


Some commands used while testing/developing
-------------------------------------------

Cleaning all built files
    $ mvn clean

Run tests defined in ./src/test/
    $ mvn test

Build executable JAR-file
    $ mvn package

Run built program
    $ java -jar target/kc2tei-0.1.jar -i src/test/resources/sample_kc_file.s1h

