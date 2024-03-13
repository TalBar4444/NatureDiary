package com.myapps.growdiary.adapters;

import android.content.Context;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.myapps.growdiary.fragments.EnlargedPhotoFragment;
import com.myapps.growdiary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<String> imageURIs;
    private Context context;
    public static boolean isFragmentOpen = false;
    public GalleryAdapter(Context context, List<String> imageURIs) {
        this.context = context;
        this.imageURIs = imageURIs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageURI = imageURIs.get(position);
        Picasso.get().load(imageURI).into(holder.itemGallery_IMG_singlePhoto);
        holder.itemGallery_IMG_singlePhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("pttt", "Adapter isFragmentOpen: " + isFragmentOpen);
                if (!isFragmentOpen) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            openPhotoFragment(imageURI);
                            isFragmentOpen = true;
                            return true;
                        default:
                            return false;

                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageURIs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemGallery_IMG_singlePhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGallery_IMG_singlePhoto = itemView.findViewById(R.id.itemGallery_IMG_singlePhoto);
        }
    }
    private void openPhotoFragment(String imageURI) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EnlargedPhotoFragment fragment = EnlargedPhotoFragment.newInstance(imageURI);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
