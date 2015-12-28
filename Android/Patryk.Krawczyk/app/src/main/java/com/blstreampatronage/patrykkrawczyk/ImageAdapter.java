package com.blstreampatronage.patrykkrawczyk;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;


public class ImageAdapter extends android.widget.BaseAdapter
{
    private ArrayList<SingleImage> mImages;
    private LayoutInflater         mImageInflater;
    private Context                mContext;

    public ImageAdapter(Context c, ArrayList<SingleImage> ims)
    {
        mContext = c;
        mImageInflater = LayoutInflater.from(mContext);
        mImages = ims;
    }

    @Override
    public int getCount() { return mImages.size(); }

    public void setImages(ArrayList<SingleImage> ims) { mImages = ims; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout     imageLayout     = (LinearLayout)     mImageInflater.inflate(R.layout.image, parent, false);
        NetworkImageView imageView       = (NetworkImageView) imageLayout.findViewById(R.id.imageView);
        TextView         titleView       = (TextView)         imageLayout.findViewById(R.id.imageTitle);
        TextView         descriptionView = (TextView)         imageLayout.findViewById(R.id.imageDescription);

        SingleImage current = mImages.get(position);

        titleView.setText(current.getTitle());
        descriptionView.setText(current.getDesc());
        imageView.setDefaultImageResId(R.drawable.defaultimage);
        imageView.setErrorImageResId(R.drawable.errorimage);
        imageView.setImageUrl(current.getUrl(), ConnectionHandler.getInstance(mContext).getImageLoader());

        imageLayout.setTag(position);

        return imageLayout;
    }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int position) { return 0; }
}
