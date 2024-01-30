package com.moviebookingapp.kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "moviebookingapp")
    public void listen(String message) {
        System.out.println("Received message: " + message);

    }

}
