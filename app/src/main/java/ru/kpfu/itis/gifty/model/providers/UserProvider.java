package ru.kpfu.itis.gifty.model.providers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.listeners.UserProviderOnCompleteListener;

/**
 * Created by Ilya Zakharchenko on 17.05.2018.
 */
public class UserProvider {

    private User user;

    private DocumentReference userDocRef;

    private CollectionReference friendsColRef;

    private UserProviderOnCompleteListener listener;

    private static UserProvider provider;

    public static UserProvider getInstance() {
        if (provider == null) {
            provider = new UserProvider();
        }
        return provider;
    }

    public UserProvider provideUser() {
        List<Friend> friends = new ArrayList<>();
        if (user == null) {
            String uid = FirebaseAuth.getInstance().getUid();
            userDocRef = FirebaseFirestore.getInstance().document("users/" + uid);
            friendsColRef = userDocRef.collection("friends");
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user = task.getResult().toObject(User.class);
                    friendsColRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task1.getResult()) {
                                friends.add(snapshot.toObject(Friend.class));
                            }
                            user.setFriendList(friends);
                        }
                    });
                }
                listener.onComplete(user, task.isSuccessful());
            });
        }
        else {
            listener.onComplete(user, true);
        }
        return provider;
    }

    public User getUser() {
        return user;
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

    public void updateName(String name) {
        user.setDisplayName(name);
        userDocRef.update("displayName", name);
    }

    public void addFriend(Friend friend) {
        List<Friend> friends = user.getFriendList();
        friends.add(friend);
        user.setFriendList(friends);
        friendsColRef.add(friend);
    }

    public void addOnCompleteListener(UserProviderOnCompleteListener listenerImpl) {
        listener = listenerImpl;
    }
}
