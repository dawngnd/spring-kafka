package org.dangnd.kafka.service;


import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class MessagingService {
    @Autowired
    @Qualifier("chatprogramTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${messaging.kafka.producer.topic}")
    private String producerTopic;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    //	@Transactional
    public String postMessageToKafka(Map<String, String> body) {
        // Mandatory information for logging
        String uuid = UUID.randomUUID().toString();

        ThreadContext.put("serviceMessageId", uuid);

        String key = body.get("key");
        String content = body.get("content");
//        log.info("postMessageToKafka Input: " + uuid + content);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerTopic, key, content);
        try {
            SendResult<String, String> message = future.get();
            String offs = "" + message.getRecordMetadata().offset();
            String partition = "" + message.getRecordMetadata().partition();
            log.info("postMessageToKafka Output: OK with offset = " + offs + " partition = " + partition + " content = " + content);

            return offs;
        } catch (Exception e) {

            log.info("postMessageToKafka error " + e);
        }

        log.info("postMessageToKafka Output: ");
        return "";
    }


}
