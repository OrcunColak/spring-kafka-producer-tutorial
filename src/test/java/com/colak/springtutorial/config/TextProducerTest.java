package com.colak.springtutorial.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TextProducerTest {

    @Autowired
    private TextProducer producer;

    @Test
    void testSendLines() {
        producer.sendLines();
    }


}