package org.gspring.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * MVC Handler Mapping for GWT RPC calls
 * 
 * @author a.buzmakoff
 * 
 */
class GwtHandlerMapping extends AbstractDetectingUrlHandlerMapping implements HandlerMapping, Ordered {
	private static final String GWT_DEFAULT_EXT = ".gwt";

	public String extension = GWT_DEFAULT_EXT;

	public void setExtension(String extension) {
		Assert.hasText(extension);

		this.extension = extension;
	}

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
		for (String relativePath : getHandlerMap().keySet()) {
			if (urlPath.endsWith(relativePath)) {
				return getHandlerMap().get(relativePath);
			}
		}
		return null;
	}
}
