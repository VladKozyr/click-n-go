package com.clickandgo.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clickandgo.databinding.SearchResultBinding;
import com.clickandgo.model.PlaceResult;

import java.util.ArrayList;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListItemViewHolder> implements FavoritesToggleListener {

    private final List<PlaceResult> mPlaceList = new ArrayList<>();
    private final FavoritesToggleListener listener;

    public ProfileListAdapter(FavoritesToggleListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchResultBinding binding = SearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setPresenter(this);
        return new ProfileListItemViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListItemViewHolder holder, int position) {
        PlaceResult result = mPlaceList.get(position);
        holder.binding.setPlaceResult(result);
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public PlaceResult getItemAt(int position) {
        if (position < 0 || position >= mPlaceList.size()) return null;

        return mPlaceList.get(position);
    }

    @Override
    public void onClicked(PlaceResult result) {
        listener.onClicked(result);
    }

    public void update(List<PlaceResult> newList) {
        mPlaceList.clear();
        mPlaceList.addAll(newList);
        notifyDataSetChanged();
    }

    static class ProfileListItemViewHolder extends RecyclerView.ViewHolder {

        SearchResultBinding binding;

        public ProfileListItemViewHolder(@NonNull View itemView, SearchResultBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}
