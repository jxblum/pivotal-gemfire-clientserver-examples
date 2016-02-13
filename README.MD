## Pivotal GemFire Client/Server Example Applications

The *Pivotal GemFire Client/Server Example Applications* project contains 4 individual modules with examples
demonstrating the configuration of *Pivotal GemFire* client and server cache applications using both *GemFire*
native [cache.xml](http://gemfire.docs.pivotal.io/docs-gemfire/latest/reference/topics/chapter_overview_cache_xml.html)
and *GemFire's* [public Java API](http://data-docs-samples.cfapps.io/docs-gemfire/latest/javadocs/japi/index.html)
along with *Spring* [Java-based Configuration](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java)
and *Spring (Data GemFire)* [XML Namespace](http://docs.spring.io/spring-data-gemfire/docs/current/reference/html/#_spring_data_gemfire_core_schema_gfe)
configuration meta-data.

The examples applications are broken down as follows:

Example Application | Description
------------------- | -----------
[native-gemfire-clientserver-xml](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/native-gemfire-clientserver-xml) | Configures GemFire client/server using native cache.xml; Tests square root (x^1/2) operations.
[native-gemfire-clientserver](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/native-gemfire-clientserver) | Configures GemFire client/server using the Java API; Tests cubed (x^3) operations.
[spring-gemfire-clientserver-xml](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/spring-gemfire-clientserver-xml) | Configures GemFire client/server using the Spring (Data GemFire) XML namespace; Test square root (x^1/2) operations.
[spring-gemfire-clientserver](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/spring-gemfire-clientserver) | Configures GemFire client/server using Spring JavaConfig; Tests squared (x^2) operations.

You can drill into each module for further details on how to build and run each example application. 

### Requirements

* Java Development Kit (JDK) 1.8.0+ (e.g. 1.8.0_72).
* Apache Maven 3.3.x

### Dependencies

Currently, the project is using *Spring Data GemFire* **1.6.2.RELEASE** and *Pivotal GemFire* **8.1.0** in order to
resolve a customer issue.

*
You can find download distributions of the *JDK/JRE* [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 
and *Apache Maven* at [maven.apache.org](http://maven.apache.org/download.cgi).
 
### Build

To build the *pivotal-gemfire-clientserver-examples* project, run the following at the command-line

```
$ mvn clean install -DskipTests
```

You need to skip tests since the project tests are actually **integration** tests.  This means they need 
a GemFire (Cache) Server running in order to run successfully.  
