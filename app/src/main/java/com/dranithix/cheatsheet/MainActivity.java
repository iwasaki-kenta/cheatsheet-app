package com.dranithix.cheatsheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import com.dranithix.cheatsheet.events.OnCategoryClickEvent;
import com.dranithix.cheatsheet.events.OnSubcategoryClickEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new CategoryFragment());
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.open_nav_drawer)
    public void openNavigationDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Subscribe
    public void onCategoryClicked(OnCategoryClickEvent event) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out, android.R.anim.fade_in,
                android.R.anim.fade_out);
        transaction.replace(R.id.fragment_container, SubcategoryFragment.newInstance(event.category));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Subscribe
    public void onSubcategoryClicked(OnSubcategoryClickEvent event) {
        Intent intent = new Intent(this, NoteListActivity.class);
        intent.putExtra("subcategory", event.subcategory);
        startActivity(intent);
    }
}
