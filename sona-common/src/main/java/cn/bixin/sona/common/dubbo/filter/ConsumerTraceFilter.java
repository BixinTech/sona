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
@Activate(group = CommonConstants.CONSUMER, order = Ordered.HIGHEST_PRECEDENCE)
public class ConsumerTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(TracerContext.TRACER_TRACE_ID, TraceHelper.getTraceId());
        return invoker.invoke(invocation);
    }
}
