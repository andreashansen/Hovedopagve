package dk.dampbiksen.community.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Calendar;

import dk.dampbiksen.community.R;
import dk.dampbiksen.community.network.ImageRequester;
import dk.dampbiksen.community.network.PollEntry;

/**
 * Adapter used to show a simple grid of products.
 */
public class PollCardRVAdapter extends RecyclerView.Adapter<PollCardViewHolder> {

    private List<PollEntry> productList;
    private ImageRequester imageRequester;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    public PollCardRVAdapter(List<PollEntry> productList) {
        this.productList = productList;
        this.imageRequester = ImageRequester.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @NonNull
    @Override
    public PollCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_util_poll_card, parent, false);
        return new PollCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PollCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            final PollEntry pollContender = productList.get(position);
            holder.productTitle.setText(pollContender.title);
            holder.productDesc.setText(pollContender.description);
            imageRequester.setImageFromUrl(holder.productImage, pollContender.url);
            holder.voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Tak fordi du stemte på " + pollContender.id,
                            Toast.LENGTH_SHORT).show();

                    Vote vote = new Vote(pollContender,FirebaseAuth.getInstance().getCurrentUser() );

                    // Write a message to the database
                    myRef = database.getReference("Polls/"+pollContender.pollid+"/Contenders/"+ pollContender.id+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    myRef.setValue("ToV:" +Calendar.getInstance().getTime());
                    myRef = database.getReference("Polls/"+pollContender.pollid+"/Votes/"+ vote.voteID);
                    myRef.setValue(vote);

                }

            });

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
