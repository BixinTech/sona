package cn.bixin.sona.common.dubbo.filter;

import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.common.trace.TracerContext;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.Ordered;

/**
 * @author qinwei
 */
@Activate(group = CommonConstants.PROVIDER, order = Ordered.LOWEST_PRECEDENCE - 3)
public class ProviderTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TraceHelper.setTraceId(RpcContext.getContext().getAttachment(TracerContext.TRACER_TRACE_ID));
        return invoker.invoke(invocation);
    }
}
