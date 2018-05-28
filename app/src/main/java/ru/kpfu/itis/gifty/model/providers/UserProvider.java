package ru.kpfu.itis.gifty.model.providers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.listeners.UserProviderOnCompleteListener;

/**
 * Created by Ilya Zakharchenko on 17.05.2018.
 */
public class UserProvider {

    private static UserProvider provider;
    private CollectionReference friendsColRef;
    private CollectionReference groupsColRef;
    private CollectionReference giftsColRef;
    private DocumentReference userDocRef;
    private FirebaseFirestore db;
    private UserProviderOnCompleteListener innerListener;
    private UserProviderOnCompleteListener listener;
    private User user;

    public static UserProvider getInstance() {
        if (provider == null) {
            provider = new UserProvider();
        }
        return provider;
    }

    public void addFriend(Friend friend) {
        List<Friend> friends = user.getFriendList();
        friends.add(friend);
        friendsColRef.document(friend.getUserId()).set(friend);
        FirebaseFirestore.getInstance().document("users/" + friend.getUserId() + "/friends/" + user.getUid())
                .set(new Friend(user.getDisplayName(), user.getUid()));
    }

    public void addGift(Gift gift) {
        List<Gift> giftList = user.getGiftList();
        giftList.add(gift);
        giftsColRef.document(gift.getName()).set(gift);
    }

    public void addGroup(Group group) {
        List<Group> groupList = user.getGroupList();
        groupList.add(group);
        groupsColRef.document(group.getName()).set(group);
    }

    public void addOnCompleteListener(UserProviderOnCompleteListener listenerImpl) {
        listener = listenerImpl;
    }

    public void clear() {
        user = null;
    }

    public void createUser(User newUser) {
        String uid = FirebaseAuth.getInstance().getUid();
        db = FirebaseFirestore.getInstance();
        userDocRef = db.document("users/" + uid);
        user = newUser;
        userDocRef.set(newUser);
    }

    public void deleteFriend(Friend friend) {
        List<Friend> friends = user.getFriendList();
        friends.remove(friend);
        friendsColRef.document(friend.getUserId()).delete();
        for (Group group : user.getGroupList()) {
            if (group.getFriendUidList().remove(friend.getUserId())) {
                updateGroups(group);
            }
        }
        db.document("users/" + friend.getUserId() + "/friends/" + user.getUid()).delete();
    }

    public void deleteGift(Gift gift) {
        List<Gift> giftList = user.getGiftList();
        giftList.remove(gift);
        giftsColRef.document(gift.getName()).delete();
    }

    public void deleteGroup(Group group) {
        List<Group> groupList = user.getGroupList();
        groupList.remove(group);
        groupsColRef.document(group.getName()).delete();
    }

    public User getUser() {
        return user;
    }

    public UserProvider provideUser() {
        if (user == null) {
            db = FirebaseFirestore.getInstance();
            String uid = FirebaseAuth.getInstance().getUid();
            userDocRef = db.document("users/" + uid);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user = task.getResult().toObject(User.class);
                    user.setFriendList(new ArrayList<>());
                    innerListener = (user -> {
                        innerListener = (user1 -> loadGifts());
                        loadGroups();
                    });
                    loadFriends();
                }
            });
        }
        else {
            listener.onComplete(user);
        }
        return getInstance();
    }

    public void updateGifts(Gift gift) {
        List<Gift> giftList = user.getGiftList();
        giftList.set(giftList.indexOf(gift), gift);
        giftsColRef.document(gift.getName()).set(gift);
    }

    public void updateGroups(Group group) {
        List<Group> groupList = user.getGroupList();
        groupList.set(groupList.indexOf(group), group);
        groupsColRef.document(group.getName()).set(group);
    }

    public void updateName(String name) {
        user.setDisplayName(name);
        WriteBatch batch = db.batch();
        for (Friend friend : user.getFriendList()) {
            String friendId = friend.getUserId();
            String uid = user.getUid();
            DocumentReference doc = db.document("users/" + friendId + "/friends/" + uid);
            batch.update(doc, "displayName", name);
        }
        batch.commit();
        userDocRef.update("displayName", name);
    }

    private void loadFriends() {
        List<Friend> friends = new ArrayList<>();
        friendsColRef = userDocRef.collection("friends");
        friendsColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot snapshot : task.getResult()) {
                    friends.add(snapshot.toObject(Friend.class));
                }
                innerListener.onComplete(user);
            }
        });
        user.setFriendList(friends);
    }

    private void loadGroups() {
        List<Group> groups = new ArrayList<>();
        groupsColRef = userDocRef.collection("groups");
        groupsColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    Group group = doc.toObject(Group.class);
                    groups.add(group);
                }
                innerListener.onComplete(user);
            }
        });
        user.setGroupList(groups);
    }

    private void loadGifts() {
        List<Gift> gifts = new ArrayList<>();
        giftsColRef = userDocRef.collection("gifts");
        giftsColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    Gift gift = doc.toObject(Gift.class);
                    gifts.add(gift);
                }
                listener.onComplete(user);
            }
        });
        user.setGiftList(gifts);
    }
}
