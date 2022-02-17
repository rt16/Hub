package com.icicisecurities.hub;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.LogoutDialog;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavInflater inflater = navController.getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.mobile_navigation);

        if (!PreferenceManager.getDefaultSharedPreferences(this).getString(AppConstants.INWARD_CENTER, "").equals("*")) {
            graph.setStartDestination(R.id.nav_new);
        } else {
            graph.setStartDestination(R.id.nav_profile);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderWelcomeTitleTV;navHeaderWelcomeTitleTV = headerView.findViewById(R.id.nav_header_welcome_user_tv);
        String welcomeText = "Logged in as " + PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString(AppConstants.LOGGED_IN_USER_ID,"");
        navHeaderWelcomeTitleTV.setText(welcomeText);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_new, R.id.nav_summary, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.setGraph(graph);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
//        Integer backStackSize = getFragmentManager().getBackStackEntryCount();
//
//        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        if (backStackSize < 2) {
//            new LogoutDialog(HomeActivity.this, HomeActivity.this);
//        } else if (backStackSize >= 2) {
//
//            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1);
//
//            if (backEntry.getName().equals(AppConstants.LEADS_FRAGMENT_BACKSTACK_TAG)) {
//                //GOTO AddPersonalDetailsFragment....
//                //@ADD_PERSONAL_DETAILS_FRAGMENT_BACKSTACK_TAG fragment will be in the Backstack for sure bcoz, whenever app starts then app goes to this page by default.
//
//                /**Why are we sending 0 here????
//                 * =============================
//                 *
//                 * When we Pop the Fragment from Backstack using TAG the we have 2 options :
//                 * 01. POP_BACK_STACK_INCLUSIVE.
//                 * 02. 0.
//                 *
//                 * If we use Inclusive then @ADD_PERSONAL_DETAILS_FRAGMENT_BACKSTACK_TAG will also get removed from the Backstack & we will see a blank screen.
//                 * But if we use 0, then @ADD_PERSONAL_DETAILS_FRAGMENT_BACKSTACK_TAG will be in History & screen will also display "AddPresonalDetailsFragment" screen.
//                 * **/
//            }//if(backEntry.getName().equals(AppConstants.LEADS_FRAGMENT_BACKSTACK_TAG)) closes here....
//            else {
//                //Pop the Fragments from the Stack....
//                fragmentManager.popBackStack();
//            }//else closes here.....
//
//        }//else if(backStackSize >= 2) closes here.....
//
//        else {
//            //Else fragment shud be removed from Backstack....
//            super.onBackPressed();
//        }//fragment shud be removed from Backstack..

    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        hideKeyboard(drawerView);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}