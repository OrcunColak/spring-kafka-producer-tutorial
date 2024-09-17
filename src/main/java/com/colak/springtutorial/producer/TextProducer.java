package com.colak.springtutorial.producer;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class TextProducer {

    // Constants for topic configuration
    private static final int PARTITION_COUNT = 8;
    private static final String TOPIC = "TEXT-DATA";
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        NewTopic newTopic = TopicBuilder.name(TOPIC)
                .partitions(PARTITION_COUNT)
                .build();

        kafkaAdmin.createOrModifyTopics(newTopic);
    }

    public void sendMessages(List<String> messages) {
        AtomicInteger counter = new AtomicInteger(0);
        messages.forEach(line -> {
            int lineIndex = counter.getAndIncrement();

            // Sends the message to the topic, distributing across partitions based on the line index
            String key = "KEY-" + (lineIndex % PARTITION_COUNT);
            kafkaTemplate.send(TOPIC, key, line);
        });
    }

    @Transactional
    public void sendMessageWithTransaction(String message) {
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaOperations.send(TOPIC, message);

            // Return any value to indicate transaction success
            return true;
        });
    }

}