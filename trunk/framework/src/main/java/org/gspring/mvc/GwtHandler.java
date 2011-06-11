package org.gspring.mvc;

import org.springframework.util.Assert;

/**
 * MVC Handler for GWT RPC calls
 * 
 * @author a.buzmakoff
 *
 */
class GwtHandler {
	private Object delegate;
	private final String requestURI;

	public GwtHandler(Object delegate, String requestURI) {
		Assert.notNull(delegate);
		Assert.hasText(requestURI);

		this.delegate = delegate;
		this.requestURI = requestURI;
	}

	public Object getDelegate() {
		return delegate;
	}

	public String getRequestURI() {
		return requestURI;
	}

}
