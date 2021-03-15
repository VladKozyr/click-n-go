package com.clickandgo.ui.navigation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clickandgo.R;
import com.clickandgo.databinding.SearchResultBinding;
import com.clickandgo.model.PlaceResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseResultFragment extends Fragment {

    private static final String PLACES = "places";
    private static final String AMOUNT = "amount";
    private static final String TYPE = "type";
    private static final String LINK = "link";
    private static final String TAG = "RESULT";

    private TextView result;
    private LinearLayout layout;


    public ChooseResultFragment() {
        // Required empty public constructor
    }

    public static ChooseResultFragment newInstance(String param1, String param2) {
        ChooseResultFragment fragment = new ChooseResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = getView().findViewById(R.id.button);
        button.setOnClickListener(this::onFindClick);
        result = getView().findViewById(R.id.result);
        layout = getView().findViewById(R.id.result_layout);
    }

    public void onFindClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(PLACES)
                .document("Кловська")
                .collection("100")
                .whereArrayContains(TYPE, "Coffee")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder sb = new StringBuilder();
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d(PLACES, document.getId());
                            sb.append(document.getData().toString())
                                    .append(System.lineSeparator());
                        }
                        SearchResultBinding binding = SearchResultBinding.inflate(getLayoutInflater(), layout, false);
                        binding.setPlaceResult(new PlaceResult("Neproseco"));
                        binding.getRoot().setOnClickListener(v -> {
                            Uri address = Uri.parse("https://maps.google.com/?cid=10426385408524127842");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, address);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        });
                        layout.addView(binding.getRoot());
                    }
                });
        Geocoder geocoder = new Geocoder(getContext());
    }
}