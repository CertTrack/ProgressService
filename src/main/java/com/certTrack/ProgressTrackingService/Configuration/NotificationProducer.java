package com.certTrack.ProgressTrackingService.Configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    public void sendNotification(Map<String, String> params) {
        kafkaTemplate.send("notification", params);
    }
}
