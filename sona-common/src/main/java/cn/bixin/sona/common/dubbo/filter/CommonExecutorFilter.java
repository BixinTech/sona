package cn.bixin.sona.common.dubbo.filter;

import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.common.exception.YppRunTimeException;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

/**
 * @author qinwei
 */
@Activate(group = CommonConstants.PROVIDER, order = Ordered.LOWEST_PRECEDENCE - 2)
public class CommonExecutorFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CommonExecutorFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (invocation.getMethodName().startsWith("$")) {
            return invoker.invoke(invocation);
        }

        Class<?> service = invoker.getInterface();
        String methodName = invocation.getMethodName();
        Object[] arguments = invocation.getArguments();

        Method method;
        try {
            method = service.getMethod(methodName, invocation.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.warn("call={}.{} reflect method error. ", service.getSimpleName(), methodName);
            return invoker.invoke(invocation);
        }

        Result result = invoker.invoke(invocation);

        if (method.isAnnotationPresent(CommonExecutor.class)) {
            CommonExecutor commonExecutor = method.getAnnotation(CommonExecutor.class);
            if (commonExecutor.printParam()) {
                log.info("call={}.{},params={}", service.getSimpleName(), methodName, JSON.toJSONString(arguments));
            }
            if (commonExecutor.printResponse()) {
                log.info("call={}.{} hasException={},result={}", service.getSimpleName(), methodName, result.hasException(), JSON.toJSONString(result.getValue()));
            }
        }

        if (!result.hasException()) {
            return result;
        }

        resetErrorResultValue(result, service.getSimpleName(), method);
        return result;
    }

    private void resetErrorResultValue(Result result, String serviceName, Method method) {
        Throwable exception = result.getException();
        if (exception instanceof YppRunTimeException) {
            resetYppRuntimeExceptionResult(result, (YppRunTimeException) exception, serviceName, method);
        } else if (exception instanceof ConstraintViolationException) {
            Object response = null;
            if (Response.class.isAssignableFrom(method.getReturnType())) {
                response = Response.fail(Code.ERROR_PARAM);
            }
            result.setValue(response);
        } else if (exception instanceof CompletionException || exception instanceof CancellationException) {
            Throwable throwable = exception.getCause();
            if (throwable instanceof YppRunTimeException) {
                resetYppRuntimeExceptionResult(result, (YppRunTimeException) throwable, serviceName, method);
            } else {
                resetSystemErrorCodeResult(result, exception, serviceName, method);
            }
        } else {
            resetSystemErrorCodeResult(result, exception, serviceName, method);
        }
        result.setException(null);
    }

    private void resetYppRuntimeExceptionResult(Result result, YppRunTimeException ex, String serviceName, Method method) {
        log.warn("YppRunTimeException. service={},method={},code={},message={}", serviceName, method.getName(), ex.getCode().getCode(), ex.getCode().getMessage());
        Object response = null;
        if (Response.class.isAssignableFrom(method.getReturnType())) {
            response = Response.fail(ex.getCode(), ex.getExt());
        }
        result.setValue(response);
    }

    private void resetSystemErrorCodeResult(Result result, Throwable exception, String serviceName, Method method) {
        Object response = null;
        if (Response.class.isAssignableFrom(method.getReturnType())) {
            response = Response.fail(Code.business("9000", "服务异常"));
        }
        result.setValue(response);
        log.error("call={}.{} error.", serviceName, method.getName(), exception);
    }

}
