package ru.kpfu.itis.gifty.ui.activities;

import android.os.Bundle;
import ru.kpfu.itis.gifty.R;

public class GroupsActivity extends BottomNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        initViews();
        initListeners();
    }

    private void initViews() {
        navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_groups);
    }

    private void initListeners() {
        navigation.setOnNavigationItemSelectedListener(navigationListener);
    }
}
