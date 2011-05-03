package org.gspring.mvc;

class GwtHandler {
	private Object delegate;
	private final String requestURI;

	public GwtHandler(Object delegate, String requestURI) {
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
