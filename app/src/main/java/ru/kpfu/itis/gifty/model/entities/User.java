package ru.kpfu.itis.gifty.model.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya Zakharchenko on 12.05.2018.
 */
@IgnoreExtraProperties
public class User {

    private String uid;
    private String displayName;
    @Exclude
    private List<Friend> friendList;

    public User(String uid, String name) {
        this.uid = uid;
        displayName = name;
        friendList = new ArrayList<>();
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(final List<Friend> friendList) {
        this.friendList = friendList;
    }
}
