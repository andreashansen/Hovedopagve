package dk.dampbiksen.community;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;


public class FragmentPolls extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_fragment_polls, container, false);

        // Set up the toolbar + menu navigation
        setUpToolbar(view);
        setUpMenuNavigation(view);

        view.findViewById(R.id.presenter).setBackground(getContext().getDrawable(R.drawable.fragments_background_shape));

        return view;
    }

    private void setUpMenuNavigation(View view) {
        view.findViewById(R.id.button_news).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_news)));
        view.findViewById(R.id.button_polls).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_polls)));
        view.findViewById(R.id.button_campaigns).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_campaigns)));
        view.findViewById(R.id.button_my_account).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_my_account)));
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar_polls);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.presenter),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu),
                getContext().getResources().getDrawable(R.drawable.close_menu)));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }


}
