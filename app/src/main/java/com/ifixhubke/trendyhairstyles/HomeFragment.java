package com.ifixhubke.trendyhairstyles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ItemClickListener{

    RecyclerView recyclerView;
    private List<Post> postList;
    PostsAdapter adapter;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences  = this.getActivity().getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPref sharedPref = new SharedPref();
        String sharedValue = sharedPreferences.getString(sharedPref.username,"");

        FloatingActionButton createPost = view.findViewById(R.id.add_post_fab);
        progressBar = view.findViewById(R.id.recycler_progressbar);

        postList = new ArrayList<>();

        fetchPosts(view);

        createPost.setOnClickListener(v ->{
            TextView txt = view.findViewById(R.id.profile_name);
            HomeFragmentDirections.ActionHomeFragmentToPostFragment action = HomeFragmentDirections.actionHomeFragmentToPostFragment(sharedValue);
            NavHostFragment.findNavController(HomeFragment.this).navigate(action);
        });

        return view;
    }

    private void initializeRecycler(View view){
        recyclerView = view.findViewById(R.id.posts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(postList, getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchPosts(View view){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        Post post = i.getValue(Post.class);
                        postList.add(post);
                        progressBar.setVisibility(View.INVISIBLE);
                        initializeRecycler(view);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void likePost(Post post, int position) {
        Toast.makeText(getContext(), "like post "+ post.getLikes(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void savePost(Post post, int position) {
        Toast.makeText(getContext(), "saved post "+post.getStyle_photo_url() +" "+ post.getStyle_name() +" "+
                post.getSalon_name() +" "+ post.getStyle_price(), Toast.LENGTH_LONG).show();
    }
}