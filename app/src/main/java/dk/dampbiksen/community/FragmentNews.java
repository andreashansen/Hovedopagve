package dk.dampbiksen.community;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import dk.dampbiksen.community.models.NewsEntry;
import dk.dampbiksen.community.navigation.NavigationIconClickListener;
import dk.dampbiksen.community.navigation.NavigationMenuClickListener;
import dk.dampbiksen.community.util.DefaultItemDecoration;
import dk.dampbiksen.community.util.NewsCardRVAdapter;


public class FragmentNews extends Fragment {

    public List<NewsEntry> newsEntries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_fragment_news, container, false);

        // Set up the toolbar + menu navigation
        setUpToolbar(view);
        setUpMenuNavigation(view);

        // Set up the RecyclerView
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        NewsCardRVAdapter adapter = new NewsCardRVAdapter(newsEntries);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DefaultItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.product_grid_spacing),
                getResources().getDimensionPixelSize(R.dimen.product_grid_spacing_small)));

        view.findViewById(R.id.presenter).setBackground(getContext().getDrawable(R.drawable.fragments_background_shape));

        return view;
    }
    private void setUpMenuNavigation(View view) {
        view.findViewById(R.id.button_news).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_news)));
        view.findViewById(R.id.button_polls).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_polls)));
        view.findViewById(R.id.button_discounts).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_discounts)));
        view.findViewById(R.id.button_my_account).setOnClickListener(new NavigationMenuClickListener(view.findViewById(R.id.button_my_account)));
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar_news);
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
