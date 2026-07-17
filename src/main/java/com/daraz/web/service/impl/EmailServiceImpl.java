package com.daraz.web.service.impl;

import com.daraz.web.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private static final String SENDER_NAME = "Daraz Online Shopping";
    private static final String SENDER_EMAIL = "noreply@daraz.lk";

    @Async
    @Override
    public void sendWelcomeEmail(String toEmail, String name) {
        String subject = "Welcome to Daraz!";
        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>" +
                "    <div style='background-color: #f57224; padding: 20px; text-align: center;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 24px;'>Welcome to Daraz</h1>" +
                "    </div>" +
                "    <div style='padding: 30px; color: #333333; line-height: 1.6;'>" +
                "      <p style='font-size: 16px;'>Hi " + name + ",</p>" +
                "      <p>Thank you for creating an account with Daraz, the leading online shopping platform. We are thrilled to have you with us!</p>" +
                "      <p>Explore millions of products, enjoy amazing daily deals, and experience fast shipping right to your doorstep.</p>" +
                "      <div style='text-align: center; margin: 30px 0;'>" +
                "        <a href='https://www.daraz.lk' style='background-color: #f57224; color: #ffffff; padding: 12px 30px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;'>Start Shopping Now</a>" +
                "      </div>" +
                "      <p>If you have any questions, our 24/7 customer support team is always here to help.</p>" +
                "      <br/>" +
                "      <p>Happy Shopping!</p>" +
                "      <p><strong>The Daraz Team</strong></p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 15px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      This is an automated email, please do not reply directly. © 2026 Daraz Group." +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        sendHtmlEmail(toEmail, subject, content);
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLinkOrOtp) {
        String subject = "Reset Your Password - Daraz";
        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>" +
                "    <div style='background-color: #212121; padding: 20px; text-align: center;'>" +
                "      <h1 style='color: #f57224; margin: 0; font-size: 24px;'>Password Reset Request</h1>" +
                "    </div>" +
                "    <div style='padding: 30px; color: #333333; line-height: 1.6;'>" +
                "      <p style='font-size: 16px;'>Hello,</p>" +
                "      <p>We received a request to reset your password for your Daraz account. Use the verification code/reset link below to create a new password:</p>" +
                "      <div style='text-align: center; margin: 30px 0;'>" +
                "        <span style='background-color: #f1f1f1; border: 1px dashed #f57224; padding: 15px 30px; font-size: 22px; font-weight: bold; letter-spacing: 3px; color: #212121; border-radius: 4px; display: inline-block;'>" + resetLinkOrOtp + "</span>" +
                "      </div>" +
                "      <p>For security, this code is only valid for 15 minutes. If you did not make this request, you can safely ignore this email.</p>" +
                "      <br/>" +
                "      <p>Thanks,</p>" +
                "      <p><strong>The Daraz Security Team</strong></p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 15px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      Need help? Contact support immediately. © 2026 Daraz Group." +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        sendHtmlEmail(toEmail, subject, content);
    }

    @Async
    @Override
    public void sendProductDiscountPromotionEmail(String toEmail, String productName, String originalPrice, String discountedPrice, String discountPercentage) {
        String subject = "Special Discount: " + discountPercentage + "% OFF on " + productName + "!";
        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>" +
                "    <div style='background-color: #f57224; padding: 20px; text-align: center;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 24px;'>Hot Deal Just For You!</h1>" +
                "    </div>" +
                "    <div style='padding: 30px; color: #333333; line-height: 1.6;'>" +
                "      <p style='font-size: 16px;'>Dear Customer,</p>" +
                "      <p>We are excited to share a special paid promotion. A product you might love is now on sale with an amazing discount!</p>" +
                "      <div style='border: 1px solid #eeeeee; border-radius: 8px; padding: 20px; margin: 20px 0; background-color: #fafafa; display: flex; align-items: center; justify-content: space-between;'>" +
                "        <div>" +
                "          <h3 style='margin: 0 0 10px 0; color: #212121;'>" + productName + "</h3>" +
                "          <p style='margin: 0; font-size: 14px; color: #777777;'>Original Price: <del>Rs. " + originalPrice + "</del></p>" +
                "          <p style='margin: 5px 0 0 0; font-size: 18px; color: #f57224; font-weight: bold;'>Deal Price: Rs. " + discountedPrice + "</p>" +
                "        </div>" +
                "        <div style='background-color: #f57224; color: #ffffff; padding: 10px 15px; border-radius: 50%; font-weight: bold; font-size: 16px;'>" +
                "          -" + discountPercentage + "%" +
                "        </div>" +
                "      </div>" +
                "      <div style='text-align: center; margin: 30px 0;'>" +
                "        <a href='https://www.daraz.lk' style='background-color: #f57224; color: #ffffff; padding: 12px 30px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;'>View Deal Details</a>" +
                "      </div>" +
                "      <p>Hurry! This promotion is valid for a limited time or until stock runs out.</p>" +
                "      <br/>" +
                "      <p>Cheers,</p>" +
                "      <p><strong>Daraz Marketing</strong></p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 15px; text-align: center; font-size: 11px; color: #999999;'>" +
                "      You are receiving this promotion as a valued customer of Daraz. If you'd like to unsubscribe from marketing emails, update your preferences in the app." +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        sendHtmlEmail(toEmail, subject, content);
    }

    @Async
    @Override
    public void sendWishlistReminderEmail(String toEmail, String productName, String price) {
        String subject = "An Item in Your Wishlist is Waiting For You!";
        String content = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>" +
                "    <div style='background-color: #3b5998; padding: 20px; text-align: center;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 24px;'>Wishlist Reminder</h1>" +
                "    </div>" +
                "    <div style='padding: 30px; color: #333333; line-height: 1.6;'>" +
                "      <p style='font-size: 16px;'>Hi there,</p>" +
                "      <p>Remember this? You saved this item to your wishlist. It is still available and waiting for you to make it yours!</p>" +
                "      <div style='border: 1px solid #eeeeee; border-radius: 8px; padding: 20px; margin: 20px 0; background-color: #fafafa;'>" +
                "        <h3 style='margin: 0 0 10px 0; color: #3b5998;'>" + productName + "</h3>" +
                "        <p style='margin: 0; font-size: 16px; color: #f57224; font-weight: bold;'>Price: Rs. " + price + "</p>" +
                "      </div>" +
                "      <div style='text-align: center; margin: 30px 0;'>" +
                "        <a href='https://www.daraz.lk' style='background-color: #f57224; color: #ffffff; padding: 12px 30px; text-decoration: none; border-radius: 4px; font-weight: bold; display: inline-block;'>Add to Cart Now</a>" +
                "      </div>" +
                "      <br/>" +
                "      <p>Happy Shopping,</p>" +
                "      <p><strong>The Daraz Team</strong></p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 15px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      © 2026 Daraz Group. All rights reserved." +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        sendHtmlEmail(toEmail, subject, content);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(SENDER_EMAIL, SENDER_NAME);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Successfully sent email to {} with subject: {}", toEmail, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {} with subject: {}. Error: {}", toEmail, subject, e.getMessage(), e);
        }
    }
}
