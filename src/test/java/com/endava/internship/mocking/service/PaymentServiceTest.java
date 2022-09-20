package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    ValidationService validationService;
    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createPayment_validationServiceCalledExpectedMethods() {
        Integer userId = 1;
        Double amount = 100.d;
        User user = new User(userId, "", Status.ACTIVE);
        doReturn(Optional.of(user)).when(userRepository).findById(eq(userId));

        paymentService.createPayment(userId, amount);

        verify(validationService, times(1)).validateUserId(eq(userId));
        verify(validationService, times(1)).validateAmount(eq(amount));
        verify(validationService, times(1)).validateUser(eq(user));
    }

    @Test
    void createPayment_throwsExceptionIfUserNotFound(){
        doReturn(Optional.empty()).when(userRepository).findById(any());

        assertThrows(NoSuchElementException.class, () -> paymentService.createPayment(1, 1.d));
    }

    @Test
    void createPayment_savesCorrectPayment(){
        Integer userId = 1;
        Double amount = 100.d;
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        User user = new User(userId, "", Status.ACTIVE);
        doReturn(Optional.of(user)).when(userRepository).findById(eq(userId));

        paymentService.createPayment(userId, amount);
        verify(paymentRepository).save(captor.capture());

        Payment actualPayment = captor.getValue();
        assertEquals(user.getId(), actualPayment.getUserId());
        assertEquals(amount, actualPayment.getAmount());
    }

    @Test
    void editMessage_validationServiceCalledExpectedMethods() {
        UUID paymentId = UUID.randomUUID();
        String newMessage = "new message";

        paymentService.editPaymentMessage(paymentId, newMessage);

        verify(validationService).validatePaymentId(eq(paymentId));
        verify(validationService).validateMessage(eq(newMessage));
    }

    @Test
    void editMessage_calledPaymentRepository(){
        UUID paymentId = UUID.randomUUID();
        String newMessage = "new message";

        paymentService.editPaymentMessage(paymentId, newMessage);

        verify(paymentRepository).editMessage(eq(paymentId), eq(newMessage));
    }

    @ParameterizedTest
    @MethodSource("providePaymentList")
    void getAllByAmountExceeding(List<Payment> paymentList) {
        Double amount = 100D;
        doReturn(paymentList).when(paymentRepository).findAll();

        List<Payment> actualList = paymentService.getAllByAmountExceeding(amount);

        //good practice or not?
        for (Payment payment : paymentList) {
            if (payment.getAmount() > amount){
                assertTrue(actualList.contains(payment));
            }
        }
    }

    static Stream<Arguments> providePaymentList(){
        Payment payment1 = new Payment(1, 1D, "message1");
        Payment payment2 = new Payment(1, 10D, "message2");
        Payment payment3 = new Payment(1, 100D, "message3");
        Payment payment4 = new Payment(1, 1000D, "message4");


        return Stream.of(
                Arguments.of(Arrays.asList(payment1, payment2, payment3)),
                Arguments.of(Arrays.asList(payment2, payment3, payment4))
        );
    }
}
