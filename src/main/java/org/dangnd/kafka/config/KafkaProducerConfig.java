package org.dangnd.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
public class KafkaProducerConfig {

    @Autowired
    Environment environment;

    @Value("${messaging.kafka.address}")
    private String messaginAddress;

    @Value("${spring.application.name}")
    private String transactionid;

    @Value("${kafka.procedure.deduplication}")
    private String procedureDeduplication;

    @Bean
    public ProducerFactory<String, String> producerChatProgramFactory() {

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, messaginAddress);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,transactionid + "-producer-" + localIp + "-");
//        config.put(ProducerConfig.RETRIES_CONFIG,10);
//        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, procedureDeduplication);

        //độ lớn tối đa của batch messages
        //config.put(ProducerConfig.BATCH_SIZE_CONFIG,"16384"); //default 16384

        //độ trễ cho lần gửi data lên broker tiếp theo
        //config.put(ProducerConfig.LINGER_MS_CONFIG,"0");  //default 0

        //độ lớn tối đa 1 request gửi lên broker
        //config.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,"1048576"); //default 1048576
        //String localIp =  Inet4Address.getLocalHost().getHostAddress();


        String localport = environment.getProperty("local.server.port");
        DefaultKafkaProducerFactory<String, String> def = new DefaultKafkaProducerFactory<>(config);
//        def.setTransactionIdPrefix(transactionid + "-producer-" + localIp + "-");
        return def;
    }

    @Bean(name = "chatprogramTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerChatProgramFactory());
    }

}