package com.omarstudiolimited.nav_t2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.omarstudiolimited.nav_t2.ui.AboutApp.AboutAppFragment;
import com.omarstudiolimited.nav_t2.ui.Devolopers.DevelopersFragment;
import com.omarstudiolimited.nav_t2.ui.MoreInfo.MoreInfoFragment;
import com.omarstudiolimited.nav_t2.ui.home.HomeFragment;
import com.omarstudiolimited.nav_t2.ui.live.LiveFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView sort_img;
    DrawerLayout drawer;
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//======================================================================================
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_live, R.id.nav_deve,R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//    ==============================================================
        sort_img = (ImageView)findViewById(R.id.sort);
        sort_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
// ================================================================
} // OnCreate End

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.nav_host_fragment);
        switch (item.getItemId()){
            case R.id.nav_home:
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new HomeFragment()).commit();
                break;

            case R.id.nav_live:
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new LiveFragment()).commit();
            break;

            case R.id.nav_deve:
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new DevelopersFragment()).commit();
                break;

            case R.id.nav_about:
                frameLayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new AboutAppFragment()).commit();
                break;

            case R.id.share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"https://www.google.com/");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent,"Share"));
                break;

            case R.id.email:
                Toast.makeText(this, "This will be added soon", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    } // On Item Selected

//    @Override
//    public void onBackPressed() {
//        AlertDialog alertDialog = new AlertDialog.Builder(this)
//                .setIcon(R.drawable.search)
//                .setTitle("Are you sure you Want to Exit ?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //set what would happen when positive button is clicked
//                        finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(),"As You like :)",Toast.LENGTH_LONG).show();
//                    }
//                })
//                .show();
//    } // On Back Pressed

} // Main Class End






































//    public void switchToSecondFragment(){
//
//        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.nav_host_fragment);
//        frameLayout.removeAllViews();
//        FragmentTransaction transaction1 =getSupportFragmentManager().beginTransaction();
//        transaction1.add(R.id.nav_host_fragment, AboutAppFragment.class,null);
//        transaction1.commit();
//    }
