package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by baeminsu on 2018. 1. 15..
 */


//메세지 관련 부모 클래스
public class Message {

    private String messageId;
    private UserRef messageUser;
    private String chatId;
    private int unReadCount;
    private Date messageDate;
    private MessageType messageType;
    private List<String> readUserList;

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setReadUserList(List<String> readUserList) {
        this.readUserList = readUserList;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageUser(UserRef messageUser) {
        this.messageUser = messageUser;
    }

    public UserRef getMessageUser() {

        return messageUser;
    }

    public String getChatId() {
        return chatId;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public List<String> getReadUserList() {
        return readUserList;
    }

    public enum MessageType {
        TEXT, PHOTO;

    }

}
