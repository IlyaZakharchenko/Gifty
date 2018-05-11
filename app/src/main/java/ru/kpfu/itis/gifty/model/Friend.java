package ru.kpfu.itis.gifty.model;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
public class Friend {

    private FirebaseUser friendUser;
    private String id;
    private FirebaseUser thisUser;

    public Friend() {
    }

    public FirebaseUser getFriendUser() {

        return friendUser;
    }

    public void setFriendUser(final FirebaseUser friendUser) {
        this.friendUser = friendUser;
    }

    public String getId() {
        return id;
    }

    public FirebaseUser getThisUser() {
        return thisUser;
    }

    public void setThisUser(final FirebaseUser thisUser) {
        this.thisUser = thisUser;
    }
}
