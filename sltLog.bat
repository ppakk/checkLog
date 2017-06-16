set PATH=C:\j2sdk1.4.2_14\bin;%PATH%
set JAVA_HOME=C:\j2sdk1.4.2_14\
set CLASSPATH=C:\SVN\Common\ThirdParty\lib\javax\j2ee.jar;C:\SVN\Common\ThirdParty\lib\db2\db2java.zip;C:\SVN\Common\ThirdParty\lib\db2\db2jcc.jar;C:\SVN\Common\ThirdParty\lib\db2\db2jcc_license_cu.jar;.
echo %CLASSPATH%
javac sltLog.java
java sltLog com.ibm.db2.jcc.DB2Driver
