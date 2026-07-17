package com.daraz.web.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendWelcomeEmail() throws Exception {
        emailService.sendWelcomeEmail("test@example.com", "John Doe");

        // Wait a tiny bit since email sending is asynchronous (@Async)
        Thread.sleep(200);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendPasswordResetEmail() throws Exception {
        emailService.sendPasswordResetEmail("reset@example.com", "123456");

        Thread.sleep(200);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendProductDiscountPromotionEmail() throws Exception {
        emailService.sendProductDiscountPromotionEmail(
                "promo@example.com",
                "Smartphone X",
                "100000",
                "90000",
                "10"
        );

        Thread.sleep(200);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendWishlistReminderEmail() throws Exception {
        emailService.sendWishlistReminderEmail("wishlist@example.com", "Laptop Pro", "250000");

        Thread.sleep(200);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
