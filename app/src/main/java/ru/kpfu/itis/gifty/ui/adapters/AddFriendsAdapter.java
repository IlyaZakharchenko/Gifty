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
import ru.kpfu.itis.gifty.ui.adapters.AddFriendsAdapter.AddFriendsViewHolder;

/**
 * Created by Ilya Zakharchenko on 24.05.2018.
 */
public class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsViewHolder> {

    class AddFriendsViewHolder extends RecyclerView.ViewHolder {

        private ImageButton actionButton;
        private TextView displayName;

        AddFriendsViewHolder(View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
        }
    }

    private List<Friend> friends;
    private int parentPosition;

    public AddFriendsAdapter(List<Friend> friends, int parentPosition) {
        this.friends = friends;
        this.parentPosition = parentPosition;
    }

    @NonNull
    @Override
    public AddFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_item, parent, false);
        return new AddFriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendsViewHolder holder, int position) {
        holder.displayName.setText(friends.get(position).getDisplayName());
        holder.actionButton.setImageResource(R.drawable.ic_add_clr_primary);
        holder.actionButton.setOnClickListener(v -> {
            UserProvider provider = UserProvider.getInstance();
            Group group = provider.getUser().getGroupList().get(parentPosition);
            group.getFriendList().add(friends.get(position));
            group.getFriendUidList().add(friends.get(position).getUserId());
            provider.updateGroups(group);
            friends.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
