@ECHO OFF
SET JAR=lib/kc2tei.jar
SET JAVA=C:\ProgramData\Oracle\Java\javapath\java.exe

IF NOT EXIST %JAVA% (
  ECHO "Java not found!"
  EXIT /b 1
)

IF NOT EXIST %JAR% (
  ECHO "Java archive %JAR% not found!"
  EXIT /b 1
)
  
%JAVA% -jar "%JAR%" %*

