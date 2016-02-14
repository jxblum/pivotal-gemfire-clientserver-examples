## Pivotal GemFire Client/Server Example Applications

The *Pivotal GemFire Client/Server Example Applications* project contains 4 individual modules with examples
demonstrating the configuration of *Pivotal GemFire* client and server cache applications using both *GemFire*
native [cache.xml](http://gemfire.docs.pivotal.io/docs-gemfire/latest/reference/topics/chapter_overview_cache_xml.html)
and *GemFire's* [public Java API](http://data-docs-samples.cfapps.io/docs-gemfire/latest/javadocs/japi/index.html)
along with *Spring* [Java-based Container Configuration](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java)
and *Spring (Data GemFire)* [XML Namespace](http://docs.spring.io/spring-data-gemfire/docs/current/reference/html/#_spring_data_gemfire_core_schema_gfe)
configuration meta-data.

The module applications are broken down into the following examples:

Example Application | Description
------------------- | -----------
[native-gemfire-clientserver-xml](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/native-gemfire-clientserver-xml) | Configures GemFire client/server using native cache.xml; Tests client/server connectivity and square root (x^1/2) operations.
[native-gemfire-clientserver](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/native-gemfire-clientserver) | Configures GemFire client/server using the Java API; Tests client/server connectivity and cubed (x^3) operations.
[spring-gemfire-clientserver-xml](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/spring-gemfire-clientserver-xml) | Configures GemFire client/server using the Spring (Data GemFire) XML namespace; Tests client/server connectivity and square root (x^1/2) operations.
[spring-gemfire-clientserver](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/spring-gemfire-clientserver) | Configures GemFire client/server using Spring Java-based Container Configuration; Tests client/server connectivity and squared (x^2) operations.

Drill into the individual modules for further details on how to build and run each example application. 

### Requirements

* *Java Development Kit* (JDK) 1.8.0+ (e.g. 1.8.0_72).
* *Apache Maven* 3.3.x

### Dependencies

Currently, the project is using *Spring Data GemFire* **1.6.2.RELEASE** and *Pivotal GemFire* **8.1.0** in order to
resolve a customer issue.

You can download distributions of the *JDK* [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 
and *Apache Maven* at [maven.apache.org](http://maven.apache.org/download.cgi).

### Build

To build this *pivotal-gemfire-clientserver-examples* project, run the following `mvn` command at the command-line

```
$ mvn clean install -DskipTests
```

You must skip tests when running the build since the project tests are technically **integration** tests.  This means 
they require a GemFire (Cache) Server to be running in order to execute successfully.  

### Issues

For any issues or feedback with this project, please file a [GitHub Issue](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/issues).  

### Contributions

I also welcome any contributions through [Pull Requests](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/pulls).
If you have an idea or improvement, please share.  No CLA necessary.

**Thanks**
