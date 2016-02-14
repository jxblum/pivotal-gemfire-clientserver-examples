## Spring (Pivotal) GemFire Client/Server XML Example Application

The following example demonstrates how to configure both a *GemFire* client and server cache application using the 
*Spring (Data GemFire)* [XML namespace](http://docs.spring.io/spring-data-gemfire/docs/current/reference/html/#_spring_data_gemfire_core_schema_gfe).

This example tests connectivity between the *Spring-configured GemFire* client and server, then runs a test case that tests
a custom *GemFire* [CacheLoader](http://data-docs-samples.cfapps.io/docs-gemfire/latest/javadocs/japi/com/gemstone/gemfire/cache/CacheLoader.html) 
([SquaresCacheLoader](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver-xml/src/main/java/io/pivotal/gemfire/main/SpringGemFireServerXmlApplication.java#L44-L55) 
associated with the **Squares** *Region* computing the square of a number (specified as the cache "key") when requested, 
such as...

```
Region.get(4) == 16
```

Thus, the **Squares** *Region* will return "16" when the key "4" is requested.  The *SquaresCacheLoader* is only invoked
if the given key (i.e. number) is not present in the cache already.

### Prerequisites

To run this example you must install a *JDK* along with *Apache Maven* in your development environment and build the project.

See the [build](https://github.com/jxblum/pivotal-gemfire-clientserver-examples#build) section for more details.

### Running
 
The example *GemFire Server* application ([SpringGemFireServerXmlApplication](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver-xml/src/main/java/io/pivotal/gemfire/main/SpringGemFireServerXmlApplication.java)) 
is a *Spring Boot* application.  By default, the [module Maven POM file](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver-xml/pom.xml#L75-L82) 
is configured with the *Spring Boot* plugin, which can be used to run the *Spring*-based *GemFire Server*.

From the project root directory, you can run the *GemFire Server* on the command-line with the following `mvn` command:

```
$ mvn -pl spring-gemfire-clientserver-xml spring-boot:run
```

OOTB, this uses the module's default configuration settings (e.g. ports) to configure the *GemFire Server*.

In addition to starting a *GemFire Server*, the *Spring* configuration also configures the server to start an embedded 
*GemFire Locator* on port `11235` along with an embedded *GemFire Manager* on port `1199` as well as a *Cache Server* 
listening for cache clients on port `12480`. 

For convenience, this project module provides the  [**connect.gfsh** shell script](https://github.com/jxblum/pivotal-gemfire-clientserver-examples/blob/master/spring-gemfire-clientserver-xml/bin/connect.gfsh) 
to connect to the running *GemFire Server* in order to inspect the configuration.  **connect.gfsh** connects to
the running *GemFire Server's* embedded *Locator*, which in turn opens a connection to the embedded *Manager*, then
proceeds to `list members`, `describe` our member, `list regions`, `describe` our *Region* and then perform a `get`,
which will invoke the `SquaresCacheLoader` on our initially empty cache, resulting in calculation being performed.

When `describe member` is executed, you should see that the **Squares** *Region* exists on that member along with 
the network service endpoint of the GemFire `CacheServer` on part `12480`.  When `describe region` is executed, 
you should see the registered `SquaresCacheLoader`.

The *Gfsh* shell script can be ran interactively (inside the *GemFire Shell*) or non-interactively 
(e.g. scripted in `Bash`).

Of course, to use *Gfsh*, you must have a [distribution](https://network.pivotal.io/products/pivotal-gemfire) 
of *Pivotal GemFire* installed on your system.  You can also use an [Apache Geode](http://geode.incubator.apache.org/) 
installation in place of *Pivotal GemFire*.  Be sure to follow the [installation instructions in the User Guide](http://gemfire.docs.pivotal.io/docs-gemfire/getting_started/installation/install_intro.html).

Invoke the *Gfsh* shell script with the `run` command inside the *GemFire Shell*:

```
gfsh>run --file=/absolute/path/to/pivotal-gemfire-clientserver-examples/spring-gemfire-clientserver-xml/bin/connect.gfsh
```

Or non-interactively, in a (Bash) shell script like so:

```
!/bin/bash
...

gfsh -e "run --file=/absolute/path/to/pivotal-gemfire-clientserver-examples/spring-gemfire-clientserver-xml/bin/connect.gfsh"
```

If you are running the *GemFire Server* remotely, you will need to tweak the *connect.gfsh* shell script appropriately.

> NOTE: make sure to adjust the file system paths and file references based on your installation location.

### Test

Once the *GemFire Server* is configured and running, you can run the test with the following:

```
$ mvn -pl spring-gemfire-clientserver test
```

(optional) Once the test runs and completes successfully, you can return to *Gfsh* and execute the `describe region` 
command again to see that the contents of the cache were updated successfully by the test during execution.

```
gfsh>describe region --name=Squares
```

(optional) You can also just make additional requests right from *Gfsh* to the *GemFire Server* cache **Squares** *Region* 
in order to perform additional computations.

### Running Remotely

If the *GemFire* client and server are separated by a network, running on different systems, then the example application 
and tests allow you to configure IP addresses, ports and so on in order to establish connectivity.

The following is a list of project-specific configuration System properties you can use when invoking either 
the client (test) or the server:

System Property | Client/Server | Default | Description
--------------- | ------------- | ------- | -----------
spring.gemfire.log.level | both | config | Configures the GemFire log level.
spring.gemfire.locator.host-port | server | localhost[11235] | Configures the embedded GemFire Locator in which to start and connect.
spring.gemfire.manager.port | server | 1199 | Configures the port on which the embedded GemFire Manager listens for JMX clients (e.g. Gfsh).
spring.gemfire.cache.server.bind-address | server | localhost | Configures the IP address of the NIC on which the GemFire Cache Server will bind the ServerSocket.
spring.gemfire.cache.server.hostname-for-clients | server | localhost | Configures the name of the DNS hostname that client will use to resolve the GemFire Cache Server.
spring.gemfire.cache.server.port | both | 12480 | Configures the port on which the embedded GemFire Cache Server listens for the Gemfire cache clients. Also the port that client uses to connect the GemFire Cache Server.
spring.gemfire.cache.server.host | client | localhost | Configures the host that client uses to connect the GemFire Cache Server. 

To run the *GemFire Server* listening on a different port configured with a log level of "warning", use the following:

```
$ mvn -pl spring-gemfire-clientserver spring-boot:run -Dspring.gemfire.cache.server.port=43210 -Dspring.gemfire.log.level=warning
```

Likewise, you need to make sure to change the port that the *GemFire* cache client tries to connect to the server on 
when running the test.  Additionally, in the remote case, you will need to specify the server's IP address:

```
$ mvn -pl spring-gemfire-clientserver test -Dspring.gemfire.cache.server.port=43210 -Dspring.gemfire.cache.server.host=10.101.21.1
```
