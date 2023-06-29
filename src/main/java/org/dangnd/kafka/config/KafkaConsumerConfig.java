package org.dangnd.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${messaging.kafka.address}")
    private String messagingAddress;

    @Value("${messaging.kafka.chatprogram.consumer.batch}")
    private boolean isBatchConsumerChatProgram;

    @Value("${messaging.kafka.chatprogram.consumer.number.of.message.in.batch}")
    private int maxBatchRecordChatProgram;

    @Value("${messaging.kafka.eventsourcing.consumer.batch}")
    private boolean isBatchConsumerEventSourcing;

    @Value("${messaging.kafka.eventsourcing.consumer.number.of.message.in.batch}")
    private int maxBatchRecordEventSourcing;

    @Value("${messaging.consumer.pool.size}")
    private int kafkaConsumerThreadPoolSize;

    @Value("${graceful.shutdown.messaging.consumer.wait.time.max}")
    private int waitTimeMax;

    @Value("${messaging.consumer.pool.thread.name.prefix}")
    private String threadNamePrefix;

    @Bean(name="kafka.consumer.thread.pool")
    ThreadPoolTaskExecutor messageProcessorExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(kafkaConsumerThreadPoolSize);
        exec.setMaxPoolSize(kafkaConsumerThreadPoolSize);
        exec.setAllowCoreThreadTimeOut(true);
        exec.setWaitForTasksToCompleteOnShutdown(true);
        exec.setAwaitTerminationSeconds(waitTimeMax);
        exec.setThreadNamePrefix(threadNamePrefix);
        //exec.setThreadFactory(ThreadFactoryFactory.defaultNamingFactory("kafka", "processor"));
        return exec;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                messagingAddress);
        props.put(
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                maxBatchRecordChatProgram);
        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                false);
//        props.put(
//                ConsumerConfig.DEFAULT_ISOLATION_LEVEL,
//                IsolationLevel.READ_COMMITTED);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(isBatchConsumerChatProgram);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setConsumerTaskExecutor(messageProcessorExecutor());
        return factory;
    }



    @Bean
    public ConsumerFactory<String, String> consumerFactoryEventSourcing() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                messagingAddress);
        props.put(
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                maxBatchRecordEventSourcing);
        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("eventSourcingKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> eventSourcingKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryEventSourcing());
        factory.setBatchListener(isBatchConsumerEventSourcing);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}