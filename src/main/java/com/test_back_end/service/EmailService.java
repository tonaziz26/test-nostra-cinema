package com.test_back_end.service;

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${email.sender}")
    private String emailSender;

    @Value("${email.subject}")
    private String emailSubject;

    @Value("${email.template}")
    private String emailTemplate;

    private final Session session;

    public EmailService(Session session) {
        this.session = session;
    }

    @Async
    public void sendEmail(String email, int otp) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(emailSubject);
            String msg = String.format(emailTemplate, otp);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception ignored) {}
    }
}
