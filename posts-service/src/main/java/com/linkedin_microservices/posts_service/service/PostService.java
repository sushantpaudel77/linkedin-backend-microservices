package com.linkedin_microservices.posts_service.service;

import com.linkedin_microservices.posts_service.dto.PostCreateRequestDto;
import com.linkedin_microservices.posts_service.dto.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostCreateRequestDto createRequestDto);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsOfUser(Long userId);
}
