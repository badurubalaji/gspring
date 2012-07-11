package org.gspring.mvc.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.View;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
class RpcGwtView implements View, ServletContextAware {
    private ServletContext servletContext;

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object delegate = model.get(RpcGwtHandlerMapping.DELEGATE_MODEL_KEY);

        RemoteServiceServlet rpcServlet = new RemoteServiceServlet(delegate) {
            private static final long serialVersionUID = -7744815355232246902L;

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

        };

        rpcServlet.doPost(request, response);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
