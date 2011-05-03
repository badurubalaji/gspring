package org.gspring.mvc;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtModelAndView extends ModelAndView {
	private static final String DELEGATE_MODEL_KEY = "delegate";

	public GwtModelAndView(ServletContext servletContext, Object delegate) {
		setView(new GwtRpcView(servletContext));
		addAllObjects(Collections.singletonMap(DELEGATE_MODEL_KEY, delegate));
	}

	private class GwtRpcView implements View {
		private final ServletContext servletContext;

		private GwtRpcView(ServletContext servletContext) {
			this.servletContext = servletContext;
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
			};

			rpcServlet.doPost(request, response);
		}
	}
}
