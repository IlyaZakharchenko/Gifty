package ru.kpfu.itis.gifty.ui.adapters;

import android.content.Context;
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
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.activities.BottomNavigationActivity;
import ru.kpfu.itis.gifty.ui.adapters.FriendListAdapter.FriendsViewHolder;
import ru.kpfu.itis.gifty.ui.fragments.FriendsGiftListFragment;

/**
 * Created by Ilya Zakharchenko on 14.05.2018.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    class FriendsViewHolder extends RecyclerView.ViewHolder {

        private ImageButton actionButton;
        private TextView displayName;

        FriendsViewHolder(final View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
        }
    }

    private List<Friend> friends;
    private Context context;

    public FriendListAdapter(List<Friend> friends, Context context) {
        this.friends = friends;
        this.context = context;
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
        holder.actionButton.setOnClickListener(v -> {
            UserProvider.getInstance().deleteFriend(friends.get(position));
            notifyDataSetChanged();
        });
        holder.itemView.setOnClickListener(v -> {
            ((BottomNavigationActivity) context).getFragmentManager()
                    .beginTransaction().replace(R.id.fragment_host, FriendsGiftListFragment.newInstance(position))
            .addToBackStack(null)
            .commit();
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
