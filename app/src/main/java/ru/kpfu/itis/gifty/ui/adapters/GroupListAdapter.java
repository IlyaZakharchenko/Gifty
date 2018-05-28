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
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.activities.BottomNavigationActivity;
import ru.kpfu.itis.gifty.ui.adapters.GroupListAdapter.GroupViewHolder;
import ru.kpfu.itis.gifty.ui.fragments.AddFriendsFragment;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private GroupFriendListAdapter adapter;
        private Button addFriendButton;
        private ImageButton deleteButton;
        private List<Friend> friendList;
        private TextView nameTextView;
        private RecyclerView recyclerView;

        GroupViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            addFriendButton = itemView.findViewById(R.id.btn_add);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    private Context context;
    private int expandedPosition = -1;
    private List<Group> groupList;
    private int previousExpandedPosition = -1;

    public GroupListAdapter(List<Group> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        boolean isExpanded = holder.getAdapterPosition() == expandedPosition;
        holder.friendList = groupList.get(position).getFriendList();
        holder.adapter = new GroupFriendListAdapter(holder.friendList, position);
        holder.addFriendButton.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.addFriendButton.setOnClickListener(view ->
                ((BottomNavigationActivity) context)
                        .getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_host, AddFriendsFragment.newInstance(position))
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

        holder.nameTextView.setText(groupList.get(holder.getAdapterPosition()).getName());
        holder.deleteButton.setImageResource(R.drawable.ic_delete);
        holder.deleteButton.setOnClickListener(v -> {
            UserProvider.getInstance().deleteGroup(groupList.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
