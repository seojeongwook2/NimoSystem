package kr.ac.knu.nimosystem.Model;

import java.io.Serializable;

public class MessageItem implements Serializable {
    private String messageId;
    private String threadId;
    private String address;
    private String contactId;
    private String contactId_string;
    private String timestamp;
    private String body;

    public MessageItem() { }

    public MessageItem(String messageId, String threadId, String address, String contactId, String contactId_string, String timestamp, String body) {
        this.messageId = messageId;
        this.threadId = threadId;
        this.address = address;
        this.contactId = contactId;
        this.contactId_string = contactId_string;
        this.timestamp = timestamp;
        this.body = body;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId_string() {
        return contactId_string;
    }

    public void setContactId_string(String contactId_string) {
        this.contactId_string = contactId_string;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
