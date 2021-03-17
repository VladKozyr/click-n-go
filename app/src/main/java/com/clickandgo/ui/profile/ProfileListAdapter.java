package com.clickandgo.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clickandgo.databinding.SearchResultBinding;
import com.clickandgo.model.PlaceResult;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListItemViewHolder> {

    private final List<PlaceResult> mPlaceList;
    private final LayoutInflater inflater;
    private final Context mContext;

    public ProfileListAdapter(Context context, List<PlaceResult> mPlaceList) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.mPlaceList = mPlaceList;
    }

    @NonNull
    @Override
    public ProfileListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchResultBinding binding = SearchResultBinding.inflate(inflater, parent, false);
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

    public void clear() {
        mPlaceList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<PlaceResult> list) {
        mPlaceList.addAll(list);
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
