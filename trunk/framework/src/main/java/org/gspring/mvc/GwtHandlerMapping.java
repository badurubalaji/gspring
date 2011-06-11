package org.gspring.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

class GwtHandlerMapping implements HandlerMapping, Ordered, GwtServiceRegistryAware {
	private static final String GWT_DEFAULT_EXT = ".gwt";

	private GwtServiceRegistry serviceRegistry;

	public String extension = GWT_DEFAULT_EXT;

	public void setExtension(String extension) {
		Assert.hasText(extension);

		this.extension = extension;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void setServiceRegistry(GwtServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		String requestURI = request.getRequestURI();

		if (!requestURI.endsWith(extension)) {
			return null;
		}

		Object delegate = serviceRegistry.retrieveDelegate(requestURI);

		Assert.notNull(delegate, "No such URI registered");

		return new HandlerExecutionChain(new GwtHandler(delegate, requestURI));
	}

}
