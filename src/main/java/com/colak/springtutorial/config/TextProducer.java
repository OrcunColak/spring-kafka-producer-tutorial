package com.colak.springtutorial.config;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;

@Component
@RequiredArgsConstructor
public class TextProducer {

    // Constants for topic configuration
    private final static int PARTITION_COUNT = 8;
    private final static String TOPIC = "TEXT-DATA";
    private final static short REPLICATION_FACTOR = 1;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    public void sendLines() {
        try (Stream<String> lines = Stream.of("line1", "line2")) {
            AtomicInteger counter = new AtomicInteger(0);
            lines.forEach(line -> sendTextMessage(line, counter.getAndIncrement()));
        }
    }

    private void sendTextMessage(String text, int lineIndex) {
        if (text == null || text.isEmpty()) {
            return;
        }
        // Sends the Link message to the topic, distributing across partitions based on the line index
        kafkaTemplate.send(TOPIC, STR."KEY-\{lineIndex % PARTITION_COUNT}", text);
    }

}