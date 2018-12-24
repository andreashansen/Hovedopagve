package dk.dampbiksen.community.models;


import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.dampbiksen.community.util.FirebaseCallback;

/**
 * A discount entry in the list of products.
 */
public class NewsEntry {
    private static final String TAG = NewsEntry.class.getSimpleName();

    public final String title;
    public final Uri dynamicUrl;
    public final String url;
    public final String text;
    public final String text1;
    public final String imgurl0;
    public final String imgurl1;
    public final String imgurl2;

    public NewsEntry(
            String title, String dynamicUrl, String url, String text, String text1, String imgurl0, String imgurl1, String imgurl2) {
        this.title = title;
        this.dynamicUrl = Uri.parse(dynamicUrl);
        this.url = url;
        this.text = text;
        this.text1 = text1;
        this.imgurl0 = imgurl0;
        this.imgurl1 = imgurl1;
        this.imgurl2 = imgurl2;

    }

    public static List<NewsEntry> initNewsList(final FirebaseCallback firebaseCallback) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Source");
        DatabaseReference newsListRef = rootRef.child("News");
        Query query = newsListRef.orderByKey();
        final ArrayList<NewsEntry> newsEntries = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsEntries.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    newsEntries.add(new NewsEntry(
                            ds.child("title").getValue(String.class),
                            "",
                            ds.child("url").getValue(String.class),
                            ds.child("text").getValue(String.class),
                            ds.child("text1").getValue(String.class),
                            ds.child("imgurl0").getValue(String.class),
                            ds.child("imgurl1").getValue(String.class),
                            ds.child("imgurl2").getValue(String.class)));
                }
                firebaseCallback.onCallbackNews(newsEntries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        query.addValueEventListener(eventListener);
        return newsEntries;
    }

}