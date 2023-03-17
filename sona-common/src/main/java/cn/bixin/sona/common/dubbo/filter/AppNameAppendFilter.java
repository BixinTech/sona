package cn.bixin.sona.common.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author qinwei
 */
@Activate(group = {Constants.CONSUMER})
public class AppNameAppendFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(Constants.APPLICATION_KEY, invoker.getUrl().getParameter(Constants.APPLICATION_KEY));
        return invoker.invoke(invocation);
    }
}

