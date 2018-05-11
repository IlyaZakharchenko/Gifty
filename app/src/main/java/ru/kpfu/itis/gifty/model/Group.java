package ru.kpfu.itis.gifty.model;

import java.util.List;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Group {

    private String id;
    private List<Friend> friendList;
    private String name;

    public Group() {

    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(final List<Friend> friendList) {
        this.friendList = friendList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
