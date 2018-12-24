package dk.dampbiksen.community.util;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import dk.dampbiksen.community.R;
import dk.dampbiksen.community.models.NewsEntry;
import dk.dampbiksen.community.network.ImageRequester;

/**
 * Adapter used to show a simple grid of products.
 */
public class NewsCardRVAdapter extends RecyclerView.Adapter<NewsCardViewHolder> {

    private List<NewsEntry> productList;
    private ImageRequester imageRequester;

    public NewsCardRVAdapter(List<NewsEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();

    }

    @NonNull
    @Override
    public NewsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_util_news_card, parent, false);
        return new NewsCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            final NewsEntry product = productList.get(position);
            holder.newsTitle.setText(product.title);
            holder.text.setText(product.text);
            holder.text1.setText(product.text1);
            imageRequester.setImageFromUrl(holder.imgurl0, product.imgurl0);
            imageRequester.setImageFromUrl(holder.imgurl1, product.imgurl1);
            imageRequester.setImageFromUrl(holder.imgurl2, product.imgurl2);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
