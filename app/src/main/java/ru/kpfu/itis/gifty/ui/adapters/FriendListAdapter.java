package ru.kpfu.itis.gifty.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.ui.adapters.FriendListAdapter.FriendsViewHolder;
import ru.kpfu.itis.gifty.model.entities.Friend;

/**
 * Created by Ilya Zakharchenko on 14.05.2018.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private List<Friend> friends;

    static class FriendsViewHolder extends RecyclerView.ViewHolder {

        private TextView displayName;
        private ImageButton actionButton;

        FriendsViewHolder(final View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
            actionButton.setOnClickListener(v ->
                    //TODO
                    Toast.makeText(itemView.getContext(), "Delete", Toast.LENGTH_SHORT).show()
            );
        }
    }

    public FriendListAdapter(final List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_item, parent, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsViewHolder holder, final int position) {
        holder.displayName.setText(friends.get(position).getDisplayName());
        holder.actionButton.setImageResource(R.drawable.ic_delete);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void update(final List<Friend> friendList) {
        friends = friendList;
        notifyDataSetChanged();
    }
}
