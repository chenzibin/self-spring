package self.spring.framework.web.servlet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * DispatcherServlet
 *
 * @author chenzb
 * @date 2019/11/25
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("hello");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init() throws ServletException {
        /*
        * 1、扫描scanPackage包路径
        * 2、实例化并缓存带有指定注解的类, 格式为：<beanName, classInstance>, 指定注解包括 Controller\Service\Repository\component
        * 3、实例化并缓存带有指定注解的方法，格式为：<url, methodInstance>, 指定注解包括 RequestMapping\PostMapping\GetMapping
        * 4、对于缓存的类, 注入带有指定注解的依赖类实例, 指定注解包括 Autowired
        *
        * */
        try(InputStream in = new FileInputStream("application.properties")) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = System.
    }

}
