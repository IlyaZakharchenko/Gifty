package ru.kpfu.itis.gifty.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.ui.adapters.FriendsGiftAdapter.FriendsGiftViewHolder;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class FriendsGiftAdapter extends RecyclerView.Adapter<FriendsGiftViewHolder> {

    class FriendsGiftViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView nameTaxtView;

        public FriendsGiftViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            nameTaxtView = itemView.findViewById(R.id.tv_name);
        }
    }

    private List<Gift> giftList;
    private String uid;

    public FriendsGiftAdapter(List<Gift> giftList, String uid) {
        this.giftList = giftList;
        this.uid = uid;
    }

    @NonNull
    @Override
    public FriendsGiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item, parent, false);
        return new FriendsGiftViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsGiftViewHolder holder, int position) {
        if (!giftList.get(position).getCheckedFriendUid().isEmpty()) {
            holder.checkBox.setChecked(true);
            if (!giftList.get(position).getCheckedFriendUid().equals(FirebaseAuth.getInstance().getUid())) {
                holder.checkBox.setEnabled(false);
            }
        }
        holder.nameTaxtView.setText(giftList.get(position).getName());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                giftList.get(position).setCheckedFriendUid(FirebaseAuth.getInstance().getUid());

            } else {
                giftList.get(position).setCheckedFriendUid("");
            }
            FirebaseFirestore.getInstance().document("users/" + uid + "/gifts/" + giftList.get(position)
                    .getName()).set(giftList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }
}
