# valhalla-cache
## About
This is a custom library which enables caching methods' calls. In a current version it is tightly coupled with the Spring 4 framework.

## How to configure valhalla-cache
In order to use **valhalla-cache** library must be installed in a local maven repository. Then it can be included in pom as a maven dependency

```xml
<dependencies>
  ...
  <dependency>
    <groupId>pl.wawek.valhalla</groupId>
    <artifactId>valhalla-cache</artifactId>
    <version>1.0.0</version>
  </dependency>
  ...
</dependencies>
```

Since **valhalla-cache** uses load-time weaving it is necessary to configure [aspectj-maven-plugin](http://www.mojohaus.org/aspectj-maven-plugin/) in a build node

```xml
<build>
  <plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>aspectj-maven-plugin</artifactId>
        <version>1.8</version>
        <configuration>
            <complianceLevel>1.8</complianceLevel>
            <source>1.8</source>
            <target>1.8</target>
            <weaveDependencies>
                <weaveDependency>
                    <groupId>pl.wawek.valhalla</groupId>
                    <artifactId>valhalla-cache</artifactId>
                </weaveDependency>
            </weaveDependencies>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>compile</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.2</version>
        <configuration>
            <source>${java.source-target.version}</source>
            <target>${java.source-target.version}</target>
        </configuration>
    </plugin>
  </plugins>
</build>
```

Dependencies listed in *weaveDependencies* must be included in the *dependencies* as well as aspectjrt and aspectweaver dependencies.

```xml
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjrt</artifactId>
  <version>${org.aspectj.aspectjrt.version}</version>
</dependency>
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>${org.aspectj.aspectjrt.version}</version>
</dependency>
```

##How to use valhalla-cache
Cache needs to be configured. The configuration requires three parameters: maximum capacity of the cache, name of the cache and the eviction algorithm. Sample configuration is below
```java
new ValhallaConfiguration.Config()
                .algorithm(Algorithm.LRU)
                .capacity(CAPACITY)
                .name("testCache")
                .configure();
```

Finally, all which needs to be done is to put *@Valhalla* annotation over method which returned value should be cached.
```java
public class TestClass {

  @Valhalla
  public long cacheValue() {
  ...
  }
}
```
