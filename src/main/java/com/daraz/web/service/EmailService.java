package com.daraz.web.service;

public interface EmailService {
    void sendWelcomeEmail(String toEmail, String name);
    void sendPasswordResetEmail(String toEmail, String resetLinkOrOtp);
    void sendProductDiscountPromotionEmail(String toEmail, String productName, String originalPrice, String discountedPrice, String discountPercentage);
    void sendWishlistReminderEmail(String toEmail, String productName, String price);
}
