# Multithreaded Adding Machine

### Deploy instructions

    # mvn -v
    # Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T18:41:47+02:00)
    # Maven home: /usr/local/Cellar/maven/3.3.9/libexec
    # Java version: 11.0.3, vendor: AdoptOpenJDK
    # Java home: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
    # Default locale: en_EE, platform encoding: UTF-8
    # OS name: "mac os x", version: "10.14.3", arch: "x86_64", family: "mac"
    
    # java -version
    # openjdk version "11.0.3" 2019-04-16
    # OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.3+7)
    # OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.3+7, mixed mode)
      
    mvn install # to create the fat jar
    java -jar target/adding-machine-1-jar-with-dependencies.jar # to run the fat jar anywhere 

If creating the fat jar with mvn install fails, run the prebuilt executable jar with

    java -jar prebuilt.jar

### Instructions for running the tests

    mvn test
    # -------------------------------------------------------
    #  T E S T S
    # -------------------------------------------------------
    # Running com.github.priidukull.AddingMachineTest
    # Configuring TestNG with: org.apache.maven.surefire.testng.conf.TestNG652Configurator@2038ae61
    # 2019-07-01 00:56:07.591:INFO::main: Logging initialized @996ms to org.eclipse.jetty.util.log.StdErrLog
    # 2019-07-01 00:56:07.738:INFO:oejs.Server:Thread-0: jetty-9.4.19.v20190610; built: 2019-06-10T16:30:51.723Z; git: afcf563148970e98786327af5e07c261fda175d3; jvm 11.0.3+7
    # 2019-07-01 00:56:07.868:INFO:oejs.AbstractConnector:Thread-0: Started ServerConnector@5c642e6a{HTTP/1.1,[http/1.1]}{0.0.0.0:1337}
    # 2019-07-01 00:56:07.869:INFO:oejs.Server:Thread-0: Started @1274ms
    # Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.531 sec
    # 
    # Results :
    # 
    # Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
    # 

        
