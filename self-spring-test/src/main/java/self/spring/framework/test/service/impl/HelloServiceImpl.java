package self.spring.framework.test.service.impl;

import self.spring.framework.annotation.Service;
import self.spring.framework.test.service.api.HelloService;

/**
 * HelloServiceImpl
 *
 * @author chenzb
 * @date 2019/11/27
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello() {
        return "hello, world!";
    }
}
