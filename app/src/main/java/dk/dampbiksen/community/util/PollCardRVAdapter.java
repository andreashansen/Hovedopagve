package dk.dampbiksen.community.util;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Calendar;

import dk.dampbiksen.community.R;
import dk.dampbiksen.community.models.Vote;
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

            ValueEventListener voteListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Vote dbvote = dataSnapshot.getValue(Vote.class);
                    if (dbvote == null)
                    {
                        holder.voteButton.setClickable(true);
                    }
                    else
                    {
                        holder.voteButton.setClickable(false);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("db-read", "loadPost:onCancelled", databaseError.toException());

                }
            };
            myRef = database.getReference("Polls/"+pollContender.pollid+"/Votes/"+ pollContender.pollid +" "+FirebaseAuth.getInstance().getCurrentUser().getUid());
            myRef.addValueEventListener(voteListener);

            holder.voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Tak fordi du stemte på " + pollContender.id,
                            Toast.LENGTH_SHORT).show();

                    Vote vote = new Vote(pollContender,FirebaseAuth.getInstance().getCurrentUser() );

                    // Write a message to the database
                    myRef = database.getReference("Polls/"+pollContender.pollid+"/Votes/"+ vote.voteID);
                    myRef.setValue(vote);
                    myRef = database.getReference("Polls/"+pollContender.pollid+"/Contenders/"+ pollContender.id+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    myRef.setValue("ToV:" +Calendar.getInstance().getTime());

                }

            });

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
