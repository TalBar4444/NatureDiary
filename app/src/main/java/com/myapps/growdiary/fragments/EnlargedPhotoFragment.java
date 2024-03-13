package com.myapps.growdiary.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import com.myapps.growdiary.R;
import com.squareup.picasso.Picasso;

public class EnlargedPhotoFragment extends Fragment {

    private static final String ARG_IMAGE_URI = "image_uri";
    private String imageURI;

    public static EnlargedPhotoFragment newInstance(String imageURI) {
        EnlargedPhotoFragment fragment = new EnlargedPhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URI, imageURI);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageURI = getArguments().getString(ARG_IMAGE_URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enlarged_photo, container, false);
        ImageView enlargedImageView = view.findViewById(R.id.enlarged_photo_imageView);
        if (imageURI != null) {
            Picasso.get().load(imageURI).into(enlargedImageView);
        }
        return view;
    }
}

