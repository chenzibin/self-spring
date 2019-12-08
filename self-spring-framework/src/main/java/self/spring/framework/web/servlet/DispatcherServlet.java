package self.spring.framework.web.servlet;

import self.spring.framework.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * DispatcherServlet
 *
 * @author chenzb
 * @date 2019/11/25
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 保存application.properties配置文件的内容
     */
    private Properties contextConfig = new Properties();

    /**
     * 保存扫描的类名
     */
    private List<String> classNames = new ArrayList<>();

    /**
     * IOC容器，保存所有bean
     */
    private Map<String, Object> ioc = new HashMap<>();

    /**
     * 保存URL和Method的映射关系
     */
    private List<Handler> handlerMapping = new ArrayList();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req, resp);
        } catch (InvocationTargetException | IllegalAccessException e) {
            resp.getWriter().write("500");
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        String servletPath = req.getServletPath();
        Handler handler = handlerMapping.stream().filter(h -> h.pattern.matcher(servletPath).matches()).findFirst().orElse(null);
        if (handler == null) {
            resp.getWriter().write("404 Not Found");
        }

        Method method = handler.method;
        Object instance = handler.controller;

        Map<String, String[]> params = req.getParameterMap();
        Object[] invokeParams = Arrays.stream(method.getParameters())
                .map(parameter -> {
                    if (parameter.getType().isAssignableFrom(HttpServletRequest.class)) {
                        return req;
                    }
                    if (parameter.getType().isAssignableFrom(HttpServletResponse.class)) {
                        return resp;
                    }
                    if (parameter.isAnnotationPresent(RequestParam.class)) {
                        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                        String paramName = requestParam.value();
                        if (params.containsKey(paramName)) {
                            return params.get(paramName)[0];
                        }
                    }
                    return null;
                }).toArray();

        Object result = method.invoke(instance, (Object[]) invokeParams);
        if (result != null) {
            resp.getWriter().write(result.toString());
        }
    }

    @Override
    public void init() throws ServletException {
        // 1. 加载配置文件
        doLoadConfig();
        // 2. 扫描相关类
        doScanner(contextConfig.getProperty("scanPackage"));
        // 3. 初始化扫描到的类，并放入Ioc容器
        doInstance();
        // 4. 完成依赖注入
        doAutowired();
        // 5. 初始化HandlerMapping
        initHandlerMapping();
    }

    private void doLoadConfig() {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            contextConfig.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replace(".", "/"));
        assert url != null;
        File dir = new File(url.getFile());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                doScanner(String.format("%s.%s", scanPackage, file.getName()));
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = String.format("%s.%s", scanPackage, file.getName().replace(".class", ""));
                classNames.add(className);
            }
        }
    }

    private void doInstance() {
        try {
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    ioc.put(className, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = (Service) clazz.getAnnotation(Service.class);
                    String beanName = service.value();
                    if ("".equals(beanName)) {
                        beanName = clazz.getName();
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    for (Class classInterface : clazz.getInterfaces()) {
                        ioc.put(classInterface.getName(), instance);
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doAutowired() {
        try {
            for (Object object : ioc.values()) {
                if (object == null || !object.getClass().isAnnotationPresent(Controller.class)) {
                    continue;
                }
                Object instance = ioc.get(object.getClass().getName());
                for (Field field : object.getClass().getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Autowired.class)) {
                        continue;
                    }
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String serviceName = autowired.value();
                    if ("".equals(serviceName)) {
                        serviceName = field.getType().getName();
                    }
                    field.setAccessible(true);
                    field.set(instance, ioc.get(serviceName));
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initHandlerMapping() {
        for (Object object : ioc.values()) {
            Class clazz = object.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping classRequestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                    baseUrl = classRequestMapping.value();
                }
                for (Method method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                        String url = baseUrl + methodRequestMapping.value();
                        Pattern pattern = Pattern.compile(url);
                        handlerMapping.add(new Handler(object, method, pattern));
                    }
                }
            }
        }

    }
}
