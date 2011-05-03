package org.gspring.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

class GwtHandlerAdapter implements HandlerAdapter, Ordered, ServletContextAware {
	private static final int NEVER_LAST_MODIFIED = -1;
	private ServletContext servletContext;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public boolean supports(Object handler) {
		return handler instanceof GwtHandler;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		GwtHandler gwtHandler = (GwtHandler) handler;

		return new GwtModelAndView(this.servletContext, gwtHandler.getDelegate());
	}

	@Override
	public long getLastModified(HttpServletRequest request, Object handler) {
		return NEVER_LAST_MODIFIED;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}

}
