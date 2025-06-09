package com.linkedin_microservices.connection_service.contorller;

import com.linkedin_microservices.connection_service.entity.Person;
import com.linkedin_microservices.connection_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections() {
        List<Person> firstDegreeConnections = connectionsService.getFirstDegreeConnections();
        return ResponseEntity.ok(firstDegreeConnections);
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionsRequest(@PathVariable Long userId) {
        boolean sentConnectionsRequest = connectionsService.sendConnectionsRequest(userId);
        return ResponseEntity.ok(sentConnectionsRequest);
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {
        Boolean acceptedConnectionRequest = connectionsService.acceptConnectionRequest(userId);
        return ResponseEntity.ok(acceptedConnectionRequest);
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {
        Boolean rejectedConnectionRequest = connectionsService.rejectConnectionRequest(userId);
        return ResponseEntity.ok(rejectedConnectionRequest);
    }
}
