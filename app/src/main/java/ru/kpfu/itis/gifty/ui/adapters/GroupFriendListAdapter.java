package ru.kpfu.itis.gifty.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Friend;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.GroupFriendListAdapter.GroupFriendViewHolder;

/**
 * Created by Ilya Zakharchenko on 23.05.2018.
 */
public class GroupFriendListAdapter extends RecyclerView.Adapter<GroupFriendViewHolder> {

    class GroupFriendViewHolder extends RecyclerView.ViewHolder {

        private ImageButton actionButton;
        private TextView displayName;

        GroupFriendViewHolder(final View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
        }
    }
    private List<Friend> friends;
    private int parentPosition;

    GroupFriendListAdapter(final List<Friend> friends, int parentPosition) {
        this.friends = friends;
        this.parentPosition = parentPosition;
    }

    @NonNull
    @Override
    public GroupFriendViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_item, parent, false);
        return new GroupFriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupFriendViewHolder holder, final int position) {
        holder.displayName.setText(friends.get(position).getDisplayName());
        holder.actionButton.setImageResource(R.drawable.ic_delete);
        holder.actionButton.setOnClickListener(v -> {
            UserProvider provider = UserProvider.getInstance();
            Group group = provider.getUser().getGroupList().get(parentPosition);
            group.getFriendUidList().remove(position);
            friends.remove(position);
            provider.updateGroups(group);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

}
