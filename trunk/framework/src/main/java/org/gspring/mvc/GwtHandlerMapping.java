package org.gspring.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * MVC Handler Mapping for GWT RPC calls
 * 
 * @author a.buzmakoff
 * 
 */
class GwtHandlerMapping extends AbstractDetectingUrlHandlerMapping {
	@Autowired(required = false)
	private CacheManager cacheManager = new NoOpCacheManager();

	@Override
	protected String[] determineUrlsForHandler(String beanName) {
		try {
			RemoteService bean = getApplicationContext().getBean(beanName, RemoteService.class);

			checkInheritsRemoteServiceDirectly(bean);

			List<String> relativePaths = new ArrayList<String>();

			findRelativePaths(bean.getClass(), relativePaths);

			return relativePaths.toArray(new String[0]);
		} catch (BeanNotOfRequiredTypeException e) {
			return null;
		}
	}

	private void findRelativePaths(Class<?> beanClass, List<String> relativePaths) {
		for (Class<?> interf : beanClass.getInterfaces()) {
			if (isRemoteServiceType(interf)) {
				RemoteServiceRelativePath relativePathAnnotation = beanClass.getAnnotation(RemoteServiceRelativePath.class);
				if (relativePathAnnotation != null) {
					relativePaths.add(relativePathAnnotation.value());
				} else {
					logger.warn("Bean of type [" + beanClass + "] does not have annotation of type [" + RemoteServiceRelativePath.class + "] and is skipped");
				}
			} else {
				findRelativePaths(interf, relativePaths);
			}
		}
	}

	private void checkInheritsRemoteServiceDirectly(Object bean) {
		for (Class<?> interfce : bean.getClass().getInterfaces()) {
			Assert.isTrue(!isRemoteServiceType(interfce), "Bean [" + (Class<?>) bean.getClass() + "] inherits [" + RemoteService.class + "] directly");
		}
	}

	private boolean isRemoteServiceType(Class<?> interf) {
		return RemoteService.class.equals(interf);
	}

	@Override
	protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
		ValueWrapper cached = getCache().get(urlPath);
		if (cached != null) {
			return cached.get();
		}

		for (String relativePath : getHandlerMap().keySet()) {
			if (urlPath.endsWith(relativePath)) {
				Object value = getHandlerMap().get(relativePath);

				getCache().put(urlPath, value);

				return value;
			}
		}

		return null;
	}

	private Cache getCache() {
		final String name = getClass().getCanonicalName();
		return cacheManager.getCache(name);
	}
}
