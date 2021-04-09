package suai.tests.common.api;

import android.media.Image;

import java.util.Date;

public class ChatClass {
    private String name;
    private Integer photo;
    private Date lastMessage;

    public  ChatClass(String name, Integer photo, Date lastMessage)
    {
        this.name = name;
        this.photo = photo;
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPhoto() {
        return photo;
    }

    public void setPhoto(Integer photo) {
        this.photo = photo;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }
}
