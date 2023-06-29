package org.dangnd.kafka.asp;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspLog {

    @Before(value = "execution(* org.dangnd.kafka.service.TestAspService.*(..))")
    public void beforeMethodExecution() {
        System.out.println("Before method execution: Logging...");
    }

    @After("execution(* org.dangnd.kafka.service.TestAspService.*(..))")
    public void afterMethodExecution() {
        System.out.println("After method execution: Logging...");
    }

}
