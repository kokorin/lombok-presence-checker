# @PresenceChecker

Lombok extension which generates Presence Checker methods

## Tested with the help of [GitHub Actions](/.github/workflows/maven-tests.yml) 

![Tests](https://github.com/kokorin/lombok-presence-checker/workflows/Tests/badge.svg)

**JDK**: 7, 8, 11, 14

# Usage 

![Maven Central](https://img.shields.io/maven-central/v/com.github.kokorin.lombok/lombok-presence-checker?style=for-the-badge)

```xml
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>com.github.kokorin.lombok</groupId>
        <artifactId>lombok-presence-checker</artifactId>
        <version>0.0.2</version>
        <scope>provided</scope>
    </dependency>
    
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                    <path>
                        <groupId>com.github.kokorin.lombok</groupId>
                        <artifactId>lombok-presence-checker</artifactId>
                        <version>0.0.2</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Overview

You can annotate any field or type with `@PresenceChecker`, to let lombok together with lombok-presence-checker
generate presence checker methods (`hasFieldName()`) automatically.

This project allows easy implementation of partial update via REST API.

MapStruct is aware of [source presence checking](https://mapstruct.org/documentation/stable/reference/html/#source-presence-check)
and uses presence checker methods by default (if present of course) to verify if a field in a target object should be updated with a value in a source
object. Without presence checkers MapStruct by default updates only fields with non-null values.

### REST & Partial Updates
 
After incoming request body get parsed in a typed object in a REST controller one can't distinguish absent property from a property with null value.

Several strategies can be applied to partial update:
1. Treat any DTO property with null value as absent property and *do not* set corresponding entity property to null
2. Treat any DTO property with null value as property explicitly set to null, thus denying partial update
3. Parse request body as JsonObject (or Map), which seems not very suitable for strongly typed languages
4. Use JSON mapper for entity partial update with a price of JSON mapper leaking to REST Controller
5. For every field in DTO add a flag to mark it as present or not

Lombok-presence-checker aims at the last strategy. Check REST controller [example](/lombok-presence-checker-example/src/main/java/com/github/kokorin/lombok/example/LombokPresenceCheckerExampleApplication.java)
and corresponding [tests](/lombok-presence-checker-example/src/test/java/com/github/kokorin/lombok/example/LombokPresenceCheckerExampleApplicationTests.java)

## With Lombok and Lombok-Presence-Checker

```java
@Getter
@Setter
public class User {
    private String name;
}

@Getter
@Setter
@PresenceChecker
public class UserUpdateDto {
    private String name;
}

//MapStruct Mapper interface declaration
@Mapper
public interface UserMapper {
    void updateUser(UserUpdateDto dto, @MappingTarget User user);
}
```

## Generated Code

```java
public class User {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class UserUpdateDto {
    private boolean hasName;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.hasName = true;
    }

    public boolean hasName() {
        return this.hasName;
    }
}

//MapStruct Mapper implementation
public class UserMapperImpl implements UserMapper {
    @Override
    public void updateUser(UserUpdateDto dto, User user) {
        if ( dto == null ) {
            return;
        }

        if ( dto.hasName() ) {
            user.setName( dto.getName() );
        }
    }
}
```

# Build

Some dependencies have to be installed to local Maven repository before compilation

First initialize everything:

`mvn initialize`

Then build:

`mvn clean install`

