package ru.kpfu.itis.gifty.ui.fragments;

import static ru.kpfu.itis.gifty.utils.Consts.RC_GROUP;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.GroupListAdapter;
import ru.kpfu.itis.gifty.ui.dialogs.CreateGroupDialog;

public class GroupsFragment extends Fragment {

    private static final String TAG = "GroupsDialog";
    private GroupListAdapter adapter;
    private ImageButton addButton;
    private List<Group> groupList;
    private RecyclerView recyclerView;

    public static GroupsFragment newInstance() {
        Bundle args = new Bundle();
        GroupsFragment fragment = new GroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        initViews(v);
        groupList = UserProvider.getInstance().getUser().getGroupList();
        initListeners();
        initFields();
        updateList();
        return v;
    }

    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    private void initFields() {
        LayoutManager manager = new LinearLayoutManager(getActivity());
        adapter = new GroupListAdapter(groupList, getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initListeners() {
        addButton.setOnClickListener(v -> showGroupDialog());
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        addButton = v.findViewById(R.id.btn_add);
        addButton.setImageResource(R.drawable.ic_add);
    }

    private void showGroupDialog() {
        CreateGroupDialog groupDialog = CreateGroupDialog.newInstance();
        groupDialog.setTargetFragment(this, RC_GROUP);
        groupDialog.show(getFragmentManager(), TAG);
    }
}
