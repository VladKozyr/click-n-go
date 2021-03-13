package com.clickandgo.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clickandgo.R;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListItemViewHolder> {

    @NonNull
    @Override
    public ProfileListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
        return new ProfileListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ProfileListItemViewHolder extends RecyclerView.ViewHolder {

        public ProfileListItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }




    }


}
