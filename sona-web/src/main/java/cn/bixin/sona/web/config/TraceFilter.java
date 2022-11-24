package cn.bixin.sona.web.config;

import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.common.trace.TracerContext;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author qinwei
 */
public class TraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        putParamFromHeader(TracerContext.TRACER_TRACE_ID, httpServletRequest);
        TraceHelper.init();
        try {
            chain.doFilter(request, response);
        } finally {
            TraceHelper.reset();
        }
    }

    private void putParamFromHeader(String key, HttpServletRequest request) {
        TracerContext context = TracerContext.getContext();
        String value = context.get(key);
        if (StringUtils.isEmpty(value)) {
            context.put(key, request.getHeader(key));
        }
    }

}
