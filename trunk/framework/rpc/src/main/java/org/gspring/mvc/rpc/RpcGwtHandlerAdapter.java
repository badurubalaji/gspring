package org.gspring.mvc.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * MVC Handler Adapter for GWT RPC calls
 *
 * @author a.buzmakoff
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RpcGwtHandlerAdapter implements HandlerAdapter, ServletContextAware, ServletConfigAware {
    private static final int NEVER_LAST_MODIFIED = -1;

    private ServletContext servletContext;
    private ServletConfig servletConfig;

    @Resource
    private View rpcGwtView;

    @Override
    public boolean supports(Object handler) {
        return handler instanceof RemoteService;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAllObjects(Collections.singletonMap(RpcGwtHandlerMapping.DELEGATE_MODEL_KEY, handler));
        modelAndView.setView(rpcGwtView);
        return modelAndView;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return NEVER_LAST_MODIFIED;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
}
