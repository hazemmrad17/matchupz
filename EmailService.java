package services.user;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    public static void sendEmail(String toEmail, String subject, String body) {

        Properties properties = new Properties();


        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");



        String username = "contact.reefinity@gmail.com";
        String password = "ajsf exhz zxxa tsrx";


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);


            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {

            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
