# JMESPath Studio

This project demonstrates how to use the [io.burt](https://github.com/burtcorp/jmespath-java)
JMESPath implementation and provides a Swing UI for testing [JMESPath expressions](http://jmespath.org/).

## Hello JMESPath

All the JMESPath-related code is contained in
[JMESUtil.java](https://github.com/terheyden/JMESPathStudio/blob/master/src/main/java/com/terheyden/jmespathstudio/ui/JMESUtil.java) - it's a nice wrapper
around the canonical `io.burt` implementation.

Usage is in [MainUI.java](https://github.com/terheyden/JMESPathStudio/blob/master/src/main/java/com/terheyden/jmespathstudio/ui/MainUI.java).

## Running JMESPath Studio

If you compile and run the JAR, you get a nice little Swing UI that lets you test out
your JMESPath expressions. Results are updated automatically as you type.

![JMESPath Studio](https://github.com/terheyden/JMESPathStudio/blob/master/src/main/resources/JMESPathStudio01.png)
