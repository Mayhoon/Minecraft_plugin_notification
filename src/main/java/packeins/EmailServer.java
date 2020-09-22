package packeins;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailServer {

    private Message message;


    public EmailServer () {
        String sender = "statusBot_mc@gmx.de";
        String password = "server_status";
        String receiver = "emailmaxhoff@gmx.de";

        Properties properties = new Properties();

        properties.put("mail.smtp.ssl.trust", "mail.gmx.net");
        properties.put("mail.smtp.host", "mail.gmx.net");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.user", sender);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.transport.protocol", "smtp");

        try {


            Session mailSession = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(properties.getProperty("mail.smtp.user"),
                            properties.getProperty("mail.smtp.password"));
                }
            });

             message = new MimeMessage(mailSession);
            InternetAddress addressTo = new InternetAddress(receiver);
            message.setRecipient(Message.RecipientType.TO, addressTo);
            message.setFrom(new InternetAddress(sender));


        } catch (Exception e) {

        }

    }

    public void sendMail(String  subject, String content) throws MessagingException {
        message.setSubject(subject);
        message.setContent(content, "text/plain");
        Transport.send(message);
    }
}
