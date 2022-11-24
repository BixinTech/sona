package cn.bixin.sona.common.dubbo.filter;

import cn.bixin.sona.common.dto.Response;
import com.dianping.cat.Cat;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

import static cn.bixin.sona.common.exception.RpcExceptionCode.*;

/**
 * @author qinwei
 */
@Activate(group = CommonConstants.CONSUMER, order = -10000000)
public class ExceptionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
        Class returnType = getReturnType(invoker, invocation);
        try {
            result = invoker.invoke(invocation);
        } catch (RpcException e) {
            if (Response.class.isAssignableFrom(returnType)) {
                return handleRpcException(invoker, invocation, e);
            } else {
                logRpcException(invoker, invocation, e);
                throw e;
            }
        } catch (Exception e) {
            logUnknownException(invoker, invocation, e);
            throw e;
        }
        return result;
    }

    public Result handleRpcException(Invoker<?> invoker, Invocation invocation, RpcException e) {
        logRpcException(invoker, invocation, e);

        Response response;
        if (e.isNetwork()) {
            response = Response.fail(NETWORK_EXCEPTION);
        } else if (e.isTimeout()) {
            response = Response.fail(TIMEOUT_EXCEPTION);
        } else if (e.isBiz()) {
            response = Response.fail(BIZ_EXCEPTION);
        } else if (e.isForbidded()) {
            response = Response.fail(FORBIDDEN_EXCEPTION);
        } else if (e.isSerialization()) {
            response = Response.fail(SERIALIZATION_EXCEPTION);
        } else {
            response = Response.fail(UNKNOWN_EXCEPTION);
        }
        return AsyncRpcResult.newDefaultAsyncResult(response, null, invocation);
    }

    public void logRpcException(Invoker<?> invoker, Invocation invocation, RpcException e) {
        String name = invoker.getInterface().getName() + '.' + invocation.getMethodName();
        if (e.getCause() != null) {
            log.error("invoke occur RpcException:" + e.getCause().getClass().getName() + ". method: " + invocation.getMethodName()
                    + " arguments: " + Arrays.toString(invocation.getArguments()) + " , url is " + invoker.getUrl(), e);
            Cat.logEvent("Exception: " + e.getCause().getClass().getSimpleName(), name);
        } else {
            log.error("invoke occur RpcException:" + ". method: " + invocation.getMethodName()
                    + " arguments: " + Arrays.toString(invocation.getArguments()) + " , url is " + invoker.getUrl(), e);
            Cat.logEvent("Exception: UNKNOW", name);
        }
    }

    public void logUnknownException(Invoker<?> invoker, Invocation invocation, Throwable e) {
        log.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);

        Cat.logEvent("Exception:" + e.getClass().getSimpleName(), invoker.getInterface().getName() + '.' + invocation.getMethodName());
    }

    public Class<?> getReturnType(Invoker<?> invoker, Invocation invocation) {
        Class<?> clazz = invoker.getInterface();
        Method method = null;
        try {
            method = clazz.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("Nosuch method exception" + RpcContext.getContext().getRemoteHost()
                    + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName(), e);
        }
        if (method != null) {
            return method.getReturnType();
        }
        return Object.class;
    }
}
