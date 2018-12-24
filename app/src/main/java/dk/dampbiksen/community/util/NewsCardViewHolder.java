package dk.dampbiksen.community.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import dk.dampbiksen.community.R;

public class NewsCardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView imgurl0;
    public NetworkImageView imgurl1;
    public NetworkImageView imgurl2;
    public TextView newsTitle;
    public TextView text;
    public TextView text1;


    public NewsCardViewHolder(@NonNull View itemView) {
        super(itemView);
        newsTitle = itemView.findViewById(R.id.news_text_title);
        imgurl0 = itemView.findViewById(R.id.news_image_top);
        text = itemView.findViewById(R.id.news_text_first);
        imgurl1 = itemView.findViewById(R.id.news_image_mid);
        text1 = itemView.findViewById(R.id.news_text_last);
        imgurl2 = itemView.findViewById(R.id.news_image_bottom);
    }
}
