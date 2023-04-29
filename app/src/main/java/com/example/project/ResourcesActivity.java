package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResourcesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        Picasso.get().setIndicatorsEnabled(true);
        Picasso.get().setLoggingEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Video> videos = new ArrayList<>();
        videos.add(new Video("How I stay productive 98% of every day.", "https://i.ytimg.com/vi/cfiw3lwrkp0/maxresdefault.jpg", "cfiw3lwrkp0", "https://www.youtube.com/watch?v=cfiw3lwrkp0"));
        videos.add(new Video("BUILD A ROUTINE : How to be more productive", "https://i.ytimg.com/vi/2Q-qeX_WVVU/maxresdefault.jpg", "2Q-qeX_WVVU", "https://www.youtube.com/watch?v=2Q-qeX_WVVU"));
        videos.add(new Video("Top 5 Productivity Tips for Work!", "https://i.ytimg.com/vi/1LOlJay5Sbw/maxresdefault.jpg", "1LOlJay5Sbw", "https://www.youtube.com/watch?v=1LOlJay5Sbw"));
        videos.add(new Video("12 Habits To Become More Productive", "https://i.ytimg.com/vi/TCluJwxMncE/maxresdefault.jpg", "TCluJwxMncE", "https://www.youtube.com/watch?v=TCluJwxMncE"));
        videos.add(new Video("How To TRIPLE Your Productivity - The Magic Formula", "https://i.ytimg.com/vi/xX8sC9O6O-8/maxresdefault.jpg", "xX8sC9O6O-8", "https://www.youtube.com/watch?v=xX8sC9O6O-8"));
        videos.add(new Video("watch this if you always procrastinate", "https://i.ytimg.com/vi/sYi5kaUVdco/maxresdefault.jpg", "sYi5kaUVdco", "https://www.youtube.com/watch?v=sYi5kaUVdco"));
        videos.add(new Video("Why you procrastinate even when it feels bad", "https://i.ytimg.com/vi/FWTNMzK9vG4/maxresdefault.jpg", "FWTNMzK9vG4", "https://www.youtube.com/watch?v=FWTNMzK9vG4"));


        mAdapter = new VideoAdapter(videos);

        mRecyclerView.setAdapter(mAdapter);
    }

    public static class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

        private ArrayList<Video> mVideos;

        public VideoAdapter(ArrayList<Video> videos) {
            mVideos = videos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Video video = mVideos.get(position);
            holder.videoTitle.setText(video.getTitle());
            Picasso.get().load(video.getThumbnailUrl()).into(holder.thumbnailImage);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideoUrl()));
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mVideos.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView thumbnailImage;
            public TextView videoTitle;

            public ViewHolder(View itemView) {
                super(itemView);
                thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
                videoTitle = itemView.findViewById(R.id.video_title);
            }
        }
    }
}
