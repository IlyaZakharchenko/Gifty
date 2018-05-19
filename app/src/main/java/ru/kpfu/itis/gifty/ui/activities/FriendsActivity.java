package ru.kpfu.itis.gifty.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.View;
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

public class FriendsActivity extends BottomNavigationActivity {

    private FriendListAdapter friendListAdapter;
    private UserListAdapter usersAdapter;
    private FirebaseFirestore db;
    private List<Friend> friendList;
    private List<User> userList;
    private LayoutManager manager;
    private User currentUser;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        db = FirebaseFirestore.getInstance();
        currentUser =  UserProvider.getInstance().getUser();
        friendList = currentUser.getFriendList();
        initViews();
        initFields();
        initListeners();
        setFriendsView();
    }

    private void initFields() {
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        userList = new ArrayList<>();
        friendListAdapter = new FriendListAdapter(friendList);
        recyclerView.setAdapter(friendListAdapter);
        usersAdapter = new UserListAdapter(userList);
    }

    protected void initListeners() {
        navigation.setOnNavigationItemSelectedListener(navigationListener);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.isEmpty()) {
                    setFriendsView();
                } else {
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
                                setUsersView();
                            });
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }
        });
    }

    protected void initViews() {
        navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_friends);
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        progressBar = findViewById(R.id.progress_bar);
        emptyTextView = findViewById(R.id.tv_empty);
    }

    private void setFriendsView() {
        if (!friendList.isEmpty()) {
            friendListAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(friendListAdapter);
            emptyTextView.setVisibility(View.GONE);
        }
        else {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(getString(R.string.no_friends));
        }
    }

    private void setUsersView() {
        if (!userList.isEmpty()) {
            emptyTextView.setVisibility(View.GONE);
            usersAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(usersAdapter);
        }
        else {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_users);
        }
    }
}
