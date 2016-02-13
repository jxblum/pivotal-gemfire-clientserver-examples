## Spring (Pivotal) GemFire Client/Server Example Application

The following example application demonstrates how to configure both a *GemFire* client and server cache application
using Spring [Java-based configuration](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#beans-java).

This example tests connectivity between the *Spring-configured GemFire* client and server, then runs a test case that tests
a custom GemFire [CacheLoader](http://data-docs-samples.cfapps.io/docs-gemfire/latest/javadocs/japi/com/gemstone/gemfire/cache/CacheLoader.html) 
([FactorialsCacheLoader](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver/src/main/java/io/pivotal/gemfire/main/SpringGemFireServerApplication.java#L138-L161)) 
computing the factorial of a number (the cache "key") when requested, such as...

```
Region.get(4) == 24
```

Thus, the **Factorials** *Region* will return 24 when the key "4" is requested.  The *FactorialsCacheLoader* is only invoked
if the given key (i.e. number) is not present in the cache already.

### Prerequisites

To run this example, you must first have installed the *JDK* along with *Apache Maven* in your development environment
and built the project.
 
See the [build](https://github.com/jxblum/pivotal-gemfire-clientserver-examples#build) section for further details.

### Running
 
The *GemFire Server* example application ([SpringGemFireServerApplication](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver/src/main/java/io/pivotal/gemfire/main/SpringGemFireServerApplication.java)) 
is a *Spring Boot* application.  By default, the [module Maven POM file](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver/pom.xml#L76-L82) 
is configured with the *Spring Boot* plugin.

From the command-line (assuming you are currently in the project root directory), you can run the *GemFire Server* 
with the following:

```
$ mvn -pl spring-gemfire-clientserver spring-boot:run
```

This uses all the module default configuration settings (e.g. ports) to configure the *GemFire Server* OOTB.

In addition to starting up a *GemFire Server*, the *Spring* configuration also configures the server to start 
an embedded *GemFire Locator* (default port: 11235) along with an embedded *GemFire Manager* (default port: 1199)
as well as a *Cache Server* listening for cache clients on (default) port 12480. 

For convenience, the project module provides a *Gfsh* shell script to connect to the running *GemFire Server*
in order to inspect 

The *Gfsh* shell script can be used and ran interactively (in the *GemFire Shell*) or non-interactively 
(i.e. scripted in an OS shell script file, e.g. Bash).

Of course, to use *Gfsh*, you must have a [distribution](https://network.pivotal.io/products/pivotal-gemfire) 
of *Pivotal GemFire* installed on your system.  You can also use an [Apache Geode](http://geode.incubator.apache.org/) 
installation in place of *Pivotal GemFire*.  Be sure to following the [User Guide for installation steps](http://gemfire.docs.pivotal.io/docs-gemfire/getting_started/installation/install_intro.html).

Invoke the *Gfsh* shell script with the following command in the *GemFire Shell*:

```
gfsh>run --file=/absolute/path/to/pivotal-gemfire-clientserver-examples/spring-gemfire-clientserver/bin/connect.gfsh
```

Or non-interactively, in (Bash) shell script:

```
!/bin/bash
...

gfsh -e "run --file=/absolute/path/to/pivotal-gemfire-clientserver-examples/spring-gemfire-clientserver/bin/connect.gfsh"
```

If you are running the *GemFire* Server remotely, you will need to tweak the *connect.gfsh* shell script appropriately.

> NOTE: make sure to adjust the file system paths and file references based on your installation location.

### Running Remotely

If the *GemFire* client and server are separated by a network and running on different systems, then the example application 
and tests allow you to configure IP addresses, ports and so on.

The following is a list of configuration System properties you can use when invoking either the server or client (test).

System Property | Client/Server | Default | Description
--------------- | ------------- | ------- | -----------
spring.gemfire.log.level | both | config | Configures the GemFire log level.
spring.gemfire.locator.host-port | server | locahost[11235] | Configures the embedded GemFire Locator in which to connect.
spring.gemfire.manager.port | server | 1199 | Configures the port on which the embedded GemFire Manager listens for JMX clients (e.g. Gfsh).
spring.gemfire.cache.server.bind-address | server | localhost | Configures the IP address of the NIC on which the GemFire Cache Server will bind the ServerSocket.
spring.gemfire.cache.server.hostname-for-clients | server | localhost | Configures the name of the DNS hostname that client will use to resolve the GemFire Cache Server.
spring.gemfire.cache.server.port | server | 12480 | Configures the port on which the embedded GemFire Cache Server listens for the Gemfire cache clients.
spring.gemfire.cache.server.host | client | localhost | Configures the host that client uses to connect the GemFire Cache Server. 
spring.gemfire.cache.server.port | client | 12480| Configures the port that client uses to connect the GemFire Cache Server. 

To run the *GemFire Server* listening on a different port with another log level, use the following:

```
$ mvn -pl spring-gemfire-clientserver spring-boot:run -Dspring.gemfire.cache.server.port=43210 -Dspring.gemfire.log.level=warning
```

Likewise, you need to make sure you change the port the *GemFire* cache client tries to connect to when running the tests:

```
$ mvn -pl spring-gemfire-clientserver test -Dspring.gemfire.cache.server.port=43210
```

### Test

Once the *GemFire Server* is configured and running, you can run the test with the following:

```
$ mvn -pl spring-gemfire-clientserver test
```

(optional) Once the test runs and completes successfully, you can return to *Gfsh* and execute the following command
to see that the contents of the cache were updated successfully by the test during execution.

```
gfsh>describe region --name=Factorials
```
