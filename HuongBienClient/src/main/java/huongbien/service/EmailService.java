package huongbien.service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class EmailService {

    private final String username;
    private final String appPassword;

    public EmailService(String username, String appPassword) {
        this.username = username;
        this.appPassword = appPassword;
    }

    public static boolean sendEmailWithOTP(String recipientEmail, String otp, String username, String appPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mã OTP của bạn");
            String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; text-align: center;">
                        <img src="cid:bannerImage" alt="Nhà Hàng Hương Biển" style="max-width: 800px; margin-bottom: 20px;" />
                        <h2 style="color: #2c3e50;">Xin chào!</h2>
                        <p>Cảm ơn bạn đã sử dụng dịch vụ của <b>Nhà Hàng Hương Biển</b>.</p>
                        <p>Mã OTP để khôi phục mật khẩu của bạn là:</p>
                        <h3 style="color: #e74c3c;">%s</h3>
                        <p style="color: #34495e;">Nếu có bất kỳ câu hỏi nào, xin vui lòng liên hệ với chúng tôi qua email hoặc số điện thoại được cung cấp.</p>
                        <p style="margin-top: 20px;">Trân trọng,<br><b>Nhà Hàng Hương Biển</b></p>
                    </body>
                    </html>
                    """.formatted(otp);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeBodyPart imagePart = new MimeBodyPart();
            ClassLoader classLoader = EmailService.class.getClassLoader();
            URL bannerUrl = classLoader.getResource("com/huongbien/img/banner/banner.png");

            if (bannerUrl == null) {
                throw new IOException("Không tìm thấy file banner.png");
            }

            File bannerFile = new File(bannerUrl.getFile());
            DataSource fds = new FileDataSource(bannerFile);
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", "<bannerImage>");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email OTP đã được gửi thành công!");
            return true;

        } catch (MessagingException | IOException e) {
            System.err.println("Lỗi khi gửi email OTP: " + e.getMessage());
            return false;
        }
    }

    public static void sendEmailWithReservation(String to, String subject, String content, String username, String appPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmailWithQRCode(String recipientEmail, String attachmentPath) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mã QR Code Nhà Hàng Hương Biển");

            String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; text-align: center;">
                        <img src="cid:bannerImage" alt="Nhà Hàng Hương Biển" style="max-width: 800px; margin-bottom: 20px;" />
                        <h2 style="color: #2c3e50;">Xin chào!</h2>
                        <p>Cảm ơn bạn đã sử dụng dịch vụ của <b>Nhà Hàng Hương Biển</b>.</p>
                        <p>Mã QR Code của bạn đã được tạo thành công. Vui lòng tải về hoặc lưu lại mã QR này để sử dụng khi đến nhà hàng.</p>
                        <p style="color: #34495e;">Nếu có bất kỳ câu hỏi nào, xin vui lòng liên hệ với chúng tôi qua email hoặc số điện thoại được cung cấp.</p>
                        <p style="margin-top: 20px;">Trân trọng,<br><b>Nhà Hàng Hương Biển</b></p>
                    </body>
                    </html>
                    """;

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeBodyPart imagePart = new MimeBodyPart();

            ClassLoader classLoader = getClass().getClassLoader();
            URL bannerUrl = classLoader.getResource("com/huongbien/img/banner/banner.png");

            if (bannerUrl == null) {
                throw new IOException("Không tìm thấy file banner.png");
            }

            File bannerFile = new File(bannerUrl.getFile());
            DataSource fds = new FileDataSource(bannerFile);
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", "<bannerImage>");

            MimeBodyPart qrPart = new MimeBodyPart();
            qrPart.attachFile(attachmentPath);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(imagePart);
            multipart.addBodyPart(qrPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email đã được gửi thành công!");
            return true;

        } catch (MessagingException | IOException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
            return false;
        }
    }
}