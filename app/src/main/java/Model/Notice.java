package Model;

public class Notice {
    private String id;
    private String messageReply;
    private String userId;
    private boolean isSeen;
    private boolean isNotify;

    public void setIsNotify(boolean notify) {
        isNotify = notify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(String messageReply) {
        this.messageReply = messageReply;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public Notice(String id, String messageReply, String userId, boolean isSeen) {
        this.id = id;
        this.messageReply = messageReply;
        this.userId = userId;
        this.isSeen = isSeen;
    }

    public Notice() {
    }

    public boolean getIsNotify() {
        return isNotify;
    }
}
