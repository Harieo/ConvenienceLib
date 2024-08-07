# ConvenienceLib
![Java CI - Build](https://github.com/Harieo/ConvenienceLib/workflows/Java%20CI%20-%20Build/badge.svg)
## Introduction
A library of code and APIs to make Spigot coding less of a hassle.  
The library is an implementation of the Spigot API which allows you to perform common tasks without having to copy and paste code constantly. 

As of version 1.1.0, the library now uses a more typical semantic versioning system. However, this also means that older Spigot versions will no longer be fully supported. We will aim to keep up to date with the latest Spigot versions moving forward and discontinue providing updates for older versions.

Please **discontinue** use of the Spigot-* versions. Instead, use our new semantic versioning system (1.#.#) as it provides better version control.
## Major Update 1.2.1 - Modular Library Restructuring
Please be aware that as of 1.2.1, the way Convenience is packaged has changed to a modular structure.

This means that **ALL existing packages are deprecated** in favour of the new common API module.
The classes are named and function mostly the same.  
The reason for the change is to allow Convenience to extend onto the Bungee and Velocity APIs.

**All versions 1.1.\* are now End of Life (EOL).** They have been deprecated in the newest version and may be removed
in later updates.

### Version Chart

Please consult the chart and use the latest library version for the Spigot version you intend to use.

| Version       | Supported Spigot Version | Changes                                                        |
|---------------|--------------------------|----------------------------------------------------------------|
| Spigot-1.12.2 | 1.12.2                   | Support for 1.12.2                                             |
| Spigot-1.16.4 | 1.16.4                   | Support for 1.16.4                                             |
| Spigot-1.16.5 | 1.16.5                   | Support for 1.16.5                                             |
| 1.1.0         | 1.17.1                   | Changed to Java 16 and 1.17.1 Spigot                           |
| 1.1.1         | 1.17.1                   | Changed the way menu interaction works                         |
| 1.1.2         | 1.18.2/1.19              | Change to Java 17. Support for 1.18.2 and 1.19.                |
| 1.1.3         | 1.20.1                   | Update dependencies and support for 1.20.1.                    |
| 1.2.1         | 1.20.1                   | Major refactoring of the library to support modular API model. |

## Installation
This library runs on Maven for compilation, so we recommend using our remote Maven repository to prevent having to clone from 
our GitHub unnecessarily. 

First, add our remote repository which stores all of our artifacts:
```
<repository>
    <id>harieo-repo</id>
    <url>https://repo.harieo.net/repository/maven-releases</url>
</repository>
```

Second, add the dependency for the platform you want to use.
```
<dependency>
    <groupId>net.harieo</groupId>
    <artifactId>ConvenienceLib-[platform]</artifactId>
    <version>1.2.1</version>
</dependency>
```

The following modules exist:
- ConvenienceLib-spigot (Platform: Native Spigot)
- ConvenienceLib-bungee (Platform: BungeeCord)
- ConvenienceLib-velocity (Platform: Velocity)
- ConvenienceLib-common (No Platform - This is our abstract module)

Therefore, to use ConvenienceLib for your Spigot plugin, use the following dependency:
```
<dependency>
    <groupId>net.harieo</groupId>
    <artifactId>ConvenienceLib-spigot</artifactId>
    <version>1.2.1</version>
</dependency>
```

Please consult the chart of versions above if you want to support an older version of Spigot. Simply change the version in the dependency as required.

**If you wish to compile the library into your plugin**, to prevent having to distribute the library as a separate jar, we recommend making use of [Maven Compiler](https://maven.apache.org/plugins/maven-compiler-plugin/) and [Maven Shade](https://maven.apache.org/plugins/maven-shade-plugin/).

## Support
If you have an issue with the library or wish to make a contribution, the full power of GitHub is at your disposal.  
Please feel free to make [Pull Requests](https://github.com/Harieo/ConvenienceLib/pulls) and 
[Issues](https://github.com/Harieo/ConvenienceLib/issues) to submit improvements/report issues.

If you just have a question, joining the Discord is the best way to get in contact! 

[![Discord Server](https://discordapp.com/api/guilds/679733506427191330/embed.png?style=banner2)](https://discord.gg/zTwWZAR)
