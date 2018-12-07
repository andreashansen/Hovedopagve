package dk.dampbiksen.community;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.view.View;
import android.widget.TextView;

public class ActivityMain extends AppCompatActivity implements NavigationHost {

    public Fragment fragmentNews;
    public Fragment fragmentPoll;
    public Fragment fragmentCampaigns;

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        fragmentNews = new FragmentNews();
        fragmentPoll = new FragmentPolls();
        fragmentCampaigns = new FragmentCampaigns();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragmentCampaigns)
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


        // 2. Shared Elements Transition
        //TransitionSet enterTransitionSet = new TransitionSet();
        //enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        //enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        //enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        //nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(FADE_DEFAULT_TIME );
        enterFade.setDuration(FADE_DEFAULT_TIME);
        enterFade.setMode(Visibility.MODE_OUT);
        nextFragment.setEnterTransition(enterFade);


        fragmentTransaction.replace(R.id.container, nextFragment);

        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();

    }


}
