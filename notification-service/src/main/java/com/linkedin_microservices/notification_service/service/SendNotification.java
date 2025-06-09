package com.linkedin_microservices.notification_service.service;

public interface SendNotification {
    void send(Long userId, String message);
}
