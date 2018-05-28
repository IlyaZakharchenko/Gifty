package ru.kpfu.itis.gifty.model.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.model.providers.UserProvider;

/**
 * Created by Ilya Zakharchenko on 11.05.2018.
 */
@IgnoreExtraProperties
public class Group {

    private List<Friend> friendList;
    private List<String> friendUidList;
    private String name;

    public Group() {

    }

    private void parceFriendList() {
        friendList = new ArrayList<>();
        if (friendUidList != null) {
            List<Friend> userFriends = UserProvider.getInstance().getUser().getFriendList();
            for (Friend friend : userFriends) {
                if (friendUidList.contains(friend.getUserId())) {
                    friendList.add(friend);
                }
            }
        }
    }

    public Group(String name) {
        friendUidList = new ArrayList<>();
        friendList = new ArrayList<>();
        this.name = name;
    }

    @Exclude
    public List<Friend> getFriendList() {
        parceFriendList();
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public List<String> getFriendUidList() {
        return friendUidList;
    }

    public void setFriendUidList(List<String> friendUidList) {
        this.friendUidList = friendUidList;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
