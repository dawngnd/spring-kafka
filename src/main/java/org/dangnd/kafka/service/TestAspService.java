package org.dangnd.kafka.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class TestAspService {
    public void execute() {
        log.info("Executing test");
    }
}
