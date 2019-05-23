package com.internship.insurance.service;

import com.internship.insurance.config.EmailConfig;
import com.internship.insurance.model.Mail;
import com.internship.insurance.model.Order;
import com.internship.insurance.model.OrderStatus;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Template;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {


    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration freemarkerConfig;



    public void sendEmail(Order order) throws Exception {
        Mail mail = new Mail();
        mail.setMailTo(order.getEmail());
        mail.setMailFrom("office@mvd-g.md");
        mail.setMailSubject(
                "Information about your " +
                        order.getInsurance().getTitle() +
                        " insurance order"
        );
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);

        Map<String, Object> model = new HashMap<>();
        model.put("id", order.getId());
        model.put("firstName", order.getFirstName());
        model.put("lastName", order.getLastName());
        model.put("insuranceTitle", order.getInsurance().getTitle());
        model.put("price", order.getPrice());
        model.put("status", order.getStatus());
        model.put("phoneNo", order.getPhoneNo());

        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        Template t;
        if(order.getStatus() == OrderStatus.PENDING) {
            t = freemarkerConfig.getTemplate("pendingOrder.html");
        }
        else {
            t = freemarkerConfig.getTemplate("order.html");
        }

        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);


        helper.setTo(mail.getMailTo());
        helper.setText(text, true);
        helper.setSubject(mail.getMailSubject());

        sender.send(message);
    }
}
