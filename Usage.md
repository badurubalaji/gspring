# Typical RPC approach usage #

See the sample provided in http://code.google.com/p/gspring/source/browse/#svn%2Ftrunk%2Fsample%2Frpc.

## Sample Spring context file ##
```
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-3.1.xsd
	http://www.springframework.org/schema/mvc
	http://www.springframework.org/schema/mvc/spring-mvc-3.1.xsd">

	<mvc:annotation-driven />

	<context:annotation-config />

	<!-- IMPORTANT PART -->
        <import resource="classpath:/gspring-context.xml" />

	<!-- OPTIONAL: can be used by gspring if declared -->
	<bean id="cacheManager" class="org.springframework.cache.concurrent.ConcurrentMapCacheManager" />
	
	<!-- ANY DECLARATIONS -->

</beans>
```

## web.xml file ##
```
	<!--
		Providing mapping as *.gwt allows simple optimizations 
		if all RemoteService services have URL ending with ".gwt"
		(URL is placed in @RemoteServiceRelativePath)
	-->
	<servlet-mapping>
		<servlet-name>DispatcherServlet</servlet-name>
		<url-pattern>*.gwt</url-pattern>
	</servlet-mapping>

	<!-- 
	Servlet can be mapped also as:

	<servlet-mapping>
		<servlet-name>DispatcherServlet</servlet-name>
		<url-pattern>*</url-pattern>
	</servlet-mapping>

	, because handler mapping inspects all services (beans) declared in application context
	and inherited from com.google.gwt.user.client.rpc.RemoteService 
	for value provided by com.google.gwt.user.client.rpc.RemoteServiceRelativePath annotation

	(inheriting this interface with such annotation is GWT requirement)
	
	-->

```


## Sample RemoteService Service interface ##
```
@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface GreetingService extends RemoteService {
	String greetServer(String name);
}
```


## Sample RemoteService Class implementer ##
```

@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {
	@Override
	public String greetServer(String name) {
		return MessageFormat.format("Hello, {0}!", name);
	}
}
```


# Typical RequestFactory approach usage #

## Sample Spring context file ##
Can be the same as in the sample above

## web.xml file ##
```
	<servlet-mapping>
		<servlet-name>DispatcherServlet</servlet-name>
		<!-- Standard URL -->
		<url-pattern>/gwtRequest</url-pattern>
	</servlet-mapping>

	<!-- 
		In case the URL needs to be changed - see below how to customize	
	-->
```


# Customizing Typical RequestFactory approach usage #

If
1. URL for mapping (say it would be **anyCustomUrl**)
2. Custom com.google.web.bindery.requestfactory.server.ExceptionHandler and com.google.web.bindery.requestfactory.server.ServiceLayerDecorator
should be provided Spring context could be extended:
```
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-3.1.xsd
	http://www.springframework.org/schema/mvc
	http://www.springframework.org/schema/mvc/spring-mvc-3.1.xsd">

	<mvc:annotation-driven />

	<context:annotation-config />

	<!-- IMPORTANT PART -->
        <import resource="classpath:/gspring-context.xml" />

	<!-- OPTIONAL: can be used by gspring if declared -->
	<bean id="cacheManager" class="org.springframework.cache.concurrent.ConcurrentMapCacheManager" />
	

	<!-- CUSTOMIZATION: Custom URL -->
	<!-- EXACT ID (to override): rfGwtHandlerMapping -->
	<bean id="rfGwtHandlerMapping" class="org.gspring.mvc.rf.RfGwtHandlerMapping">
		<constructor-arg value="anyCustomUrl"/>
	</bean>

	<!-- CUSTOMIZATION: Custom ExceptionHandler and ServiceLayerDecorator(s) -->
	<!-- ANY Beans of these types can be declared here and will be automcatically detected and used -->

	<!-- ANY DECLARATIONS -->

</beans>
```