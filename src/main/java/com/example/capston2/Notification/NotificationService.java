package com.example.capston2.Notification;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false = plain text, true = HTML

            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }



    public void sendRequestNotification(String designerEmail, String requestInfo, String designerName) {
        String subject = "New Design Request Received";
        String body = "Hello Designer " + designerName + ", \n\n"
                + "You have received a new design request.\n\n"
                + "Request Details:\n"
                + requestInfo + "\n\n"
                + "Please log in to Tasmeem Hub to review and respond.\n\n"
                + "Best regards,\n"
                + "Tasmeem Hub Team";

        sendEmail(designerEmail, subject, body);
    }

    public void sendDeadlineReminder(String designerEmail, String orderInfo) {
        String subject = "Deadline Reminder – Draft Due Today";
        String body = "Hello Designer,\n\n"
                + "This is a reminder that the deadline for one of your orders is today.\n\n"
                + "Order Details:\n"
                + orderInfo + "\n\n"
                + "If you have not submitted the draft yet, please upload it as soon as possible.\n\n"
                + "Best regards,\n"
                + "Tasmeem Hub Team";

        sendEmail(designerEmail, subject, body);
    }


    public void sendDraftNotification(String clientEmail, String clientName) {
        String subject = "Your Design Draft Is Ready for Review";
        String body = "Hello " + clientName + ", \n\n"
                + "Your designer has submitted the draft for your project.\n\n"
                + "Please log in to Tasmeem Hub to review and approve or request changes.\n\n"
                + "Best regards,\n"
                + "Tasmeem Hub Team";

        sendEmail(clientEmail, subject, body);
    }

}
