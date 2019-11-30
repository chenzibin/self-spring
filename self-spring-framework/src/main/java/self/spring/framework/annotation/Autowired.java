package self.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Autowired
 *
 * @author chenzb
 * @date 2019/11/27
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    String value() default "";
}
