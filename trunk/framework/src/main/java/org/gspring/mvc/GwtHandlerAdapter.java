package org.gspring.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * MVC Handler Adapter for GWT RPC calls
 * 
 * @author a.buzmakoff
 * 
 */
class GwtHandlerAdapter implements HandlerAdapter, Ordered, ServletContextAware {
	private static final int NEVER_LAST_MODIFIED = -1;
	private ServletContext servletContext;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public boolean supports(Object handler) {
		final boolean filterOnlySupported = (handler instanceof RemoteService);
		return filterOnlySupported;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return new GwtModelAndView(this.servletContext, handler);
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
