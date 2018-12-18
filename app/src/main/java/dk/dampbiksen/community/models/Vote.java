package dk.dampbiksen.community.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class Vote {

    public String voteID;
    public String pollID;
    public String voteDate;
    public String voterID;
    public String voteRecipient;


    public Vote ()
    {

    }
    public Vote(PollEntry contender, FirebaseUser user)
    {
        this.voteDate = Calendar.getInstance().getTime().toString();
        this.voterID = user.getDisplayName();
        this.voteRecipient = contender.id;
        this.pollID = contender.pollid;
        this.voteID = contender.pollid +" "+ user.getUid();

    }
}
