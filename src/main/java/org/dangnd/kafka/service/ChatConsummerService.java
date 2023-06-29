package org.dangnd.kafka.service;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class ChatConsummerService {

    @KafkaListener(topics = "${messaging.kafka.chatprogram.consumer.topic}",
            groupId = "${messaging.kafka.chatprogram.consumer.groupid}",
            properties = {"key.deserializer=org.apache.kafka.common.serialization.StringDeserializer", "value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"}, concurrency = "${messaging.kafka.chatprogram.consumer.concurrent.thread}", containerFactory = "kafkaListenerContainerFactory")
    public void chatProgramListener(List<String> data, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions, Acknowledgment acknowledgment) {
        //Mandatory information for logging
        String _id = "consumer-" + UUID.randomUUID().toString();
        ThreadContext.put("serviceMessageId", _id);
        // commit ack to kafka server that all message has been processed


        int i = 0;
        for (String ms : data) {
            log.info("----------" + "Consumed message " + " size " + data.size() + "---------- " + Thread.currentThread().getName() + " partition " + partitions.get(0) + " data = " + ms);
//            log.info("Consumed message " + i + " : " + ms);
            i++;
        }


        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.info("kafka consumer error " + e.toString());
        }
        acknowledgment.acknowledge();
        log.info("---chatProgramListener done ----");
    }


}
