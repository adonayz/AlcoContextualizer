package edu.wpi.alcoholcontextualizer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.List;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.database.DatabaseHandler;
import edu.wpi.alcoholcontextualizer.fragments.HomeFragment;
import edu.wpi.alcoholcontextualizer.fragments.LocationFragment;
import edu.wpi.alcoholcontextualizer.fragments.PeopleFragment;
import edu.wpi.alcoholcontextualizer.fragments.TimeFragment;
import edu.wpi.alcoholcontextualizer.model.Location;
import edu.wpi.alcoholcontextualizer.model.Person;
import edu.wpi.alcoholcontextualizer.model.Time;
import edu.wpi.alcoholcontextualizer.utilities.DownloadBitmapTask;
import edu.wpi.alcoholcontextualizer.utilities.MLRoundedImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnChartSelected, GoogleApiClient.OnConnectionFailedListener {

    //private GoogleSignInAccount acct;
    private FirebaseUser acct;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_INVITE = 5421;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Replace the fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.activity_main_frameLayout, new HomeFragment()).commit();

        //Intent i = getIntent();
        //acct = (GoogleSignInAccount) i.getParcelableExtra("GoogleAccountData");
        acct = mAuth.getCurrentUser();

        View hView = navigationView.getHeaderView(0);

        MLRoundedImageView profilePictureImageView = (MLRoundedImageView) hView.findViewById(R.id.userProfilePicture);
        TextView displayNameTextView = (TextView) hView.findViewById(R.id.userDisplayName);
        TextView emailAddressTextView = (TextView) hView.findViewById(R.id.userEmailAddress);

        DownloadBitmapTask task = new DownloadBitmapTask(profilePictureImageView);
        task.execute(acct.getPhotoUrl().toString());
        displayNameTextView.setText(acct.getDisplayName());
        emailAddressTextView.setText(acct.getEmail());

        // handle deep link activities

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d(TAG, "getInvitation: no data");
                            return;
                        }

                        // Get the deep link
                        Uri deepLink = data.getLink();

                        // Extract invite
                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                        if (invite != null) {
                            String invitationId = invite.getInvitationId();
                        }

                        // Handle the deep link
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

//        printDatabaseDataInLog();
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();

        super.onStart();

    }

    private void printDatabaseDataInLog() {
        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         */
        List<Location> locations = db.getAllLocations();
        List<Time> times = db.getAllTimes();
        List<Person> persons = db.getAllPersons();

        Log.d("*********loc count", String.valueOf(db.getLocationsCount()));
        Log.d("********time count", String.valueOf(db.getTimesCount()));
        Log.d("********person count", String.valueOf(db.getPersonsCount()));

        for (Location ln : locations) {
            String log = "Id: " + ln.getLocationId() + " ,Name: " + ln.getLocationName() + " ,Lat: " + ln.getLatitude()
                    + " ,Long: " + ln.getLongitude() + " ,Freq: " + ln.getFrequencyAmount() + " ,date: " + ln.getRecentDate();
            Log.d("Location: ", log);
        }

        for (Time t : times) {
            String log = "Id: " + t.getTimeId() + " ,month: " + t.getMonth() + " ,day: " + t.getDay()
                    + " ,hour: " + t.getHour() + " ,amt: " + t.getDrinkAmount();
            Log.d("Time: ", log);
        }

        for (Person p : persons) {
            String log = "Id: " + p.getPersonId() + " ,Name: " + p.getPersonName() + " ,amt: " + p.getDrinkAmount()
                    + " ,gName: " + p.getGroupName();
            Log.d("Person: ", log);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tutorial) {
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // The fragment to be loaded
        Fragment fragment;
        int titleResourceId;

        // Deternmine which fragment
        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                titleResourceId = R.string.app_name;
                break;
            case R.id.nav_location:
                fragment = new LocationFragment();
                titleResourceId = R.string.nav_location_menu_title;
                break;
            case R.id.nav_People:
                fragment = new PeopleFragment();
                titleResourceId = R.string.nav_people_menu_title;
                break;
            case R.id.nav_time:
                fragment = new TimeFragment();
                titleResourceId = R.string.nav_time_menu_title;
                break;
            case R.id.nav_add_friends:
                fragment = null;
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();

                startActivityForResult(intent, REQUEST_INVITE);

                titleResourceId = R.string.nav_add_friends_menu_title;
                break;
            case R.id.sign_out:
                fragment = null;

                signOut();

                titleResourceId = R.string.sign_out_title;
                break;
            default:
                fragment = new LocationFragment();
                titleResourceId = R.string.nav_location_menu_title;
        }

        // Replace the fragment
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.activity_main_frameLayout, fragment).commit();
            // Set the title
            setTitle(titleResourceId);
        }


        // Close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onChartSelected(int imageResId, String name) {

        Fragment fragment;

        switch (Charts.valueOf(name.replaceAll(" ", "_").toUpperCase())) {
            case FREQUENT_DRINKING_LOCATIONS:
                fragment = LocationFragment.newInstance(0);
                break;
            case RECENT_DRINKING_LOCATIONS:
                fragment = LocationFragment.newInstance(1);
                break;
            case MONTHLY_DRINKS:
                fragment = TimeFragment.newInstance(0);
                break;
            case DAILY_DRINKS:
                fragment = TimeFragment.newInstance(1);
                break;
            case DRINKS_WITH_INDIVIDUALS:
                fragment = PeopleFragment.newInstance(0);
                break;
            case DRINKS_WITH_GROUPS:
                fragment = PeopleFragment.newInstance(1);
                break;
            default:
                fragment = new PeopleFragment();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_frameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    public enum Charts {
        FREQUENT_DRINKING_LOCATIONS,
        RECENT_DRINKING_LOCATIONS,
        MONTHLY_DRINKS,
        DAILY_DRINKS,
        DRINKS_WITH_INDIVIDUALS,
        DRINKS_WITH_GROUPS
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), GoogleLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
