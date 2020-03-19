package aara.tech.rootless_auto_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.ui.Fragments.AddVehicleFragment;
import aara.tech.rootless_auto_user.ui.Fragments.BookNowFragment;
import aara.tech.rootless_auto_user.ui.Fragments.MoreInfoFragment;
import aara.tech.rootless_auto_user.ui.Fragments.MyAppointmentFragment;
import aara.tech.rootless_auto_user.ui.Fragments.MyVehicleFragment;
import aara.tech.rootless_auto_user.ui.Fragments.PaymentFragment;
import aara.tech.rootless_auto_user.utils.Commonhelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Commonhelper commonhelper;
    ActionBar actionBar;
    Fragment selectedFragment = null;
    TextView nav_username;
    String username;
    Context context;
    Toolbar toolbar;

    //OnBackPress
    private long BPTime;
    private Toast backToast;

    public void initView(){

       /* drawerLayout = findViewById(R.id.activity_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);*/

         actionBar = getActionBar();


        commonhelper = new Commonhelper(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new BookNowFragment()).commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        username = commonhelper.getSharedPreferences("current_user_name", null);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName =  navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        txtProfileName.setText(username);


        toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tablayout_gradient));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
                   /* case R.id.nav_dashboard:
                        selectedFragment = new DashboardFragment();
                        Toast.makeText(MainActivity.this, "Dashboard",Toast.LENGTH_SHORT).show();
                        break;*/
            case R.id.nav_book_now:
                selectedFragment = new BookNowFragment();
                setActionBarTitle("Book Now");
                Toast.makeText(MainActivity.this, "Book Now", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_my_vehicle:
                selectedFragment = new MyVehicleFragment();
                setActionBarTitle("My Vehicle");
                break;

                /*try {
                    String username = commonhelper.getSharedPreferences("uname", null);
                    String error = commonhelper.getSharedPreferences("error", null);
                    String password = commonhelper.getSharedPreferences("password", null);
                    String email = commonhelper.getSharedPreferences("email", null);
                    Toast.makeText(MainActivity.this, email + "\n" + password + "\n" + username, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


//                        Toast.makeText(MainActivity.this, "My Vehicle",Toast.LENGTH_SHORT).show();
            case R.id.nav_add_vehicle:
                selectedFragment = new AddVehicleFragment();
                setActionBarTitle("Add Vehicle");
                Toast.makeText(MainActivity.this, "Add Vehicle", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_my_appointment:
                selectedFragment = new MyAppointmentFragment();
                setActionBarTitle("My Appointment");
                Toast.makeText(MainActivity.this, "My Appointment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_payment:
                selectedFragment = new PaymentFragment();
                setActionBarTitle("Payment");
                Toast.makeText(MainActivity.this, "Payment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_more_info:
                selectedFragment = new MoreInfoFragment();
                setActionBarTitle("More Info");
                Toast.makeText(MainActivity.this, "More Info", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                if (commonhelper.ClearSharedPreference()) {
                    commonhelper.ShowMesseage("Log Out Successfull");
                    commonhelper.callintent(MainActivity.this, SplashActivity.class);
                } else {
                    commonhelper.ShowMesseage("Something Went Wrong");
                }

                break;
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }

    private void setActionBarTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
