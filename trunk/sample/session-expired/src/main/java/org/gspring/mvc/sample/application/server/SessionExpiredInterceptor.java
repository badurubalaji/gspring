package org.gspring.mvc.sample.application.server;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.gspring.mvc.sample.application.shared.security.SessionExpiredExceptionApp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SessionExpiredInterceptor implements MethodInterceptor, BeanNameAware, ApplicationContextAware {
    private String name;

    private ApplicationContext applicationContext;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        throw new SessionExpiredExceptionApp();
    }

    public Object newInstance(Class clazz) {
        try {
            Enhancer e = new Enhancer();
            e.setSuperclass(clazz);
            e.setCallback((Callback) applicationContext.getBean(name));
            return e.create();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
