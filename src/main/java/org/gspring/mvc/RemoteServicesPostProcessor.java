package org.gspring.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

class RemoteServicesPostProcessor implements BeanPostProcessor, Ordered {
	private GwtServiceRegistryImpl gwtServiceRegistryImpl = new GwtServiceRegistryImpl();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		bindRegistryService(bean);

		List<String> relativePaths = findRelativePaths(bean.getClass());

		checkForAlreadyRegisteredBeans(bean, relativePaths);

		registerBeans(bean, relativePaths);

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	private void bindRegistryService(Object bean) {
		if (bean instanceof GwtServiceRegistryAware) {
			GwtServiceRegistryAware gwtServiceRegistryAware = (GwtServiceRegistryAware) bean;
			gwtServiceRegistryAware.setServiceRegistry(gwtServiceRegistryImpl);
		}
	}

	private void checkForAlreadyRegisteredBeans(Object bean, List<String> relativePaths) {
		for (String relativePath : relativePaths) {
			Assert.isTrue(!gwtServiceRegistryImpl.beanAlreadyExists(relativePath), "Bean " + bean + " could not be registered for path " + relativePath);
		}
	}

	private void registerBeans(Object bean, List<String> relativePaths) {
		for (String relativePath : relativePaths) {
			gwtServiceRegistryImpl.cacheBean(relativePath, bean);
		}
	}

	private static List<String> findRelativePaths(Class<?> beanClass) {
		List<String> annotations = new ArrayList<String>();

		RemoteServiceRelativePath annotation = beanClass.getAnnotation(RemoteServiceRelativePath.class);
		if (annotation != null) {
			annotations.add(annotation.value());
		}

		for (Class<?> ifc : beanClass.getInterfaces()) {
			annotation = AnnotationUtils.findAnnotation(ifc, RemoteServiceRelativePath.class);
			if (annotation != null) {
				annotations.add(annotation.value());
			}
		}

		return annotations;

	}

	private class GwtServiceRegistryImpl implements GwtServiceRegistry {
		private Object lockForReplacing = new Object();

		private Map<String, Object> remoteServicesMapping = new HashMap<String, Object>();

		@Override
		public Object getDelegate(String url) {
			if (beanAlreadyExists(url)) {
				return remoteServicesMapping.get(url);
			} else {
				replace(url);
				return getDelegate(url);
			}
		}

		private boolean beanAlreadyExists(String relativeUrl) {
			return remoteServicesMapping.containsKey(relativeUrl);
		}

		private void cacheBean(String url, Object bean) {
			remoteServicesMapping.put(url, bean);
		}

		private void replace(String url) {
			synchronized (lockForReplacing) {
				for (String relativeUrl : remoteServicesMapping.keySet()) {
					boolean matches = url.contains(relativeUrl);
					if (matches) {
						Object existingBean = remoteServicesMapping.remove(relativeUrl);
						remoteServicesMapping.put(url, existingBean);
						return;
					}
				}
			}
			throw new RuntimeException("Could not find for service for url: " + url);
		}
	}
}
