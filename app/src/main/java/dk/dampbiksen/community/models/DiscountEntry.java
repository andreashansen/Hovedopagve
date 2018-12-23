package dk.dampbiksen.community.models;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import dk.dampbiksen.community.R;
import dk.dampbiksen.community.util.FirebaseCallback;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A discount entry in the list of products.
 */
public class DiscountEntry {
    private static final String TAG = DiscountEntry.class.getSimpleName();

    public final String title;
    public final Uri dynamicUrl;
    public final String url;
    public final String discountcode;
    public final String discountid;
    public final String description;

    public DiscountEntry(
            String title, String dynamicUrl, String url, String discountcode, String discountid, String description) {
        this.title = title;
        this.dynamicUrl = Uri.parse(dynamicUrl);
        this.url = url;
        this.discountcode = discountcode;
        this.discountid = discountid;
        this.description = description;

    }

    public static List<DiscountEntry> initDiscountList(final FirebaseCallback firebaseCallback) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Source");
        DatabaseReference discountsListRef = rootRef.child("Discounts");
        Query query = discountsListRef.orderByChild("title");
        final ArrayList<DiscountEntry> discountEntries = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discountEntries.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    discountEntries.add(new DiscountEntry(
                            ds.child("title").getValue(String.class),
                            "",
                            ds.child("url").getValue(String.class),
                            ds.child("discountcode").getValue(String.class),
                            ds.child("discountid").getValue(String.class),
                            ds.child("description").getValue(String.class)));
                }
                firebaseCallback.onCallbackDiscount(discountEntries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        query.addValueEventListener(eventListener);
        return discountEntries;
    }

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of DiscountEntry objects
     * Used before Firebase intergration
     */
    public static List<DiscountEntry> initProductEntryList(Resources resources) {

        InputStream inputStream = resources.openRawResource(R.raw.offers);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonProductsString = writer.toString();
        Gson gson = new Gson();
        Type productListType = new TypeToken<ArrayList<DiscountEntry>>() {
        }.getType();
        return gson.fromJson(jsonProductsString, productListType);
    }


}