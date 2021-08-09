package Model;

public class Notice {
    private String id;
    private String message;
    private String messageReply;
    private String userId;
    private boolean isSeen;
    private boolean isReply;
    private boolean isNotify;

    public Notice(String id, String message, String messageReply, String userId, boolean isSeen, boolean isReply, boolean isNotify) {
        this.id = id;
        this.message = message;
        this.messageReply = messageReply;
        this.userId = userId;
        this.isSeen = isSeen;
        this.isReply = isReply;
        this.isNotify = isNotify;
    }

    public boolean getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(boolean isNotify) {
        this.isNotify = isNotify;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean getIsReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public Notice() {
    }


}
