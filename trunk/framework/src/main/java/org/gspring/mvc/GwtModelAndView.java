package org.gspring.mvc;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * MVC Model And View for GWT RPC calls. Contains and uses for rendering GWT RPC
 * Servlet
 * 
 * @author a.buzmakoff
 * 
 */
public class GwtModelAndView extends ModelAndView {
	private static final String DELEGATE_MODEL_KEY = "delegate";

	public GwtModelAndView(ServletContext servletContext, ServletConfig servletConfig, Object delegate) {
		setView(new GwtRpcView(servletContext, servletConfig));
		addAllObjects(Collections.singletonMap(DELEGATE_MODEL_KEY, delegate));
	}

	private class GwtRpcView implements View {
		private final ServletContext servletContext;
		private final ServletConfig servletConfig;

		private GwtRpcView(ServletContext servletContext, ServletConfig servletConfig) {
			this.servletContext = servletContext;
			this.servletConfig = servletConfig;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
			Object delegate = model.get(DELEGATE_MODEL_KEY);

			RemoteServiceServlet rpcServlet = new RemoteServiceServlet(delegate) {
				private static final long serialVersionUID = -7744815355232246902L;

				@Override
				public ServletContext getServletContext() {
					return servletContext;
				}

				@Override
				public ServletConfig getServletConfig() {
					return servletConfig;
				}
			};

			rpcServlet.doPost(request, response);
		}
	}
}