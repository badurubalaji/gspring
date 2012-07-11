package org.gspring.mvc.rpc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Collections;

/**
 * MVC Model And View for GWT RPC calls.
 * Used to render GWT RPC Servlet response
 *
 * @author a.buzmakoff
 */
class RpcGwtModelAndView extends ModelAndView {
    static final String DELEGATE_MODEL_KEY = "delegate";

    RpcGwtModelAndView(View rpcView, Object delegate) {
        setView(rpcView);
        addAllObjects(Collections.singletonMap(DELEGATE_MODEL_KEY, delegate));
    }

}
