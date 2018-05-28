package ru.kpfu.itis.gifty.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toolbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.FriendsGiftAdapter;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class FriendsGiftListFragment extends Fragment {

    private static final String KEY_INDEX = "Index";
    private List<Gift> giftList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FriendsGiftAdapter adapter;
    private User user;

    public static FriendsGiftListFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(KEY_INDEX, index);
        FriendsGiftListFragment fragment = new FriendsGiftListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giftList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_friends, container, false);
        initViews(v);
        initFields();
        return v;
    }

    private void initFields() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar.setVisibility(View.VISIBLE);
        loadUser();
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        progressBar = v.findViewById(R.id.progress_bar);
        toolbar = v.findViewById(R.id.toolbar);
    }

    private void loadUser() {
        User currentUser = UserProvider.getInstance().getUser();
        String uid = currentUser.getFriendList().get(getArguments().getInt(KEY_INDEX)).getUserId();
        DocumentReference userDocRef = FirebaseFirestore.getInstance().document("users/" + uid);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = task.getResult().toObject(User.class);
                List<Friend> friends = new ArrayList<>();
                userDocRef.collection("friends").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        for (DocumentSnapshot snapshot : task1.getResult()) {
                            friends.add(snapshot.toObject(Friend.class));
                        }
                        List<Group> groups = new ArrayList<>();
                        userDocRef.collection("groups").get().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                for (DocumentSnapshot doc : task2.getResult()) {
                                    Group group = doc.toObject(Group.class);
                                    groups.add(group);
                                }
                                List<Gift> gifts = new ArrayList<>();
                                userDocRef.collection("gifts").get().addOnCompleteListener(task3 -> {
                                    if (task3.isSuccessful()) {
                                        for (DocumentSnapshot doc : task3.getResult()) {
                                            Gift gift = doc.toObject(Gift.class);
                                            gifts.add(gift);
                                        }
                                        for (Gift gift : user.getGiftList()) {
                                            for (Group group : parceGroups(gift.getGroupsId())) {
                                                if (group.getFriendUidList().contains(currentUser.getUid())) {
                                                    giftList.add(gift);
                                                    break;
                                                }
                                            }
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        adapter = new FriendsGiftAdapter(giftList, user.getUid());
                                        recyclerView.setAdapter(adapter);
                                        toolbar.setTitle(user.getDisplayName() + getString(R.string.friends_gift_list));
                                    }
                                });
                                user.setGiftList(gifts);
                            }
                        });
                        user.setGroupList(groups);
                    }
                });
                user.setFriendList(friends);
            }
        });
    }

    private List<Group> parceGroups(List<String> groupsId) {
        List<Group> groupList = new ArrayList<>();
        if (groupsId != null) {
            List<Group> groups = user.getGroupList();
            for (Group group : groups) {
                if (groupsId.contains(group.getName())) {
                    groupList.add(group);
                }
            }
        }
        return groupList;
    }
}
