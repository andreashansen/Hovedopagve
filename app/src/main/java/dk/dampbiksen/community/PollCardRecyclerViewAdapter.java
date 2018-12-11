package dk.dampbiksen.community;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dk.dampbiksen.community.network.ImageRequester;
import dk.dampbiksen.community.network.PollEntry;
import dk.dampbiksen.community.network.ProductEntry;

/**
 * Adapter used to show a simple grid of products.
 */
public class PollCardRecyclerViewAdapter extends RecyclerView.Adapter<PollCardViewHolder> {

    private List<PollEntry> productList;
    private ImageRequester imageRequester;

    PollCardRecyclerViewAdapter(List<PollEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public PollCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_util_poll_card, parent, false);
        return new PollCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PollCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            PollEntry pollContender = productList.get(position);
            holder.productTitle.setText(pollContender.title);
            holder.productDesc.setText(pollContender.description);
            imageRequester.setImageFromUrl(holder.productImage, pollContender.url);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
