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

    private String displayName;
    private List<Friend> friendList;
    private List<Gift> giftList;
    private List<Group> groupList;
    private String uid;

    public User(String uid, String name) {
        this.uid = uid;
        displayName = name;
        friendList = new ArrayList<>();
    }

    public User(String uid) {
        this(uid, null);
    }

    public User() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    @Exclude
    public List<Friend> getFriendList() {
        return friendList;
    }

    @Exclude
    public void setFriendList(final List<Friend> friendList) {
        this.friendList = friendList;
    }

    @Exclude
    public List<Gift> getGiftList() {
        return giftList;
    }

    @Exclude
    public void setGiftList(List<Gift> giftList) {
        this.giftList = giftList;
    }

    @Exclude
    public List<Group> getGroupList() {
        return groupList;
    }

    @Exclude
    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
