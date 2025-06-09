package com.linkedin_microservices.posts.service.events;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PostCreatedEvent {
    Long creatorId;
    String content;
    Long postId;
}
