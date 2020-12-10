# @PresenceChecker

Lombok extension which generates Presence Check methods

## Overview

## With Lombok and Lombok-Presence-Checker

```java
@PresenceChecker
@Getter
@Setter
public static class UserDto {
    private String name;
    private long value;
}
```

## Vanilla Java

# Build

Some dependencies have to be installed to local Maven repository before compilation

First initialize everything:

`mvn initialize`

Then build:

`mvn clean install`

