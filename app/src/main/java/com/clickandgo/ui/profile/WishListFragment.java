package com.clickandgo.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.clickandgo.R;
import com.clickandgo.model.PlaceResult;
import com.clickandgo.utils.ItemClickSupport;

import java.util.LinkedList;
import java.util.List;

public class WishListFragment extends Fragment {

    private RecyclerView mWishRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<PlaceResult> results = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            results.add(new PlaceResult("Neproseco"));
        }

        mWishRecyclerView = view.findViewById(R.id.wish_list);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_wish_list);

        ProfileListAdapter adapter = new ProfileListAdapter(getContext(), results);
        mWishRecyclerView.setAdapter(adapter);
        mWishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupItemClickListeners();
        setupRefreshListeners();
    }

    private void setupRefreshListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            //TODO update from firestore
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupItemClickListeners() {
        ItemClickSupport.addTo(mWishRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.d("ITEM CLICK", "Item single clicked " + mWishRecyclerView);
                Uri address = Uri.parse("https://maps.google.com/?cid=10426385408524127842");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, address);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                Log.d("ITEM CLICK", "Item double clicked " + mWishRecyclerView);
            }
        });
    }
}
