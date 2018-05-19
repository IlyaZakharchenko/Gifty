package ru.kpfu.itis.gifty.model.entities;

import java.util.List;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Group {

    private String id;
    private List<User> friendList;
    private String name;

    public Group() {

    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(final List<User> friendList) {
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
