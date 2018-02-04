package com.kodefactory.tech.lib.email;

import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendHtmlMessage(String to, String subject, Context context, String template);
    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException;
}
