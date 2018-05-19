package ru.kpfu.itis.gifty.model.entities;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Gift {

    private String id;
    private String name;
    private User checkedFriend;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public User getCheckedFriend() {
        return checkedFriend;
    }

    public void setCheckedFriend(User checkedFriend) {
        this.checkedFriend = checkedFriend;
    }

    public Gift() {

    }
}
