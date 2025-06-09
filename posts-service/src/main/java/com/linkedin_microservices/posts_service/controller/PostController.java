package com.linkedin_microservices.posts_service.controller;

import com.linkedin_microservices.posts_service.auth.UserContextHolder;
import com.linkedin_microservices.posts_service.dto.PostCreateRequestDto;
import com.linkedin_microservices.posts_service.dto.PostDto;
import com.linkedin_microservices.posts_service.service.PostService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/core")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto createRequestDto) {
        PostDto createdPost = postService.createPost(createRequestDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto postById = postService.getPostById(postId);
        return ResponseEntity.ok(postById);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostOfUser(@PathVariable Long userId) {
        List<PostDto> allPostOfUser = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(allPostOfUser);
    }
}
