package com.safalifter.notificationservice.controller;

import com.safalifter.notificationservice.model.Notification;
import com.safalifter.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Notification endpoints")
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/getAllByUserId/{userId}")
    @Operation(summary = "Get notifications by user id")
    public ResponseEntity<List<Notification>> getAllByUserId(@PathVariable String userId) {
        log.info("Request received to fetch notifications for userId={}", userId);
        return ResponseEntity.ok(notificationService.getAllByUserId(userId));
    }
}