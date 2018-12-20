package dk.dampbiksen.community.util;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import dk.dampbiksen.community.R;
import dk.dampbiksen.community.models.Vote;
import dk.dampbiksen.community.network.ImageRequester;
import dk.dampbiksen.community.models.ProductEntry;

import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class ProductCardRVAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {

    private List<ProductEntry> productList;
    private ImageRequester imageRequester;

    public ProductCardRVAdapter(List<ProductEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
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
            ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productDiscountCode = product.discountcode;
            imageRequester.setImageFromUrl(holder.productImage, product.url);

            holder.productDiscountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), holder.productDiscountCode,
                            Toast.LENGTH_LONG).show();


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
