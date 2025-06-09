package com.linkedin_microservices.notification_service.consumer;


import com.linkedin_microservices.notification_service.clients.ConnectionsClient;
import com.linkedin_microservices.notification_service.dto.PersonDto;
import com.linkedin_microservices.posts.service.events.PostCreatedEvent;
import com.linkedin_microservices.posts.service.events.PostLikedEvent;
import com.linkedin_microservices.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for (PersonDto connection : connections) {
            sendNotification.send(connection.getUserId(), "Your connection "
                    + postCreatedEvent.getCreatorId() + " has created a post, Check it out!");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notification: handlePostLiked: {}", postLikedEvent);

        String message = String.format("Your post, %s has been liked by %s",
                postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

        sendNotification.send(postLikedEvent.getCreatorId(), message);
    }
}