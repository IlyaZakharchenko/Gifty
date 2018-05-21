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
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.listeners.UserProviderOnCompleteListener;
import ru.kpfu.itis.gifty.ui.activities.SetNameActivity;

/**
 * Created by Ilya Zakharchenko on 17.05.2018.
 */
public class UserProvider {

    private static UserProvider provider;
    private CollectionReference friendsColRef;
    private UserProviderOnCompleteListener listener;
    private User user;
    private DocumentReference userDocRef;

    public static UserProvider getInstance() {
        if (provider == null) {
            provider = new UserProvider();
        }
        return provider;
    }

    public void addFriend(Friend friend) {
        List<Friend> friends = user.getFriendList();
        friends.add(friend);
        user.setFriendList(friends);
        friendsColRef.document(friend.getUserId()).set(friend);
    }

    public void addOnCompleteListener(UserProviderOnCompleteListener listenerImpl) {
        listener = listenerImpl;
    }

    public void clear() {
        user = null;
    }

    public void createUser(User newUser) {
        String uid = FirebaseAuth.getInstance().getUid();
        userDocRef = FirebaseFirestore.getInstance().document("users/" + uid);
        user = newUser;
        userDocRef.set(newUser);
    }

    public User getUser() {
        return user;
    }

    public UserProvider loadFriends() {
        List<Friend> friends = new ArrayList<>();
        friendsColRef = userDocRef.collection("friends");
        friendsColRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot snapshot : task.getResult()) {
                    friends.add(snapshot.toObject(Friend.class));
                }
                user.setFriendList(friends);
            }
            listener.onComplete(user);
        });
        user.setFriendList(friends);

        return getInstance();
    }

    public UserProvider provideUser() {
        if (user == null) {
            String uid = FirebaseAuth.getInstance().getUid();
            userDocRef = FirebaseFirestore.getInstance().document("users/" + uid);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user = task.getResult().toObject(User.class);
                    user.setFriendList(new ArrayList<>());
                }
                listener.onComplete(user);
            });
        } else {
            listener.onComplete(user);
        }
        return getInstance();
    }

    public void updateName(String name) {
        user.setDisplayName(name);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
}
