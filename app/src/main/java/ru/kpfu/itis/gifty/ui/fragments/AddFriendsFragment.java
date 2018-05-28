package ru.kpfu.itis.gifty.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.AddFriendsAdapter;

/**
 * Created by Ilya Zakharchenko on 24.05.2018.
 */
public class AddFriendsFragment extends Fragment {

    private static final String KEY_POSITION = "POSITION";
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    public static AddFriendsFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        AddFriendsFragment fragment = new AddFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_friends, container, false);
        initViews(v);
        initFields();
        return v;
    }

    @SuppressLint("NewApi")
    private void initFields() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Friend> friends = new ArrayList<>();
        User user = UserProvider.getInstance().getUser();
        Group group = user.getGroupList().get(getArguments().getInt(KEY_POSITION));
        for (Friend friend : user.getFriendList()) {
            if (!group.getFriendList().contains(friend)) {
                friends.add(friend);
            }
        }
        AddFriendsAdapter adapter = new AddFriendsAdapter(friends, getArguments().getInt(KEY_POSITION));
        recyclerView.setAdapter(adapter);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        toolbar = view.findViewById(R.id.toolbar);
    }
}
