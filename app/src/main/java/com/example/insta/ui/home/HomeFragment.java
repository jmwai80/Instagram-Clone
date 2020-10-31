package com.example.insta.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.Post;
import com.example.insta.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class HomeFragment extends Fragment
{
    public static final String TAG = "PostsFragment";

    private RecyclerView rvPosts;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.rvPosts);

        rvPosts = view.findViewById(R.id.rvPosts);
        queryPosts();
    }

    private void queryPosts()
    {
        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);

        postQuery.include(Post.KEY_USER);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                for(int i = 0; i < objects.size(); i++)
                {
                    Post post = objects.get(i);
                }
            }
        });
    }
}