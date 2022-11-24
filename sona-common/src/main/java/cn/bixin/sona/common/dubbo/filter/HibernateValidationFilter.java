package cn.bixin.sona.common.dubbo.filter;

import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.Response;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ArrayUtils;
import org.apache.dubbo.config.spring.extension.SpringExtensionFactory;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qinwei
 */
@Activate(group = CommonConstants.PROVIDER, order = Ordered.LOWEST_PRECEDENCE - 1)
public class HibernateValidationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HibernateValidationFilter.class);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (invocation.getMethodName().startsWith("$")) {
            return invoker.invoke(invocation);
        }

        Class<?> service = invoker.getInterface();
        Method method;
        try {
            method = service.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.warn("call={}.{} reflect method error. ", service.getSimpleName(), invocation.getMethodName());
            return invoker.invoke(invocation);
        }

        Set<ApplicationContext> contexts = SpringExtensionFactory.getContexts();
        if (CollectionUtils.isEmpty(contexts)) {
            return invoker.invoke(invocation);
        }
        Map<String, ?> beansMap = contexts.iterator().next().getBeansOfType(service);
        if (beansMap.isEmpty()) {
            return invoker.invoke(invocation);
        }
        Object serviceInstance = beansMap.values().stream().findAny().orElse(null);

        if (invalid(serviceInstance, method, invocation.getArguments())) {
            Response<Object> response = null;
            if (Response.class.isAssignableFrom(method.getReturnType())) {
                response = Response.fail(Code.ERROR_PARAM);
            }
            Result result = AsyncRpcResult.newDefaultAsyncResult(response, null, invocation);
            result.setAttachments(invocation.getAttachments());
            return result;
        }
        return invoker.invoke(invocation);
    }

    private boolean invalid(Object validateParamService, Method method, Object[] args) {
        return !methodArgsValidate(validateParamService, method, args)
                || !methodArgsFieldValidate(method, args);
    }

    private boolean methodArgsFieldValidate(Method method, Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return true;
        }
        List<ConstraintViolation<Object>> error = Arrays.stream(args)
                .filter(Objects::nonNull)
                .map(validator::validate)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(error)) {
            return true;
        }

        List<String> errorMessage = error.stream().map(i -> i.getPropertyPath() + " " + i.getMessage()).collect(Collectors.toList());
        log.warn("methodArgsFieldValidate failed. method={}, message={}", method.getName(), errorMessage);
        return false;
    }

    private boolean methodArgsValidate(Object validateParamService, Method method, Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return true;
        }
        ExecutableValidator executableValidator = validator.forExecutables();
        Set<ConstraintViolation<Object>> constraintViolationSet = executableValidator.validateParameters(validateParamService, method, args);
        if (CollectionUtils.isEmpty(constraintViolationSet)) {
            return true;
        }
        List<String> errorMessage = constraintViolationSet.stream().map(i -> i.getPropertyPath() + " " + i.getMessage()).collect(Collectors.toList());
        log.warn("methodArgsValidate failed. message={}", errorMessage);
        return false;
    }

}
