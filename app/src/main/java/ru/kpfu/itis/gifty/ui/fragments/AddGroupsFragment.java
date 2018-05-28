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
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.AddGroupsAdapter;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class AddGroupsFragment extends Fragment {

    private static final String KEY_POSITION = "POSITION";
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    public static AddGroupsFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        AddGroupsFragment fragment = new AddGroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_friends, container, false);
        initViews(v);
        initFields();
        return v;
    }

    @SuppressLint("NewApi")
    private void initFields() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Group> groups = new ArrayList<>();
        User user = UserProvider.getInstance().getUser();
        Gift gift = user.getGiftList().get(getArguments().getInt(KEY_POSITION));
        for (Group group : user.getGroupList()) {
            if (!gift.getGroupList().contains(group)) {
                groups.add(group);
            }
        }
        AddGroupsAdapter adapter = new AddGroupsAdapter(groups, getArguments().getInt(KEY_POSITION));
        recyclerView.setAdapter(adapter);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setTitle(R.string.add_groups);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        toolbar = view.findViewById(R.id.toolbar);
    }
}
