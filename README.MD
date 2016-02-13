## Pivotal GemFire Client/Server Example Applications

The *Pivotal GemFire Client/Server Example Applications* project contains 4 individual modules with examples
demonstrating the configuration of *Pivotal GemFire* client and server cache applications using both *GemFire*
native [cache.xml](http://gemfire.docs.pivotal.io/docs-gemfire/latest/reference/topics/chapter_overview_cache_xml.html)
as well as *GemFire's* [public Java API](http://data-docs-samples.cfapps.io/docs-gemfire/latest/javadocs/japi/index.html)
along with *Spring* [JavaConfig](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java)
and *Spring (Data GemFire)* [XML namespace-based](http://docs.spring.io/spring-data-gemfire/docs/current/reference/html/#_spring_data_gemfire_core_schema_gfe)
configuration meta-data.

The examples applications are broken down as follows:

Example Application | Description
------------------- | -----------
[native-gemfire-clientserver-xml](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/native-gemfire-clientserver-xml) | Configures GemFire client/server using native cache.xml; Tests square root (x^1/2) operations.
[native-gemfire-clientserver](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/native-gemfire-clientserver) | Configures GemFire client/server using the Java API; Tests cubed (x^3) operations.
[spring-gemfire-clientserver-xml](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/spring-gemfire-clientserver-xml) | Configures GemFire client/server using the Spring (Data GemFire) XML namespace; Test square root (x^1/2) operations.
[spring-gemfire-clientserver](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/tree/master/spring-gemfire-clientserver) | Configures GemFire client/server using Spring JavaConfig; Tests squared (x^2) operations.

You can drill into each module for further details on how to build and run each example application. 
