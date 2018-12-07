package dk.dampbiksen.community;

import android.view.View;
import android.widget.Toast;

public class NavigationMenuClickListener implements View.OnClickListener {


    NavigationMenuClickListener(View sheet)
    {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button_news:
                ((ActivityMain)v.getContext()).transitionTo(((ActivityMain)v.getContext()).fragmentNews, false);
                break;
            case R.id.button_polls:
                ((ActivityMain)v.getContext()).transitionTo(((ActivityMain)v.getContext()).fragmentPoll, false);
                break;
            case R.id.button_campaigns:
                ((ActivityMain)v.getContext()).transitionTo(((ActivityMain)v.getContext()).fragmentCampaigns, false);
                break;
            case R.id.button_my_account:
                Toast.makeText(v.getContext(), "Onclick on MY ACCOUNT caught.",
                        Toast.LENGTH_LONG).show();
                break;
        }

    }
}
