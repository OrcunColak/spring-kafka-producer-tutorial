package com.colak.springtutorial.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TextProducerTest {

    @Autowired
    private TextProducer producer;

    @Test
    void testSendMessages() {
        producer.sendMessages(List.of("line1", "line2"));
    }
}