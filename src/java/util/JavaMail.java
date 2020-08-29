/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class JavaMail {
    
    private String mailSMTPServer;
    private String mailSMTPServerPort;

    public JavaMail() { //Para o GMAIL 
        mailSMTPServer = "smtp.live.com";
        mailSMTPServerPort = "465";
    }
    
    public JavaMail(String mailSMTPServer, String mailSMTPServerPort) { //Para outro Servidor
        this.mailSMTPServer = mailSMTPServer;
        this.mailSMTPServerPort = mailSMTPServerPort;
    }
    
    public void sendMail(String to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", "noreply.upmoney.ufpr@gmail.com");
        props.put("mail.smtp.password", "tccupmoney");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress("noreply.upmoney.ufpr@gmail.com"));
            message.setSubject(subject);
            message.setText(body, "utf-8", "html");
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport transport = session.getTransport("smtp");
            transport.connect(host, "noreply.upmoney.ufpr@gmail.com", "tccupmoney");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch (AddressException ae) {
            ae.printStackTrace();
        }catch (MessagingException me) {
            me.printStackTrace();
        }

    }
}