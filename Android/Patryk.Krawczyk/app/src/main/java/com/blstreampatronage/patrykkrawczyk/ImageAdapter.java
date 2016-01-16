package com.blstreampatronage.patrykkrawczyk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

/**
 * Adapter used to display images
 * Set CLEAN_CACHE_EVERY_LOAD to true if you wish cache to clean with each image load
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
{
    private ArrayList<SingleImage>    mImages;
    private ProgressBar               mProgressBar;
    private final LayoutInflater      mImageInflater;
    private final ImageLoader         imageLoader;
    private final DisplayImageOptions displayImageOptions;

    // Will slowdown app, every time image is loaded, it is first cleaned from cache
    private static final boolean   CLEAN_CACHE_EVERY_LOAD = false;

    /**
     * Constructor for adapter, initializes fields and customizes display ImageOptions for UIL
     * @param activity the activity where you use this adapter
     * @param ims list of SingleImage that will be used to inflate the Views
     */
    public ImageAdapter(Activity activity, ArrayList<SingleImage> ims)
    {
        mImageInflater = LayoutInflater.from(activity.getApplicationContext());
        mImages        = ims;
        imageLoader    = ImageLoader.getInstance();
        mProgressBar   = (ProgressBar) activity.findViewById(R.id.progressBar);

        displayImageOptions = new DisplayImageOptions.Builder()
                                                     .showImageOnLoading  (R.drawable.loadingimage)
                                                     .showImageForEmptyUri(R.drawable.emptyimage)
                                                     .showImageOnFail     (R.drawable.errorimage)
                                                     .cacheInMemory(true)
                                                     .cacheOnDisk(true)
                                                     .bitmapConfig(Bitmap.Config.RGB_565)
                                                     .build();
    }

    /**
     * Inflates View and returns a ViewHolder pointing to it
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View       view   = mImageInflater.inflate(R.layout.image, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * Prepared one image with information, uses UIL to download image
     * @param viewHolder that will be used to set up image
     * @param i the iterator of current image
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.d("TAG", "Loading " + i);
        SingleImage current = mImages.get(i);

        viewHolder.title.setText(current.getTitle());
        viewHolder.description.setText(current.getDesc());

        if (CLEAN_CACHE_EVERY_LOAD) {
            MemoryCacheUtils.removeFromCache(current.getUrl(), ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache(current.getUrl(), ImageLoader.getInstance().getDiskCache());
        }

        imageLoader.displayImage(current.getUrl(), viewHolder.image, displayImageOptions, new ImageLoadingListener(viewHolder.progressBar));
    }

    /**
     * Listener that allows control of the progress bar of each image
     */
    private class ImageLoadingListener extends SimpleImageLoadingListener {
        private ProgressBar progressBar;

        public ImageLoadingListener(ProgressBar pg) { progressBar = pg; }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            progressBar.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Should return size of list of provided images
     * @return size of list of provided images
     */
    @Override
    public int getItemCount() {
        return mImages.size();
    }

    /**
     * Class used by onCreateViewHolder to prepare single image
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView    image;
        public TextView     title;
        public TextView     description;
        public ProgressBar  progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            image       = (ImageView)   itemView.findViewById(R.id.imageView);
            title       = (TextView)    itemView.findViewById(R.id.imageTitle);
            description = (TextView)    itemView.findViewById(R.id.imageDescription);
            progressBar = (ProgressBar) itemView.findViewById(R.id.imageProgressBar);
        }
    }
}
