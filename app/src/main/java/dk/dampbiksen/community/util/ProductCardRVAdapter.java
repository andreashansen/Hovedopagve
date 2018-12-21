package dk.dampbiksen.community.util;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.dampbiksen.community.R;
import dk.dampbiksen.community.models.Vote;
import dk.dampbiksen.community.network.ImageRequester;
import dk.dampbiksen.community.models.ProductEntry;

import java.util.Calendar;
import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class ProductCardRVAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    public ProductCardRVAdapter(List<ProductEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_util_product_card, parent, false);
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            final ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productDiscountCode = product.discountcode;
            holder.productDiscountId = product.discountid;
            imageRequester.setImageFromUrl(holder.productImage, product.url);

            ValueEventListener discountListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    String discounted = dataSnapshot.getValue(String.class);

                    if (discounted == null)
                    {
                        holder.productDiscountButton.setClickable(true);
                    }
                    else
                    {
                        if(discounted.equalsIgnoreCase(product.discountcode))
                        {
                            MaterialButton mb = holder.productDiscountButton;
                            mb.setBackgroundTintMode(PorterDuff.Mode.ADD);
                            mb.setText("Brugt");
                            holder.productDiscountButton.setClickable(false);

                        }

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("db-read", "loadPost:onCancelled", databaseError.toException());

                }
            };
            myRef = database.getReference("Discounts/"+holder.productDiscountId +"/"+ holder.productTitle.getText()+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
            myRef.addValueEventListener(discountListener);





            holder.productDiscountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), holder.productDiscountId + holder.productTitle.getText(),
                            Toast.LENGTH_LONG).show();

                    // Write a message to the database
                    myRef = database.getReference("Discounts/"+holder.productDiscountId +"/"+ holder.productTitle.getText()+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    myRef.setValue(holder.productDiscountCode);



                    MaterialButton mb  = holder.productDiscountButton;
                    mb.setBackgroundTintMode(PorterDuff.Mode.ADD);
                    mb.setText("Brugt");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
