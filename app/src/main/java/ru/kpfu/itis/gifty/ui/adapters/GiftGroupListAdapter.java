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
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.adapters.GiftGroupListAdapter.GiftGroupViewHolder;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class GiftGroupListAdapter extends RecyclerView.Adapter<GiftGroupViewHolder> {

    class GiftGroupViewHolder extends RecyclerView.ViewHolder {

        private ImageButton actionButton;
        private TextView displayName;

        GiftGroupViewHolder(final View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
        }
    }

    private List<Group> groups;
    private int parentPosition;

    GiftGroupListAdapter(List<Group> groups, int parentPosition) {
        this.groups = groups;
        this.parentPosition = parentPosition;
    }

    @NonNull
    @Override
    public GiftGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new GiftGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftGroupViewHolder holder, int position) {
        holder.displayName.setText(groups.get(position).getName());
        holder.actionButton.setImageResource(R.drawable.ic_delete);
        holder.actionButton.setOnClickListener(v -> {
            UserProvider provider = UserProvider.getInstance();
            Gift gift = provider.getUser().getGiftList().get(parentPosition);
            gift.getGroupsId().remove(position);
            groups.remove(position);
            provider.updateGifts(gift);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
