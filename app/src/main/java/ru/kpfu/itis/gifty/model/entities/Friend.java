package ru.kpfu.itis.gifty.model.entities;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Friend {

    private String displayName;
    private String userId;

    public Friend() {

    }

    public Friend(final String displayName,final String userId) {
        this.displayName = displayName;
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}
