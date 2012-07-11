package org.gspring.mvc.rf;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.View;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
class RfGwtView implements View, ServletContextAware {
    private RfServlet requestFactoryServlet = new RfServlet();

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        requestFactoryServlet.render(request, response);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        requestFactoryServlet.setServletContext(servletContext);
    }
}
