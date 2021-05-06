package com.omarstudiolimited.nav_t2.ui.Devolopers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.omarstudiolimited.nav_t2.MainActivity;
import com.omarstudiolimited.nav_t2.R;

public class DevelopersFragment extends Fragment {

    private DevelopersViewModel DevelopersViewModel;

    View view;
    TextView toolbarTextView;
    ImageView toolbarImageView;

    TextView omar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_developers, container, false);

        toolbarTextView  = (TextView) ((MainActivity) this.getActivity()).findViewById(R.id.tbtv);
        toolbarTextView.setText("Developers");
        toolbarImageView  = (ImageView) ((MainActivity) this.getActivity()).findViewById(R.id.right_imagV);
        toolbarImageView.setImageResource(R.drawable.ic_baseline_developer_mode_24);



        return view;
    }
}