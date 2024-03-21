package RestAPI.Util.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Properties;

public class SMTPMail {

    private final String sender = "bitnetsupp@gmail.com";
    private final String password = "tcyc saie aemh jajb";

    private final Properties properties;

    public SMTPMail() {
        properties = new Properties();

        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.user", sender);
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
    }

    public synchronized HashMap<Integer, String> sendEMail(String receiver, String subject, String body) {
        HashMap<Integer, String> result = new HashMap<>();

        Integer httpResponse = 404;
        String textResponse = "";

        try {
            if (!receiver.contains("@")) {
                result.put(400, "Cannot sent the email because the provided address is invalid!");
            } else {
                Session session = Session.getInstance(properties, new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }

                });

                MimeMessage message = new MimeMessage(session);

                message.setFrom(new InternetAddress(sender));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
                message.setSubject(subject);
                message.setContent(body, "text/html; charset=utf-8");

                Transport transport = session.getTransport("smtp");
                transport.connect(sender, password);
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                transport.close();

                result.put(200, "The email was sent to the following address: " + receiver);
            }
        } catch (Exception e) {
            result.put(500, "An error has been detected! please check logs!");
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: sendEMail: " + e);
        }

        result.put(httpResponse, textResponse);
        return result;
    }
}
