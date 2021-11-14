package com.android.whatsappsaver;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.whatsappsaver.fragments.WhatsAppImagesFragment;
import com.android.whatsappsaver.fragments.WhatsAppSavedFragment;
import com.android.whatsappsaver.fragments.WhatsAppVideosFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        new File(Utils.statusSaverPath).mkdirs();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //TODO

        if (id == R.id.navRate) {
            Toast.makeText(this, "Option Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.navBusinessWhatsApp) {
            Toast.makeText(this, "Will be implemented soon", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(MainActivity.this, getSupportFragmentManager());
        adapter.addFragment(new WhatsAppImagesFragment(), "Images");
        adapter.addFragment(new WhatsAppVideosFragment(), "Videos");
        adapter.addFragment(new WhatsAppSavedFragment(), "Saved");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    class ViewPageAdapter extends FragmentPagerAdapter {
        private Context context;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public ViewPageAdapter(Context context1, FragmentManager fragmentManager){
            super(fragmentManager);
            context = context1;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}