# ConvenienceLib
![Java CI - Build](https://github.com/Harieo/ConvenienceLib/workflows/Java%20CI%20-%20Build/badge.svg)

A library of code and APIs to make Spigot coding less of a hassle.  

This library is split into different Spigot versions due to issues of compatibility with new versions after they're released. 
These version-specific branches are labelled `Spigot-<version>` and the `master` branch will reflect only the very latest
version of Spigot that this library supports.  

Please clone from the branch holding the Spigot version you are working on or follow the steps below to use Maven.
**We support all Spigot versions which have a branch** and this library was designed to work with the most 
popular versions, even if they may now be oudated.

The library is an implementation of the Spigot API which allows you to perform common tasks without having to copy and paste code constantly. 

## Installation
This library runs on Maven for compilation, so we recommend using our remote Maven repository to prevent having to clone from 
our GitHub unnecessarily. 

Firstly, add our remote repository which stores all of our artifacts:
```
        <repository>
            <id>nexus</id>
            <name>Releases</name>
            <url>http://repo.harieo.net/repository/maven-releases</url>
        </repository>
```

Secondly, add the dependency. Change the version to `Spigot-<version>` where `<version>` is the Spigot version you're 
developing on. For example, `Spigot-1.12.2`.  

For a list of the versions we support, [consult our branch list](https://github.com/Harieo/ConvenienceLib/branches) 
to see if it exists. If your version does not exist as a branch, we unfortunately don't support it at the moment.

```
        <dependency>
            <groupId>uk.co.harieo</groupId>
            <artifactId>ConvenienceLib</artifactId>
            <version>Spigot-...</version>
        </dependency>
```

## Support
If you have an issue with the library or wish to make a contribution, the full power of GitHub is at your disposal.  
Please feel free to make [Pull Requests](https://github.com/Harieo/ConvenienceLib/pulls) and 
[Issues](https://github.com/Harieo/ConvenienceLib/issues) to submit improvements/report issues.

If you just have a question, joining the Discord is the best way to get in contact! 

[![Discord Server](https://discordapp.com/api/guilds/679733506427191330/embed.png?style=banner2)](https://discord.gg/zTwWZAR)
