package com.example.quiz5.Gallery;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Data.PhotoInfoRepository;
import com.example.quiz5.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<PhotoInfo> photoList;
    private PhotoInfoRepository repo;
    private Context mContext;
    private Application application;

    //Constructor for the GalleryAdapter
    public GalleryAdapter(Application application, List<PhotoInfo> photoList) {
        this.photoList = photoList;
        this.application = application;
        this.repo = new PhotoInfoRepository(application);
        this.mContext = application.getApplicationContext(); // Getting application context
    }


    // Called whenevere the recyclerView needs a new RecyclewView.viewholder
    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creates a new view using the LayoutInlfater.from(and inflates with the item_gallery xml file we have created)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        // binds the data to the corresponding item in the recyclerview
        holder.bind(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void delete(int position) {
        // Deletes an image from the database
        repo.delete(photoList.get(position));
        notifyItemRemoved(position);
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nameTextView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            itemView.findViewById(R.id.GalleryDelete_Button).setOnClickListener(this);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        // Sets the data of the imageView and the nameTextview to be the values stored in the photoInfo object
        public void bind(PhotoInfo photoInfo) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(photoInfo.getImageData(), 0, photoInfo.getImageData().length));
            nameTextView.setText(photoInfo.getName());
        }

        //Sets an onClickListener to the deleteButton, tells the button to call the deleteMethod from the GalleryAdapter class
        @Override
        public void onClick(View v) {
            delete(getAdapterPosition());
        }
    }
}
