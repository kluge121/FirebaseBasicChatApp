package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

import java.util.Date;

/**
 * Created by baeminsu on 2018. 1. 15..
 */

public class ChatInfo {
    private String chatId;
    private String title;
    private Date createDate;
    private boolean disabled;
    private int totalUserCount;
    private int totalUnReadCount;
    private String lastMessage;


    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {

        return lastMessage;
    }

    public void setTotalUnReadCount(int totalUnReadCount) {
        this.totalUnReadCount = totalUnReadCount;
    }

    public int getTotalUnReadCount() {

        return totalUnReadCount;
    }


    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setTotalUserCount(int totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public String getChatId() {

        return chatId;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public int getTotalUserCount() {
        return totalUserCount;
    }


    public boolean equals(ChatInfo obj) {
        return chatId.equals(obj.getChatId());
    }
}
