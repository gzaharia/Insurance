package com.internship.insurance.model;

import java.util.Map;

public class Mail {
    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailContent;
    private String contentType;
    private Map<String, Object> model;


    public Mail(String mailFrom, String mailTo, String mailSubject, String mailContent, String contentType, Map<String, Object> model) {
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;

        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
        this.contentType = contentType;
        this.model = model;
    }

    public Mail() {
        this.contentType = "text/plain";
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
