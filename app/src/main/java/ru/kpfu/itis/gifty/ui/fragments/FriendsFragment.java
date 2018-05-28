package ru.kpfu.itis.gifty.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.FriendListAdapter;
import ru.kpfu.itis.gifty.ui.adapters.UserListAdapter;

public class FriendsFragment extends Fragment {

    private User currentUser;
    private FirebaseFirestore db;
    private TextView emptyTextView;
    private List<Friend> friendList;
    private FriendListAdapter friendListAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<User> userList;
    private UserListAdapter usersAdapter;

    public static FriendsFragment newInstance() {
        Bundle args = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        currentUser = UserProvider.getInstance().getUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        initViews(v);
        friendList = UserProvider.getInstance().getUser().getFriendList();
        initListeners();
        initFields();
        setFriendsView();
        return v;
    }

    private void initFields() {
        LayoutManager manager = new LinearLayoutManager(getActivity());
        userList = new ArrayList<>();
        friendListAdapter = new FriendListAdapter(friendList, getActivity());
        usersAdapter = new UserListAdapter(userList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(friendListAdapter);
    }

    private void initListeners() {
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.isEmpty()) {
                    setFriendsView();
                } else {
                    setUsersView();
                    progressBar.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    String textStart = newText.substring(0, newText.length() - 1);
                    char textEnd = (char) (newText.charAt(newText.length() - 1) + 1);
                    String textFinal = textStart + textEnd;
                    db.collection("users")
                            .whereGreaterThanOrEqualTo("displayName", newText)
                            .whereLessThan("displayName", textFinal)
                            .get()
                            .addOnCompleteListener(task -> {
                                userList.clear();
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        User user = document.toObject(User.class);
                                        boolean isFriend = false;
                                        for (Friend friend : friendList) {
                                            if (friend.getUserId().equals(user.getUid())) {
                                                isFriend = true;
                                                break;
                                            }
                                        }
                                        if (!user.getUid().equals(currentUser.getUid()) && !isFriend) {
                                            userList.add(document.toObject(User.class));
                                        }
                                    }
                                } else {
                                    Snackbar.make(recyclerView, getString(R.string.error_loading_error),
                                            Snackbar.LENGTH_LONG).show();
                                }
                                usersAdapter.notifyDataSetChanged();
                            });
                }
                return true;
            }
        });
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        searchView = v.findViewById(R.id.search_view);
        progressBar = v.findViewById(R.id.progress_bar);
        emptyTextView = v.findViewById(R.id.tv_empty);
    }

    private void setFriendsView() {
        friendListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(friendListAdapter);
        if (!friendList.isEmpty()) {
            emptyTextView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(getString(R.string.no_friends));
        }
    }

    private void setUsersView() {
        usersAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(usersAdapter);
        if (!userList.isEmpty()) {
            emptyTextView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_users);
        }
    }
}
