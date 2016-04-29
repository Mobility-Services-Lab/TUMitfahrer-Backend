/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.services;

import de.tumitfahrer.enums.Language;
import de.tumitfahrer.enums.MailFrom;
import de.tumitfahrer.util.LanguageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service("mailService")
public class MailService {

    @Autowired
    LanguageUtils languageUtils;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.user}")
    private String username;
    @Value("${mail.pass}")
    private String password;
    private Properties props;
    @Value("${environment}")
    private String env;

    public void init() {
        props = new Properties();
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);
    }

    private boolean mail(String to, MailFrom from, String subject, String content, String type) {
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from.getMailFrom()));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            if (env.equals("development")) {
                message.setSubject(subject + " (" + env + ")");
            } else {
                message.setSubject(subject);
            }

            // Send the actual HTML message, as big as you like
            message.setContent(content, type);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean forgotPassword(String to, String first_name, String password, Language lng) {
        String content = languageUtils.getTranslation(lng, "mail.forgot_pw_content");
        String subject = languageUtils.getTranslation(lng, "mail.forgot_pw_subject");

        content = content.replace("$$first_name$$", first_name);
        content = content.replace("$$email$$", to);
        content = content.replace("$$password$$", password);

        return mail(to, MailFrom.INFO, subject, content, "text/plain");
    }

    public boolean welcomeMail(String to, String first_name, String password, Language lng) {
        String content = languageUtils.getTranslation(lng, "mail.welcome_content");
        String subject = languageUtils.getTranslation(lng, "mail.welcome_subject");

        content = content.replace("$$first_name$$", first_name);
        content = content.replace("$$email$$", to);
        content = content.replace("$$password$$", password);

        return mail(to, MailFrom.INFO, subject, content, "text/plain");
    }

    public boolean contactMail(String from, String first_name, String title, String message, Language lng) {
        String content = languageUtils.getTranslation(lng, "mail.contact_content");
        String subject = languageUtils.getTranslation(lng, "mail.contact_subject");

        content = content.replace("$$first_name$$", first_name);
        content = content.replace("$$email$$", from);
        content = content.replace("$$message$$", message);
        content = content.replace("$$title$$", title);
        return mail(MailFrom.INFO.getMailFrom(), MailFrom.INFO, subject, content, "text/plain");
    }
}
