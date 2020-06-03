package com.bon.email;

import com.bon.logger.Logger;
import com.bon.util.StringUtils;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@SuppressWarnings("ALL")
public class EmailSentAuthenticator extends javax.mail.Authenticator {
    private static final String TAG = EmailSentAuthenticator.class.getSimpleName();

    private String user = "", pass = "", from = "", port = "",
            sport = "", host = "", subject = "", body = "";
    private String[] to = null;
    private boolean auth = false, debuggable = false, starttls = false;
    private Multipart multipart = null;

    /**
     * instance
     *
     * @param user
     * @param pass
     * @return
     */
    public static EmailSentAuthenticator getInstance(String user, String pass) {
        return new EmailSentAuthenticator(user, pass);
    }

    /**
     * @param user
     * @param pass
     */
    public EmailSentAuthenticator(String user, String pass) {
        try {
            this.user = user;
            this.pass = pass;
            this.debuggable = true; // debug mode on or off - default off
            this.auth = true; // smtp authentication - default on
            this.starttls = true; // start tls
            this.multipart = new MimeMultipart();
            this.setMailCapCommandMap();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * attach file in email
     *
     * @param filename
     * @throws Exception
     */
    public EmailSentAuthenticator addAttachment(String filename) {
        try {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            this.multipart.addBodyPart(messageBodyPart);
        } catch (MessagingException e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass);
    }

    /**
     * get properties of email
     *
     * @return
     */
    private Properties setProperties() {
        Properties props = new Properties();
        try {
            props.setProperty("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", host);
            if (debuggable) props.put("mail.debug", "true");
            if (auth) props.put("mail.smtp.auth", "true");
            if (starttls) props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.socketFactory.port", sport);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return props;
    }

    /**
     * There is something wrong with MailCap, javamail can not find a
     * handler for the multipart/mixed part, so this bit needs to be added.
     */
    private void setMailCapCommandMap() {
        try {
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap
                    .getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    // the getters and setters
    public EmailSentAuthenticator setBody(String _body) {
        this.body = _body;
        return this;
    }

    public EmailSentAuthenticator setTo(String[] _to) {
        this.to = _to;
        return this;
    }

    public EmailSentAuthenticator setFrom(String _from) {
        this.from = _from;
        return this;
    }

    public EmailSentAuthenticator setSubject(String _subject) {
        this.subject = _subject;
        return this;
    }

    public EmailSentAuthenticator setPort(String _port) {
        this.port = _port;
        return this;
    }

    public EmailSentAuthenticator setSport(String sport) {
        this.sport = sport;
        return this;
    }

    public EmailSentAuthenticator setHost(String _host) {
        this.host = _host;
        return this;
    }

    /**
     * send email
     *
     * @return
     * @throws Exception
     */
    public boolean send() throws Exception {
        Properties props = setProperties();
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(pass) && to.length > 0
                && !StringUtils.isEmpty(from) && !StringUtils.isEmpty(subject)
                && !StringUtils.isEmpty(body)) {
            Session session = Session.getDefaultInstance(props, this);
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));

            InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) addressTo[i] = new InternetAddress(to[i]);

            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            msg.setContent(multipart);

            // send email
            Transport.send(msg);
            return true;
        } else {
            return false;
        }
    }
}
