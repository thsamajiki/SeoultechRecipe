package com.seoultech.recipeschoolproject.view.main.recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seoultech.recipeschoolproject.R;
import com.seoultech.recipeschoolproject.database.FirebaseData;
import com.seoultech.recipeschoolproject.listener.OnCompleteListener;
import com.seoultech.recipeschoolproject.listener.OnRatingUploadListener;
import com.seoultech.recipeschoolproject.listener.OnRecyclerItemClickListener;
import com.seoultech.recipeschoolproject.listener.Response;
import com.seoultech.recipeschoolproject.view.post.PostActivity;
import com.seoultech.recipeschoolproject.vo.RecipeData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RecipeFragment extends Fragment implements View.OnClickListener, OnRecyclerItemClickListener<RecipeData>, OnRatingUploadListener {

    private RecyclerView recipeRecycler;
    private FloatingActionButton btnPost;
    private RecipeAdapter recipeAdapter;
    private ArrayList<RecipeData> recipeDataArrayList = new ArrayList<>();
    private static final int POST_REQ_CODE = 333;
    public static final String EXTRA_RECIPE_DATA = "recipeData";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecipeAdapter();
        downloadRecipeData();
    }

    private void initView(View view) {
        recipeRecycler = view.findViewById(R.id.recycler_recipe);
        btnPost = view.findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);
    }

    private void setRecipeAdapter() {
        recipeAdapter = new RecipeAdapter(requireActivity(), recipeDataArrayList);
        recipeAdapter.setOnRecyclerItemClickListener(this);
        recipeRecycler.setAdapter(recipeAdapter);
    }

    private void downloadRecipeData() {
        FirebaseData.getInstance().downloadRecipeData(new OnCompleteListener<ArrayList<RecipeData>>() {
            @Override
            public void onComplete(boolean isSuccess, Response<ArrayList<RecipeData>> response) {
                if (isSuccess && response.isNotEmpty()) {
                    recipeDataArrayList.clear();
                    recipeDataArrayList.addAll(response.getData());
                    recipeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_post:
                Intent intent = new Intent(requireActivity(), PostActivity.class);
                startActivityForResult(intent, POST_REQ_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POST_REQ_CODE && resultCode == RESULT_OK && data != null) {
            RecipeData recipeData = data.getParcelableExtra(EXTRA_RECIPE_DATA);
            if (recipeData != null) {
                recipeDataArrayList.add(0, recipeData);
                recipeAdapter.notifyItemInserted(0);
                recipeRecycler.smoothScrollToPosition(0);
            }

        }
    }

    @Override
    public void onItemClick(int position, View view, RecipeData data) {
        if (view.getId() == R.id.cv_rating_container) {
            RatingDialog ratingDialog = new RatingDialog(requireActivity());
            ratingDialog.setOnRatingUploadListener(this);
            ratingDialog.setRecipeData(data);
            ratingDialog.show();
        } else {
            Intent intent = new Intent(requireActivity(), DetailActivity.class);
            intent.putExtra(EXTRA_RECIPE_DATA, data);
            startActivity(intent);
        }
    }

    @Override
    public void onRatingUpload(RecipeData recipeData) {
        int index = recipeDataArrayList.indexOf(recipeData);
        recipeDataArrayList.set(index, recipeData);
        recipeAdapter.notifyItemChanged(index);
    }
}