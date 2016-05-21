package com.dranithix.cheatsheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.dranithix.cheatsheet.events.OnCategoryClickEvent;
import com.dranithix.cheatsheet.events.OnSubcategoryClickEvent;
import com.dranithix.cheatsheet.ui.CategoryListAdapter;
import com.dranithix.cheatsheet.util.DebugUtil;

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

        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new CategoryFragment());
        transaction.commit();
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
        transaction.replace(R.id.fragment_container, SubcategoryFragment.newInstance(event.category.getId()));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Subscribe
    public void onSubcategoryClicked(OnSubcategoryClickEvent event) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }
}
