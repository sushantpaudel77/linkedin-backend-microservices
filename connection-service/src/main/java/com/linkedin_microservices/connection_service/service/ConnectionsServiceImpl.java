package com.linkedin_microservices.connection_service.service;

import com.linkedin_microservices.connection_service.auth.UserContextHolder;
import com.linkedin_microservices.connection_service.entity.Person;
import com.linkedin_microservices.connection_service.event.AcceptConnectionRequestEvent;
import com.linkedin_microservices.connection_service.event.SendConnectionRequestEvent;
import com.linkedin_microservices.connection_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

    @Transactional
    @Override
    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting fist degree connections for user with ID: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    @Override
    public boolean sendConnectionsRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Trying to send connection request, sender: {}, receiver: {}", senderId, receiverId);

        try {
            if (senderId.equals(receiverId)) {
                throw new IllegalStateException("Both sender and receiver are the same");
            }

            boolean alreadySendRequest = personRepository.connectionRequestExist(senderId, receiverId);
            if (alreadySendRequest) {
                throw new IllegalStateException("Connection request already exists");
            }

            boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
            if (alreadyConnected) {
                throw new IllegalStateException("Already connected users, cannot add new request");
            }

            personRepository.addConnectionRequest(senderId, receiverId);
            log.info("Successfully sent the connection request from {} to {}", senderId, receiverId);

            SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .build();

            sendConnectionRequestEventKafkaTemplate.send(
                    "send-connection-request-topic", sendConnectionRequestEvent);

            return true;

        } catch (IllegalStateException e) {
            log.warn("Connection request failed: {}", e.getMessage());
            throw e; // rethrow so upstream can handle this
        } catch (Exception e) {
            log.error("Unexpected error while sending connection request from {} to {}: {}", senderId, receiverId, e.getMessage(), e);
            throw new IllegalStateException("Failed to send connection request due to server error");
        }
    }


    @Override
    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExist = personRepository.connectionRequestExist(senderId, receiverId);
        if (!connectionRequestExist) {
            throw new IllegalStateException("No connection request exists to accept");
        }
        personRepository.acceptConnectionRequest(senderId, receiverId);

        log.info("Successfully accepted the connection request, sender: {}, receiver: {}", senderId, receiverId);
        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptConnectionRequestEventKafkaTemplate.send(
                "accept-connection-request-topic", acceptConnectionRequestEvent);

        return true;
    }

    @Override
    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExist = personRepository.connectionRequestExist(senderId, receiverId);
        if (!connectionRequestExist) {
            throw new IllegalStateException("No connection request exists, cannot delete");
        }
        personRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }
}
