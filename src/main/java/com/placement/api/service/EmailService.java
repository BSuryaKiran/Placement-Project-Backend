package com.placement.api.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    private String getFromEmail() {
        return "NexStep Placement <postmaster@" + domain + ">";
    }


    public void sendEmail(String to, String subject, String text) {
        try {
            // Use .asString() instead of .asJson() to prevent crashes on non-JSON responses
            HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + domain + "/messages")
                    .basicAuth("api", apiKey)
                    .field("from", getFromEmail())
                    .field("to", to)
                    .field("subject", subject)
                    .field("text", text)
                    .field("h:Reply-To", "bkiran2006@outlook.com")
                    .asString();
            
            if (request.getStatus() != 200) {
                System.err.println("❌ MAILGUN ERROR: Status " + request.getStatus());
                System.err.println("❌ RAW RESPONSE: " + request.getBody());
            } else {
                System.out.println("✅ EMAIL SENT SUCCESSFULLY to: " + to);
                System.out.println("✅ MAILGUN RESPONSE: " + request.getBody());
            }
        } catch (UnirestException e) {
            System.err.println("❌ NETWORK ERROR sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendAcceptanceEmail(String studentEmail, String studentName, String jobTitle, String companyName, String recruiterName) {
        String subject = "Application Accepted - " + jobTitle;
        // Template as requested by user with ** for emphasis
        String text = String.format(
                "Dear %s,\n\n" +
                "Your application for **%s** at %s has been **accepted**.\n\n" +
                "We will contact you soon with the next steps.\n\n" +
                "Regards,\n" +
                "%s\n" +
                "%s",
                studentName, jobTitle, companyName, recruiterName, companyName);
        
        sendEmail(studentEmail, subject, text);
    }

    public void sendApplicationReceivedEmail(String studentEmail, String studentName, String jobTitle, String companyName, String recruiterName) {
        String subject = "Application Received - " + jobTitle;
        String text = String.format(
                "Dear %s,\n\n" +
                "Thank you for applying for **%s** at %s.\n\n" +
                "We have received your application and it is currently **Under Review**.\n\n" +
                "We will notify you once there is an update.\n\n" +
                "Regards,\n" +
                "%s\n" +
                "%s",
                studentName, jobTitle, companyName, recruiterName, companyName);
        
        sendEmail(studentEmail, subject, text);
    }

    public void sendRejectionEmail(String studentEmail, String studentName, String jobTitle, String companyName, String recruiterName) {
        String subject = "Application Update - " + jobTitle;
        // Template as requested by user with ** for emphasis
        String text = String.format(
                "Dear %s,\n\n" +
                "Thank you for applying for **%s** at %s.\n\n" +
                "We regret to inform you that your application was **not selected**.\n\n" +
                "We wish you the best for your future.\n\n" +
                "Regards,\n" +
                "%s\n" +
                "%s",
                studentName, jobTitle, companyName, recruiterName, companyName);
        
        sendEmail(studentEmail, subject, text);
    }
}
