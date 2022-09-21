package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemPaymentRepositoryTest {

    InMemPaymentRepository paymentRepository;

    Payment payment1 = new Payment(1, 1D, "");
    Payment payment2 = new Payment(1, 10D, "");
    Payment payment3 = new Payment(1, 100D, "");

    @BeforeEach
    void setUp() {
        paymentRepository = new InMemPaymentRepository();
    }

    @Test
    void findById_throwsExceptionWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> paymentRepository.findById(null));
    }

    @Test
    void findById_shouldFindCorrectPayment(){
        paymentRepository.save(payment1);
        assertEquals(Optional.of(payment1), paymentRepository.findById(payment1.getPaymentId()));
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenPaymentIsNotPresent(){
        assertEquals(Optional.empty(), paymentRepository.findById(UUID.randomUUID()));
    }

    @Test
    void findAll_shouldReturnAllPayments() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        List<Payment> expectedList = Arrays.asList(payment1, payment2, payment3);
        List<Payment> actualList = paymentRepository.findAll();

        assertTrue((actualList.containsAll(expectedList)));
        assertTrue((expectedList.containsAll(actualList)));
    }

    @Test
    void save_shouldThrowExceptionWithNullPayment() {
        assertThrows(IllegalArgumentException.class, () -> paymentRepository.save(null));
    }

    @Test
    void save_shouldThrowExceptionWithAlreadySavedPayment() {
        paymentRepository.save(payment1);

        assertThrows(IllegalArgumentException.class, () -> paymentRepository.save(payment1));
    }

    @Test
    void findById_shouldSaveCorrectPayment(){
        assertEquals(payment1, paymentRepository.save(payment1));
    }


    @Test
    void editMessage_shouldThrowExceptionWithNullId() {
        assertThrows(NoSuchElementException.class, () -> paymentRepository.editMessage(null, "new message"));
    }

    @Test
    void editMessage_shouldEditSavedPayment() {
        paymentRepository.save(payment1);
        String newMessage = "new message";

        Payment actualPayment = paymentRepository.editMessage(payment1.getPaymentId(), newMessage);
        assertEquals(newMessage, actualPayment.getMessage());
    }
}
