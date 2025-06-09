package com.linkedin_microservices.notification_service.consumer;

import com.linkedin_microservices.connection_service.event.AcceptConnectionRequestEvent;
import com.linkedin_microservices.connection_service.event.SendConnectionRequestEvent;
import com.linkedin_microservices.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent event) {

        log.info("handle connections: handleSendConnectionRequest: {}", event);
        if (event == null || event.getSenderId() == null || event.getReceiverId() == null) {
            log.warn("Received an invalid connection request event: {}", event);
            return;
        }
        String message = String.format(
                "You have received a connection request from user with ID: %s",
                event.getSenderId()
        );
        log.info("Sending connection notification to receiverId: {}", event.getReceiverId());
        sendNotification.send(event.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent event) {

        log.info("handle connections: handleAcceptConnectionRequest: {}", event);
        if (event == null || event.getSenderId() == null || event.getReceiverId() == null) {
            log.warn("Received invalid accept-connection-request event: {}", event);
            return;
        }
        String message = String.format(
                "Your connection request has been accepted by the user with ID: %s",
                event.getReceiverId()
        );
        log.info("Sending connection acceptance notification to senderId: {}", event.getSenderId());
        sendNotification.send(event.getSenderId(), message);
    }

}
