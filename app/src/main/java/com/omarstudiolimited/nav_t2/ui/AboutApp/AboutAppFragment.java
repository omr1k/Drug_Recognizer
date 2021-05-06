package com.omarstudiolimited.nav_t2.ui.AboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.omarstudiolimited.nav_t2.MainActivity;
import com.omarstudiolimited.nav_t2.R;

import static com.omarstudiolimited.nav_t2.R.*;

public class AboutAppFragment extends Fragment {

    private AboutAppViewModel AboutAppViewModel;

    View view;
    TextView toolbarTextView;
    ImageView toolbarImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout.fragment_aboutapp, container, false);

        toolbarTextView  = (TextView) ((MainActivity) this.getActivity()).findViewById(id.tbtv);
        toolbarTextView.setText("About App");
        toolbarImageView  = (ImageView) ((MainActivity) this.getActivity()).findViewById(id.right_imagV);
        toolbarImageView.setImageResource(drawable.ic_question);

        return view;
    }
}