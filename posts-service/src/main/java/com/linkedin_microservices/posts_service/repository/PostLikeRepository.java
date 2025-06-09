package com.linkedin_microservices.posts_service.repository;

import com.linkedin_microservices.posts_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(long userId, Long postId);
}
