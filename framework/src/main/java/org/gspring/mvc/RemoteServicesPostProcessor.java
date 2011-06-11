package org.gspring.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Post processor to search beans and provide with Beans registry class
 * 
 * @author a.buzmakoff
 * 
 */
class RemoteServicesPostProcessor implements BeanPostProcessor, Ordered {
	private GwtServiceRegistryImpl gwtServiceRegistryImpl = new GwtServiceRegistryImpl();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		bindRegistryService(bean);

		List<String> relativePaths = findRelativePathsFromSuperInterfacesOnly(bean.getClass());

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

	protected List<String> findRelativePathsFromSuperInterfacesOnly(Class<?> beanClass) {
		List<String> annotations = new ArrayList<String>();

		for (Class<?> interfaces : beanClass.getInterfaces()) {
			RemoteServiceRelativePath annotation = AnnotationUtils.findAnnotation(interfaces, RemoteServiceRelativePath.class);
			if (annotation != null) {
				annotations.add(annotation.value());
			}
		}

		return annotations;

	}
}
