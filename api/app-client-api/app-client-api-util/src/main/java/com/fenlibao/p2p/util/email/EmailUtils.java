package com.fenlibao.p2p.util.email;

import com.fenlibao.p2p.util.loader.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

	private static final Logger logger=LogManager.getLogger(EmailUtils.class);
	
	/** 
     * 发送邮件 
     */
	public static void sendEmail(String subject,String email,String content) {  
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);  
        try {  
            message.setSubject(subject);  
            message.setSentDate(new Date());  
            message.setFrom(new InternetAddress(Sender.get("mail.send.from")));
            message.setRecipient(RecipientType.TO, new InternetAddress(email));  
            
            message.setContent(content,"text/html;charset=utf-8");  
            // 发送邮件  
            Transport transport = session.getTransport("smtp");
            transport.connect(Sender.get("mail.smtp"), Sender.get("mail.send.from"), Sender.get("mail.send.password"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {  
        	logger.error(email+" send email error......");
        	logger.error(e.getMessage(),e);
        }  
    }
	
	
	public static Session getSession() {  
        Properties props = new Properties();  
        props.setProperty("mail.transport.protocol", "smtp");  
        props.setProperty("mail.smtp.host", Sender.get("mail.smtp"));
        props.setProperty("mail.smtp.port", Sender.get("mail.port"));
        props.setProperty("mail.smtp.auth", "true");  
        
        Session session = Session.getInstance(props);
        return session;  
    }  
}
