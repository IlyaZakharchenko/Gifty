package ru.kpfu.itis.gifty.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import ru.kpfu.itis.gifty.R;

public class BottomNavigationActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        initViews();
        initListeners();
    }

    private void initViews() {
        navigation = findViewById(R.id.navigation);
        fragmentContainer = findViewById(R.id.fragment_host);
    }

    private void initListeners() {
        navigation.setOnNavigationItemSelectedListener(item -> {
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
        });
    }
}
