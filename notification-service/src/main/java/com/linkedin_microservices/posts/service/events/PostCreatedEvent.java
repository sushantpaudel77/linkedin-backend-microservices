package com.linkedin_microservices.posts.service.events;

import lombok.Data;

@Data
public class PostCreatedEvent {
    Long creatorId;
    String content;
    Long postId;
}
