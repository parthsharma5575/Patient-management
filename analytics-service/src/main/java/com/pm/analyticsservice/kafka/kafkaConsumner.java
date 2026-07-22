package com.pm.analyticsservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import patient.events.PatientEvent;

@Service
public class kafkaConsumner {
    private static final Logger log = LoggerFactory.getLogger(kafkaConsumner.class);

    @KafkaListener(topics = "patient", groupId = "analytics-group")
    public void consume(byte[] message) {
        try {
            PatientEvent event = PatientEvent.parseFrom(message);
            log.info("Consumed patient event: id={}, name={}, email={}, type={}",
                    event.getPatientId(), event.getName(), event.getEmail(), event.getEventType());
        } catch (Exception e) {
            log.error("Error parsing patient event", e);
        }
    }
}
