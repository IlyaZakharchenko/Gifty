package ru.kpfu.itis.gifty.ui.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.fragments.FriendsFragment;
import ru.kpfu.itis.gifty.ui.fragments.GiftListFragment;
import ru.kpfu.itis.gifty.ui.fragments.GroupsFragment;
import ru.kpfu.itis.gifty.ui.fragments.ProfileFragment;
import ru.kpfu.itis.gifty.utils.BottomNavigationHelper;

public class BottomNavigationActivity extends AppCompatActivity {

    private FriendsFragment friendsFragment;
    private GiftListFragment giftListFragment;
    private GroupsFragment groupsFragment;
    private FragmentManager manager;
    private BottomNavigationView navigation;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        manager = getFragmentManager();
        giftListFragment = GiftListFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        friendsFragment = FriendsFragment.newInstance();
        groupsFragment = GroupsFragment.newInstance();
        initViews();
        initListeners();
        navigation.setSelectedItemId(R.id.navigation_gift_list);
        BottomNavigationHelper.disableShiftMode(navigation);
        if (UserProvider.getInstance().getUser().getDisplayName() == null) {
            startActivity(new Intent(this, SetNameActivity.class));
        }
    }

    private void initListeners() {
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    if (navigation.getSelectedItemId() != R.id.navigation_profile) {
                        manager.beginTransaction().replace(R.id.fragment_host, profileFragment, "Profile "
                                + "Fragment").commit();
                    }
                    return true;
                case R.id.navigation_friends:
                    if (navigation.getSelectedItemId() != R.id.navigation_friends) {
                        manager.beginTransaction().replace(R.id.fragment_host, friendsFragment, "Friends "
                                + "Fragment").commit();
                    }
                    return true;
                case R.id.navigation_groups:
                    if (navigation.getSelectedItemId() != R.id.navigation_groups) {
                        manager.beginTransaction().replace(R.id.fragment_host, groupsFragment, "Groups "
                                + "Fragment").commit();
                    }
                    return true;

                case R.id.navigation_gift_list:
                    if (navigation.getSelectedItemId() != R.id.navigation_gift_list) {
                        manager.beginTransaction().replace(R.id.fragment_host, giftListFragment, "Gift List "
                                + "Fragment").commit();
                    }
                    return true;

                default:
                    return true;
            }
        });
    }

    private void initViews() {
        navigation = findViewById(R.id.navigation);
    }
}
