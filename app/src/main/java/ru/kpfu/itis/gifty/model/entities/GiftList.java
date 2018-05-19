package ru.kpfu.itis.gifty.model.entities;

import java.util.List;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class GiftList {

    private String id;
    private User owner;
    private String name;
    private List<Gift> giftList;
    private List<Group> groupList;

    public GiftList() {

    }

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Gift> getGiftList() {
        return giftList;
    }

    public void setGiftList(final List<Gift> giftList) {
        this.giftList = giftList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(final List<Group> groupList) {
        this.groupList = groupList;
    }
}
