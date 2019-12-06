package self.spring.framework.test.controller;

import self.spring.framework.annotation.Autowired;
import self.spring.framework.annotation.Controller;
import self.spring.framework.annotation.RequestMapping;
import self.spring.framework.annotation.RequestParam;
import self.spring.framework.test.service.api.HelloService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * HelloController
 *
 * @author chenzb
 * @date 2019/11/27
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/say")
    public Object sayHello(@RequestParam("name") String name, HttpServletRequest req, ServletResponse resp) {
        return helloService.hello(name);
    }
}
