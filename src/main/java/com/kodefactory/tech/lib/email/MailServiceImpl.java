package com.kodefactory.tech.lib.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Service
public class MailServiceImpl implements MailService {

    Logger logger = LoggerFactory.getLogger(MailServiceImpl.class.getName());

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;


    /**
     * Send a simple message
     * @param to
     * @param subject
     * @param text
     */
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


    /**
     * Send HTML based message
     * @param to
     * @param subject
     * @param context
     * @param template
     */
    @Override
    public void sendHtmlMessage(String from, String to, String subject, String template, Context context) {
        MimeMessage mail = emailSender.createMimeMessage();
        String body = templateEngine.process(template, context);
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mail, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Send message with attachment
     * @param to
     * @param subject
     * @param text
     * @param pathToAttachment
     * @throws MessagingException
     */
    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Attachment", file);

        emailSender.send(message);
    }
}
