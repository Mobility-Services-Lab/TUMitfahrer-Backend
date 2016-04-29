package de.tumitfahrer.dtos.message;


import javax.xml.bind.annotation.XmlElement;

public class MessageDTO {

    private String content;
    private String senderName;
    private String dateSent;

    @XmlElement(name = "content")
    public String getContent() {
        return content;
    }

    @XmlElement(name = "content")
    public void setContent(String content) {
        this.content = content;
    }

    @XmlElement(name = "sender_name")
    public String getSenderName() {
        return senderName;
    }

    @XmlElement(name = "sender_name")
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @XmlElement(name = "date_sent")
    public String getDateSent() {
        return dateSent;
    }

    @XmlElement(name = "date_sent")
    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }


}
