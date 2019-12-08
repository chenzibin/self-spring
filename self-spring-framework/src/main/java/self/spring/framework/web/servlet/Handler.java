package self.spring.framework.web.servlet;

import self.spring.framework.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Handler {

    /**
     * 保存方法对应的实例
     */
    protected Object controller;

    /**
     * 方法
     */
    protected Method method;

    protected Pattern pattern;

    /**
     * 参数顺序
     */
    protected Map<String, Integer> paramIndexMapping;

    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        paramIndexMapping = new HashMap<>();
        putParamIndexMapping(method);
    }

    private void putParamIndexMapping(Method method) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestParam.class) && !parameters[i].getAnnotation(RequestParam.class).value().equals("")) {
                paramIndexMapping.put(parameters[i].getName(), i);
                continue;
            }
            if (parameters[i].getType().isAssignableFrom(HttpServletRequest.class) || parameters[i].getType().isAssignableFrom(HttpServletResponse.class)) {
                paramIndexMapping.put(parameters[i].getType().getName(), i);
                continue;
            }
        }
    }
}
