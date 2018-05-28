package ru.kpfu.itis.gifty.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.activities.BottomNavigationActivity;
import ru.kpfu.itis.gifty.ui.adapters.GiftListAdapter.GiftViewHolder;
import ru.kpfu.itis.gifty.ui.fragments.AddFriendsFragment;
import ru.kpfu.itis.gifty.ui.fragments.AddGroupsFragment;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class GiftListAdapter extends RecyclerView.Adapter<GiftViewHolder> {

    class GiftViewHolder extends RecyclerView.ViewHolder {

        private GiftGroupListAdapter adapter;
        private Button addGroupButton;
        private ImageButton deleteButton;
        private List<Group> groupList;
        private TextView nameTextView;
        private RecyclerView recyclerView;

        GiftViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            addGroupButton = itemView.findViewById(R.id.btn_add);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    private int expandedPosition = -1;
    private List<Gift> giftList;
    private int previousExpandedPosition = -1;
    private Context context;

    public GiftListAdapter(List<Gift> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new GiftViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        boolean isExpanded = holder.getAdapterPosition() == expandedPosition;
        holder.groupList = giftList.get(position).getGroupList();
        holder.adapter = new GiftGroupListAdapter(holder.groupList, position);
        holder.addGroupButton.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.addGroupButton.setOnClickListener(view ->
                ((BottomNavigationActivity) context)
                        .getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_host, AddGroupsFragment.newInstance(position))
                        .addToBackStack(null)
                        .commit());

        holder.recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recyclerView.setAdapter(holder.adapter);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded) {
            previousExpandedPosition = holder.getAdapterPosition();
        }

        holder.itemView.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : holder.getAdapterPosition();
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.nameTextView.setText(giftList.get(holder.getAdapterPosition()).getName());
        holder.deleteButton.setImageResource(R.drawable.ic_delete);
        holder.deleteButton.setOnClickListener(v -> {
            UserProvider.getInstance().deleteGift(giftList.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }
}
