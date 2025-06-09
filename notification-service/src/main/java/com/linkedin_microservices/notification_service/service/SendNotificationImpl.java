package com.linkedin_microservices.notification_service.service;

import com.linkedin_microservices.notification_service.entity.Notification;
import com.linkedin_microservices.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationImpl implements SendNotification {

    private final NotificationRepository notificationRepository;

    @Override
    public void send(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);

        notificationRepository.save(notification);
        log.info("Notification saved for user: {}", userId);
    }
}
