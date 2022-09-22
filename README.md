# sudachi

## Getting Started

### Installation

`pom.xml`
```xml
 <repository>
  <id>github</id>
  <name>GitHub Packages</name>
  <url>https://maven.pkg.github.com/2mug1/sudachi</url>
</repository>

<dependency>
  <groupId>net.iamtakagi</groupId>
  <artifactId>sudachi</artifactId>
  <version>1.0.0</version>
  <scope>compile</scope>
</dependency>
```

`build.gradle`
```gradle
repositories {
  maven (url = "https://maven.pkg.github.com/2mug1/sudachi")
}
dependencies {
  implementation("net.iamtakagi:sudachi:1.0.0")
}
```

## LICENSE
[MIT License](./LICENSE) (© 2022 iamtakagi)