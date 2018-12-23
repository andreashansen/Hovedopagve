package dk.dampbiksen.community;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;

import java.util.Calendar;
import java.util.List;

import dk.dampbiksen.community.models.PollEntry;
import dk.dampbiksen.community.navigation.NavigationHost;


public class ActivityMain extends AppCompatActivity implements NavigationHost {

    public FragmentNews fragmentNews;
    public FragmentPolls fragmentPolls;
    public FragmentDiscounts fragmentDiscounts;

    private static final long FADE_DEFAULT_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        fragmentNews = new FragmentNews();
        fragmentPolls = new FragmentPolls();
        fragmentDiscounts = new FragmentDiscounts();

        fragmentPolls.pollEntries = PollEntry.readData(new PollEntry.FirebaseCallback() {
            @Override
            public void onCallback(List<PollEntry> list) {
                fragmentPolls.pollEntries = list;
                Log.d("FCB","Done :"+ Calendar.getInstance().getTime().toString() +"called from mainactivity");
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragmentNews)
                    .commit();
        }
    }

    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    public void transitionTo(Fragment fragment, boolean addToBackstack) {

        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        Fragment nextFragment = fragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        exitFade.setMode(Visibility.MODE_OUT);

        previousFragment.setExitTransition(exitFade);


        // 2. Enter Transition for New Fragment
        Slide enterSlide = new Slide(Gravity.RIGHT);
        enterSlide.setStartDelay(FADE_DEFAULT_TIME );
        enterSlide.setDuration(FADE_DEFAULT_TIME);
        enterSlide.setMode(Visibility.MODE_IN);


        nextFragment.setEnterTransition(enterSlide);


        fragmentTransaction.replace(R.id.container, nextFragment);

        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();

    }


}
