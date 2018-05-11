package ru.kpfu.itis.gifty.model;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Gift {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Friend getCheckedFriend() {
        return checkedFriend;
    }

    public void setCheckedFriend(final Friend checkedFriend) {
        this.checkedFriend = checkedFriend;
    }

    public Gift() {

    }

    private Friend checkedFriend;
}
