# Running the samples of usage #

## RPC approach ##

In order to try RPC approach integration the standard (modified server part) GWT "Hello World" application bundled with Eclipse plugin can be built and run:

1. Checkout sample source for RPC approach
http://code.google.com/p/gspring/source/browse/#svn%2Ftrunk%2Fsample%2Frpc

2.
Make sure port 8080 is free

3.
Run from the root of sample code
```
mvn jetty:run-war
```

4. Open http://localhost:8080/rpc-sample/index.htm for the RPC sample and http://localhost:8080/rpc-security-sample/index.htm for the Session expired sample

## Request Factory approach ##
1. Find the "DynaTableRf" sample in GWT SDK for 2.5 version (2.5 uses maven to assemble) by http://google-web-toolkit.googlecode.com/svn/releases/2.5/samples/dynatablerf

2. Remove old servlet declaration in web.xml for RequestFactory calls, add required libraries and make other changes according to your application (previous sample could be taken as template), replace old servlet mapping with new one
```
  <servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <url-pattern>/gwtRequest</url-pattern>
  </servlet-mapping>
```

3. Run Jetty server in the same way like in previous sample