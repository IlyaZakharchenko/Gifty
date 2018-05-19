package ru.kpfu.itis.gifty.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import ru.kpfu.itis.gifty.R;

public abstract class BottomNavigationActivity extends AppCompatActivity {

    protected BottomNavigationView navigation;

    protected OnNavigationItemSelectedListener navigationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationListener = item -> {
            Intent intent = new Intent()
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    if (navigation.getSelectedItemId() != R.id.navigation_profile) {
                        intent.setClass(this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    return true;
                case R.id.navigation_friends:
                    if (navigation.getSelectedItemId() != R.id.navigation_friends) {
                        intent.setClass(this, FriendsActivity.class);
                        startActivity(intent);
                    }
                    return true;
                case R.id.navigation_groups:
                    if (navigation.getSelectedItemId() != R.id.navigation_groups) {
                        intent.setClass(this, GroupsActivity.class);
                        startActivity(intent);
                    }
                    return true;

                case R.id.navigation_gift_list:
                    if (navigation.getSelectedItemId() != R.id.navigation_gift_list) {
                        intent.setClass(this, GiftListActivity.class);
                        startActivity(intent);
                    }
                    return true;

                default:
                    return true;
            }
        };
    }
}
