package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicValidationServiceTest {

    BasicValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new BasicValidationService();
    }

    @Test
    void validateAmount_sadPath() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(-1D))
        );
    }

    @Test
    void validateAmount_happyPath() {
        assertDoesNotThrow(() -> validationService.validateAmount(10D));
    }

    @Test
    void validatePaymentId_sadPath() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validatePaymentId(null));
    }

    @Test
    void validatePaymentId_happyPath() {
        assertDoesNotThrow(() -> validationService.validatePaymentId(UUID.randomUUID()));
    }

    @Test
    void validateUserId_sadPath() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateUserId(null));
    }
    @Test
    void validateUserId_happyPath() {
        assertDoesNotThrow(() -> validationService.validateUserId(1));
    }

    @Test
    void validateUser_sadPath() {
        User user = new User(1, "", Status.INACTIVE);
        assertThrows(IllegalArgumentException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateUser_happyPath() {
        User user = new User(1, "", Status.ACTIVE);
        assertDoesNotThrow(() -> validationService.validateUser(user));
    }

    @Test
    void validateMessage_sadPath() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateMessage(null));
    }

    @Test
    void validateMessage_happyPath() {
        assertDoesNotThrow(() -> validationService.validateMessage("message"));
    }
}
