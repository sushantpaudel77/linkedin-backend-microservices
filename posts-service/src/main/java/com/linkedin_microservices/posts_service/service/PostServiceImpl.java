package com.linkedin_microservices.posts_service.service;

import com.linkedin_microservices.posts_service.auth.UserContextHolder;
import com.linkedin_microservices.posts_service.clients.ConnectionsClient;
import com.linkedin_microservices.posts_service.dto.PostCreateRequestDto;
import com.linkedin_microservices.posts_service.dto.PostDto;
import com.linkedin_microservices.posts_service.entity.Post;
import com.linkedin_microservices.posts.service.events.PostCreatedEvent;
import com.linkedin_microservices.posts_service.exception.ResourceNotFoundException;
import com.linkedin_microservices.posts_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    @Override
    public PostDto createPost(PostCreateRequestDto createRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post newPost = modelMapper.map(createRequestDto, Post.class);
        newPost.setUserId(userId);

        Post savedPost = postRepository.save(newPost);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .creatorId(userId)
                .postId(savedPost.getId())
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with ID: {}", postId);

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with ID: " + postId));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> userPosts = postRepository.findByUserId(userId);
        return userPosts.stream()
                .map(element -> modelMapper.map(element, PostDto.class))
                .toList();
    }
}
