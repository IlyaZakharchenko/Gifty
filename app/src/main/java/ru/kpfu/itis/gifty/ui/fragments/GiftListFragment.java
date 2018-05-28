package ru.kpfu.itis.gifty.ui.fragments;

import static ru.kpfu.itis.gifty.utils.Consts.RC_GIFT;

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
import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.GiftListAdapter;
import ru.kpfu.itis.gifty.ui.dialogs.CreateGiftDialog;

public class GiftListFragment extends Fragment {


    private static final String TAG = "GiftDialog";
    private GiftListAdapter adapter;
    private ImageButton addButton;
    private List<Gift> giftList;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    public static GiftListFragment newInstance() {
        Bundle args = new Bundle();
        GiftListFragment fragment = new GiftListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giftList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        initViews(v);
        giftList = UserProvider.getInstance().getUser().getGiftList();
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
        adapter = new GiftListAdapter(giftList, getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        toolbar.setTitle(R.string.title_gift_list);
    }

    private void initListeners() {
        addButton.setOnClickListener(v -> showGiftDialog());
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        addButton = v.findViewById(R.id.btn_add);
        addButton.setImageResource(R.drawable.ic_add);
        toolbar = v.findViewById(R.id.toolbar);
    }

    private void showGiftDialog() {
        CreateGiftDialog giftDialog = CreateGiftDialog.newInstance();
        giftDialog.setTargetFragment(this, RC_GIFT);
        giftDialog.show(getFragmentManager(), TAG);
    }
}
