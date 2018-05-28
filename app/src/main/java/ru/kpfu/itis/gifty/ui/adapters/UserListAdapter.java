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
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.UserListAdapter.UserListViewHolder;

/**
 * Created by Ilya Zakharchenko on 16.05.2018.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListViewHolder> {

    static class UserListViewHolder extends RecyclerView.ViewHolder {

        private ImageButton actionButton;
        private TextView displayNameTextView;

        UserListViewHolder(final View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
        }
    }
    private List<User> userList;

    public UserListAdapter(final List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, final int position) {
        User user = userList.get(position);
        holder.displayNameTextView.setText(user.getDisplayName());
        holder.actionButton.setImageResource(R.drawable.ic_add_clr_primary);
        holder.actionButton.setOnClickListener(v -> {
            userList.remove(user);
            notifyDataSetChanged();
            UserProvider.getInstance()
                    .addFriend(new Friend(user.getDisplayName(), user.getUid()));

        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
