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
            message.setSubject("Mã OTP của bạn - Nhà Hàng Hương Biển");

            String htmlContent = """
                    <html>
                    <head>
                        <style>
                            @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
                            body {
                                font-family: 'Roboto', Arial, sans-serif;
                                line-height: 1.6;
                                color: #333333;
                                margin: 0;
                                padding: 0;
                                background-color: #f9f9f9;
                            }
                            .container {
                                max-width: 600px;
                                margin: 0 auto;
                                background-color: #ffffff;
                                border-radius: 8px;
                                overflow: hidden;
                                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
                            }
                            .header {
                                text-align: center;
                                padding: 20px 0;
                                background-color: #1e88e5;
                            }
                            .header img {
                                max-width: 100%%;
                                height: auto;
                            }
                            .content {
                                padding: 30px;
                                text-align: center;
                            }
                            h1 {
                                color: #1e88e5;
                                font-size: 24px;
                                margin-bottom: 20px;
                            }
                            p {
                                margin-bottom: 20px;
                                font-size: 16px;
                            }
                            .otp-container {
                                margin: 30px auto;
                                max-width: 320px;
                                padding: 5px;
                            }
                            .otp-box {
                                background-color: #f5f5f5;
                                border: 1px solid #e0e0e0;
                                border-radius: 8px;
                                padding: 20px;
                                position: relative;
                            }
                            .otp-code {
                                font-size: 32px;
                                font-weight: 700;
                                letter-spacing: 5px;
                                color: #1e88e5;
                                margin: 10px 0;
                            }
                            .copy-button {
                                display: block;
                                background-color: #FFFFFF;
                                color: white;
                                text-decoration: none;
                                padding: 10px 20px;
                                border-radius: 4px;
                                margin: 15px auto 5px;
                                font-weight: 500;
                                max-width: 150px;
                                text-align: center;
                                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                                transition: background-color 0.3s;
                            }
                            .footer {
                                text-align: center;
                                padding: 20px 30px;
                                background-color: #f5f5f5;
                                color: #757575;
                                font-size: 14px;
                            }
                            .highlight {
                                font-weight: 600;
                                color: #1e88e5;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <img src="cid:bannerImage" alt="Nhà Hàng Hương Biển" style="max-width: 550px;">
                            </div>

                            <div class="content">
                                <h1>Xin chào quý khách!</h1>
                                <p>Cảm ơn quý khách đã sử dụng dịch vụ của <span class="highlight">Nhà Hàng Hương Biển</span>.</p>
                                <p>Mã OTP để khôi phục mật khẩu của quý khách là:</p>

                                <div class="otp-container">
                                    <div class="otp-box">
                                        <div class="otp-code">%s</div>
                                        <a href="#" class="copy-button" onclick="navigator.clipboard.writeText('%s')">SAO CHÉP</a>
                                        <p style="font-size: 12px; margin-top: 10px; color: #757575;">
                                        </p>
                                    </div>
                                </div>

                                <p>Mã OTP này sẽ hết hạn sau 10 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>
                                <p>Nếu quý khách không yêu cầu mã OTP này, vui lòng bỏ qua email này hoặc liên hệ với chúng tôi.</p>
                            </div>

                            <div class="footer">
                                <p>Trân trọng,<br><b>Nhà Hàng Hương Biển</b></p>
                                <p>Địa chỉ: 12 Nguyễn Văn Bảo, Phường 1, Gò Vấp, TP. Hồ Chí Minh</p>
                                <p>Hotline: 0123 456 789 | Email: huongbienrestaurantcskh@gmail.com</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(otp, otp);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeBodyPart imagePart = new MimeBodyPart();
            ClassLoader classLoader = EmailService.class.getClassLoader();
            URL bannerUrl = classLoader.getResource("huongbien/img/banner/banner.png");

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

    public static void sendEmailWithReservation(String to, String subject, String content, String username,
            String appPassword) {
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
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            @import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600;700&display=swap');
                            
                            :root {
                                --primary: #0277bd;
                                --primary-light: #58a5f0;
                                --primary-dark: #004c8c;
                                --accent: #26a69a;
                                --text-on-light: #37474f;
                                --text-on-dark: #ffffff;
                                --background-light: #ffffff;
                                --background-gray: #f5f7fa;
                            }
                            
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }
                            
                            body {
                                font-family: 'Montserrat', Arial, sans-serif;
                                line-height: 1.6;
                                color: var(--text-on-light);
                                background-color: #e1f5fe;
                                margin: 0;
                                padding: 0;
                            }
                            
                            .email-wrapper {
                                max-width: 650px;
                                margin: 0 auto;
                                background: linear-gradient(180deg, #e3f2fd 0%, #ffffff 100%);
                                border-radius: 16px;
                                overflow: hidden;
                                box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
                                border: 1px solid #e0e0e0;
                            }
                            
                            .email-header {
                                position: relative;
                                text-align: center;
                                padding: 0;
                                background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 100%);
                            }
                            
                            .email-header img {
                                width: 100%;
                                max-width: 100%;
                                display: block;
                            }
                            
                            .wave-divider {
                                position: absolute;
                                bottom: -2px;
                                left: 0;
                                width: 100%;
                                overflow: hidden;
                                line-height: 0;
                            }
                            
                            .wave-divider svg {
                                display: block;
                                width: calc(100% + 1.3px);
                                height: 46px;
                            }
                            
                            .wave-divider .shape-fill {
                                fill: var(--background-light);
                            }
                            
                            .email-body {
                                padding: 40px 50px;
                                text-align: center;
                                background-color: var(--background-light);
                            }
                            
                            .greeting {
                                color: var(--primary);
                                font-size: 28px;
                                font-weight: 700;
                                margin-bottom: 25px;
                                letter-spacing: -0.5px;
                            }
                            
                            .message {
                                font-size: 16px;
                                line-height: 1.7;
                                margin-bottom: 25px;
                                color: var(--text-on-light);
                            }
                            
                            .highlight {
                                color: var(--primary);
                                font-weight: 600;
                            }
                            
                            .qr-container {
                                margin: 35px auto;
                                max-width: 340px;
                                position: relative;
                            }
                            
                            .qr-box {
                                background: var(--background-gray);
                                border-radius: 12px;
                                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
                                padding: 30px;
                                position: relative;
                                border-left: 4px solid var(--accent);
                            }
                            
                            .qr-label {
                                font-size: 14px;
                                font-weight: 600;
                                text-transform: uppercase;
                                color: var(--accent);
                                letter-spacing: 1.5px;
                                margin-bottom: 15px;
                            }
                            
                            .qr-frame {
                                background-color: white;
                                padding: 15px;
                                border-radius: 8px;
                                display: inline-block;
                                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
                                margin-bottom: 20px;
                                position: relative;
                                z-index: 1;
                            }
                            
                            .qr-frame::before {
                                content: '';
                                position: absolute;
                                top: -5px;
                                left: -5px;
                                right: -5px;
                                bottom: -5px;
                                background: linear-gradient(135deg, var(--primary) 0%, var(--accent) 100%);
                                z-index: -1;
                                border-radius: 10px;
                                opacity: 0.3;
                            }
                            
                            .info-box {
                                background-color: rgba(2, 119, 189, 0.05);
                                border-left: 3px solid var(--primary);
                                padding: 15px;
                                margin: 30px 0;
                                text-align: left;
                                border-radius: 4px;
                            }
                            
                            .info-title {
                                color: var(--primary);
                                font-weight: 600;
                                font-size: 15px;
                                margin-bottom: 5px;
                                display: flex;
                                align-items: center;
                                gap: 6px;
                            }
                            
                            .info-content {
                                font-size: 14px;
                                color: var(--text-on-light);
                                margin-left: 20px;
                                padding-left: 0;
                            }
                            
                            .info-content li {
                                margin-bottom: 6px;
                                list-style-type: none;
                                position: relative;
                            }
                            
                            .info-content li:before {
                                content: "•";
                                color: var(--primary);
                                font-weight: bold;
                                display: inline-block;
                                width: 1em;
                                margin-left: -1em;
                                position: absolute;
                                left: 0;
                            }
                            
                            .divider {
                                height: 1px;
                                background: linear-gradient(90deg, rgba(0,0,0,0) 0%, rgba(0,0,0,0.1) 50%, rgba(0,0,0,0) 100%);
                                margin: 30px 0;
                            }
                            
                            .email-footer {
                                background-color: var(--primary-dark);
                                color: var(--text-on-dark);
                                text-align: center;
                                padding: 30px;
                                border-bottom-left-radius: 16px;
                                border-bottom-right-radius: 16px;
                            }
                            
                            .footer-logo {
                                font-size: 20px;
                                font-weight: 700;
                                letter-spacing: 1px;
                                margin-bottom: 15px;
                                color: white;
                            }
                            
                            .footer-info {
                                font-size: 13px;
                                color: rgba(255, 255, 255, 0.8);
                                margin-bottom: 8px;
                            }
                            
                            .footer-contact {
                                margin-top: 15px;
                            }
                            
                            .contact-link {
                                display: inline-block;
                                color: white;
                                text-decoration: none;
                                margin: 0 10px;
                                font-size: 12px;
                                transition: all 0.2s;
                            }
                            
                            .contact-link:hover {
                                color: var(--accent);
                            }
                            
                            @media only screen and (max-width: 600px) {
                                .email-body {
                                    padding: 30px 20px;
                                }
                                
                                .greeting {
                                    font-size: 24px;
                                }
                                
                                .qr-box {
                                    padding: 20px;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-wrapper">
                            <div class="email-header">
                                <img src="cid:bannerImage" alt="Nhà Hàng Hương Biển">
                                <div class="wave-divider">
                                    <svg data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 120" preserveAspectRatio="none">
                                        <path d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z" opacity=".25" class="shape-fill"></path>
                                        <path d="M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z" opacity=".5" class="shape-fill"></path>
                                        <path d="M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84.23-20.12,127.61-26.46,59-8.63,112.48,12.24,165.56,35.4C827.93,77.22,886,95.24,951.2,90c86.53-7,172.46-45.71,248.8-84.81V0Z" class="shape-fill"></path>
                                    </svg>
                                </div>
                            </div>
                            
                            <div class="email-body">
                                <h1 class="greeting">Xin chào Quý khách</h1>
                                <p class="message">Cảm ơn quý khách đã sử dụng dịch vụ của <span class="highlight">Nhà Hàng Hương Biển</span>.</p>
                                
                                <div class="qr-container">
                                    <div class="qr-box">
                                        <div class="qr-label">Mã QR Code của bạn</div>
                                        <div class="qr-frame">
                                            <img src="cid:qrCodeImage" alt="QR Code" style="max-width: 200px; height: auto;">
                                        </div>
                                        <p style="font-size: 14px; margin-top: 10px; color: #505050;">
                                            Vui lòng lưu lại hoặc chụp ảnh màn hình để sử dụng khi đến nhà hàng
                                        </p>
                                    </div>
                                </div>
                                
                                <div class="info-box">
                                    <div class="info-title">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="#0277bd" stroke-width="2"/>
                                            <path d="M12 8V12" stroke="#0277bd" stroke-width="2" stroke-linecap="round"/>
                                            <path d="M12 16H12.01" stroke="#0277bd" stroke-width="2" stroke-linecap="round"/>
                                        </svg>
                                        Hướng dẫn sử dụng:
                                    </div>
                                    <ul class="info-content">
                                        <li>Xuất trình mã QR này khi đến nhà hàng để được hỗ trợ nhanh chóng</li>
                                        <li>Mã QR có thể được sử dụng để nhận các ưu đãi đặc biệt</li>
                                        <li>Mỗi mã QR chỉ được sử dụng một lần</li>
                                    </ul>
                                </div>
                                
                                <div class="divider"></div>
                                
                                <p class="message">Nếu bạn gặp khó khăn trong quá trình sử dụng dịch vụ, vui lòng liên hệ với chúng tôi qua số điện thoại <span class="highlight">0123 456 789</span> hoặc phản hồi email này.</p>
                            </div>
                            
                            <div class="email-footer">
                                <div class="footer-logo">NHÀ HÀNG HƯƠNG BIỂN</div>
                                <div class="footer-info">12 Nguyễn Văn Bảo, Phường 1, Gò Vấp, TP. Hồ Chí Minh</div>
                                <div class="footer-info">Email: huongbienrestaurantcskh@gmail.com | Hotline: 0123 456 789</div>
                                
                                <div class="footer-contact">
                                    <a href="#" class="contact-link">Trang chủ</a> |
                                    <a href="#" class="contact-link">Thực đơn</a> |
                                    <a href="#" class="contact-link">Đặt bàn</a> |
                                    <a href="#" class="contact-link">Liên hệ</a>
                                </div>
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html; charset=UTF-8");

            // Banner image
            MimeBodyPart bannerPart = new MimeBodyPart();
            ClassLoader classLoader = getClass().getClassLoader();
            URL bannerUrl = classLoader.getResource("huongbien/img/banner/banner.png");

            if (bannerUrl == null) {
                throw new IOException("Không tìm thấy file banner.png");
            }

            File bannerFile = new File(bannerUrl.getFile());
            DataSource bannerDS = new FileDataSource(bannerFile);
            bannerPart.setDataHandler(new DataHandler(bannerDS));
            bannerPart.setHeader("Content-ID", "<bannerImage>");
            bannerPart.setDisposition(MimeBodyPart.INLINE);

            MimeBodyPart qrPart = new MimeBodyPart();
            File qrFile = new File(attachmentPath);
            DataSource qrDS = new FileDataSource(qrFile);
            qrPart.setDataHandler(new DataHandler(qrDS));
            qrPart.setHeader("Content-ID", "<qrCodeImage>");
            qrPart.setDisposition(MimeBodyPart.INLINE);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachmentPath);
            attachmentPart.setFileName("QRCode-HuongBien.png");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(bannerPart);
            multipart.addBodyPart(qrPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email với QR Code đã được gửi thành công!");
            return true;

        } catch (MessagingException | IOException e) {
            System.err.println("Lỗi khi gửi email QR Code: " + e.getMessage());
            return false;
        }
    }
}