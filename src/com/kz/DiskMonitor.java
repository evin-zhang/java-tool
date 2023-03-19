package com.kz;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import java.util.Date;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class DiskMonitor {

    public static void main(String[] args) throws Exception {
        // 设定阈值
        long threshold = 10L;

        // 邮件信息
        String subject = "磁盘使用量超过阈值警报";
        String content = "";
        // SMTP服务器信息
        String smtpHost = "smtp.126.com";
        int smtpPort = 587;

        // 发件人和收件人信息
        String from = "xxx@126.com";
        String password = "sssss";
        String to = "xxxxxxx";

        // 邮件发送相关配置
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        while (true) {
            // 执行df命令，获取磁盘使用情况
            Process p = new ProcessBuilder("df").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            long usage = 0L;
            long total = 0L;

            // 解析df命令的输出，获取磁盘使用情况
            while ((line = br.readLine()) != null) {
                if (line.contains("/dev/")) {
                    String[] parts = line.trim().split("\\s+");
                    usage += Long.parseLong(parts[4].replace("%", ""));
                    total += Long.parseLong(parts[1]);
                }
            }

            // 判断磁盘使用情况是否达到阈值
            if (usage >= threshold) {
                content = "磁盘使用量已达到" + usage + "%，总容量为" + total + " KB";
                // 发送邮件
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                    message.setSubject(subject);
                    message.setSentDate(new Date());
                    message.setText(content);
                    Transport.send(message);
                    System.out.println("邮件发送成功");
                } catch (MessagingException e) {
                    System.out.println("邮件发送失败：" + e.getMessage());
                }
            }

            // 每隔一段时间再次检查磁盘使用情况
            Thread.sleep(60000L); // 每隔一分钟检查一次
        }
    }
}
