# How to checkout framework and sample of usage #

http://code.google.com/p/gspring/source/checkout

# How to view framework and sample of usage #
http://code.google.com/p/gspring/source/browse

# How to build source code #

Project can be assembled using **maven** 2 (http://maven.apache.org/)

There are three options to build.
Each coresponds appropriate maven profile.
See maven profiles in pom.xml for details.

Assemble for RPC approach
```
mvn clean package -Prpc
```

Assemble for RequestFactory approach
```
mvn clean package -Prf
```

Assemble for both approaches
```
mvn clean package
```