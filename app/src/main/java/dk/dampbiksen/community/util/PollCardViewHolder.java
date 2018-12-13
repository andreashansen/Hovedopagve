package dk.dampbiksen.community.util;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import dk.dampbiksen.community.R;

public class PollCardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView productImage;
    public TextView productTitle;
    public TextView productDesc;
    public MaterialButton voteButton;


    public PollCardViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productDesc = itemView.findViewById(R.id.product_desc);
        voteButton = itemView.findViewById(R.id.button_vote);

    }
}
