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
import ru.kpfu.itis.gifty.ui.adapters.AddGroupsAdapter.AddGroupsViewHolder;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class AddGroupsAdapter extends RecyclerView.Adapter<AddGroupsViewHolder> {

    class AddGroupsViewHolder extends RecyclerView.ViewHolder {

        private ImageButton actionButton;
        private TextView displayName;

        AddGroupsViewHolder(View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.tv_name);
            actionButton = itemView.findViewById(R.id.btn_action);
        }
    }

    public AddGroupsAdapter(List<Group> groups, int parentPosition) {
        this.groups = groups;
        this.parentPosition = parentPosition;
    }

    private List<Group> groups;
    private int parentPosition;

    @NonNull
    @Override
    public AddGroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_item, parent, false);
        return new AddGroupsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddGroupsViewHolder holder, int position) {
        holder.displayName.setText(groups.get(position).getName());
        holder.actionButton.setImageResource(R.drawable.ic_add_clr_primary);
        holder.actionButton.setOnClickListener(v -> {
            UserProvider provider = UserProvider.getInstance();
            Gift gift = provider.getUser().getGiftList().get(parentPosition);
            gift.getGroupList().add(groups.get(position));
            gift.getGroupsId().add(groups.get(position).getName());
            provider.updateGifts(gift);
            groups.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
