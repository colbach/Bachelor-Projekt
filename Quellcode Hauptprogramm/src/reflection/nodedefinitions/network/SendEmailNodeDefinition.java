package reflection.nodedefinitions.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import logging.AdditionalLogger;
import reflection.*;
import reflection.customdatatypes.rawdata.RawData;
import reflection.customdatatypes.rawdata.RawDataFromFile;

public class SendEmailNodeDefinition implements NodeDefinition {

    @Override
    public int getInletCount() {
        return 8;
    }

    @Override
    public Class getClassForInlet(int index) {
        switch (index) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            case 6:
                return String.class;
            case 7:
                return RawData.class;
            default:
                return null;
        }
    }

    @Override
    public String getNameForInlet(int index) {
        switch (index) {
            case 0:
                return "SMTP";
            case 1:
                return "SMTP-Port";
            case 2:
                return "Absender";
            case 3:
                return "Passwort";
            case 4:
                return "Empfänger";
            case 5:
                return "Subject";
            case 6:
                return "Text";
            case 7:
                return "Anhang";
            default:
                return null;
        }
    }

    @Override
    public boolean isInletForArray(int index) {
        if (index == 7) {
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInletEngaged(int index) {
        return false;
    }

    @Override
    public int getOutletCount() {
        return 0;
    }

    @Override
    public Class getClassForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public String getNameForOutlet(int index) {
        switch (index) {
            default:
                return null;
        }
    }

    @Override
    public boolean isOutletForArray(int index) {
        switch (index) {
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Email senden";
    }

    @Override
    public String getDescription() {
        return "" + TAG_PREAMBLE + "";
    }

    @Override
    public String getUniqueName() {
        return "buildin.SendEmail";
    }

    @Override
    public String getIconName() {
        return "Envelope_30px.png";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void run(InOut io, API api) throws Exception {

        String smtp = (String) io.in0(0, "mail.febas.net");
        Integer smtpport = (Integer) io.in0(1, 465);
        String absender = (String) io.in0(2, "mustermann@dapotato.de");
        String passwort = (String) io.in0(3, "maximilian1?");
        String empfnger = (String) io.in0(4, "spam@dapotato.de");
        String subject = (String) io.in0(5, "Unbenannt");
        String text = (String) io.in0(6, "");
        //Object[] anhang = io.in(7, new Object[0]);
        RawData anhang = (RawData) io.in0(7, null);

        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.socketFactory.port", smtpport.toString());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpport.toString());

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(absender, passwort);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(absender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfnger));
        message.setSubject(subject);

        if (anhang == null) { // ohne Anhang...
            message.setText(text);

        } else { // mit Anhang...
            
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(text);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(anhang.getData(), "text/plain");
            
            messageBodyPart.setDataHandler(new DataHandler(source));
            String fileName = "Anhang";
            if(anhang instanceof RawDataFromFile) {
                fileName = ((RawDataFromFile) anhang).getFileName();
            }
            
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart );

        }

        Transport.send(message);
        AdditionalLogger.out.println("Email von " + absender + " nach " + empfnger + " gesendet.");
    }

}
