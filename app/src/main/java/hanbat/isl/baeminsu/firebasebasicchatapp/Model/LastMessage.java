package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

/**
 * Created by baeminsu on 2018. 1. 29..
 */

public class LastMessage {

    private String messgeContent;
    private Message.MessageType messageType;
    private String userEmail;


    public LastMessage() {
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {

        return userEmail;
    }

    public void setMessageType(Message.MessageType messageType) {
        this.messageType = messageType;
    }

    public Message.MessageType getMessageType() {

        return messageType;
    }

    public void setMessgeContent(String messgeContent) {
        this.messgeContent = messgeContent;
    }

    public String getMessgeContent() {

        if (this.getMessageType() == Message.MessageType.TEXT) {
            return messgeContent;
        } else {
            return "사진";
        }
    }
}
