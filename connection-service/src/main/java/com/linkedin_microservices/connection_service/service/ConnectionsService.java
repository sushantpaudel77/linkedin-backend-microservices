package com.linkedin_microservices.connection_service.service;

import com.linkedin_microservices.connection_service.entity.Person;

import java.util.List;

public interface ConnectionsService {
    List<Person> getFirstDegreeConnections();

    boolean sendConnectionsRequest(Long receiverId);

    Boolean acceptConnectionRequest(Long senderId);

    Boolean rejectConnectionRequest(Long senderId);
}
