package com.omarstudiolimited.nav_t2.ui.MoreInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.omarstudiolimited.nav_t2.MainActivity;
import com.omarstudiolimited.nav_t2.R;
import com.omarstudiolimited.nav_t2.ui.AboutApp.AboutAppViewModel;
import com.omarstudiolimited.nav_t2.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.omarstudiolimited.nav_t2.R.*;

public class MoreInfoFragment extends Fragment {

    private com.omarstudiolimited.nav_t2.ui.AboutApp.AboutAppViewModel AboutAppViewModel;

    View view;
    TextView toolbarTextView;
    ImageView toolbarImageView,img;
    ImageView toolbarImageViewBack;
    TextView MoreInfoTextView;
    String Key_Value;
    String result_val;
    ProgressDialog progressDoalog;
    String Code;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(layout.fragment_moreinfo, container, false);

        toolbarTextView  = (TextView) ((MainActivity) this.getActivity()).findViewById(id.tbtv);
        toolbarTextView.setText("More Info");
        toolbarImageView  = (ImageView) ((MainActivity) this.getActivity()).findViewById(id.right_imagV);
        toolbarImageView.setImageResource(drawable.ic_question);


        img=view.findViewById(id.img);
        Key_Value = getArguments().getString("Code");
        MoreInfoTextView=view.findViewById(id.MoreInfoTextView);
        sendToServer();
        return view;
    }
    public void sendToServer (){
        ShowProgress(); // Show The Progress Dialog While Fetching
        result_val=Key_Value;
        result_val = result_val.replace(" ", "");
        String url = "http://momenghazouli.pythonanywhere.com/?input=" + result_val;
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Code = response.getString("Code");
                    String NameOfMedicine = response.getString("NameOfMedicine");
                    String Concentration = response.getString("Concentration");
                    String Uses = response.getString("Uses");
                    String SidesEffect = response.getString("SidesEffect");
                    String DrugInteractions = response.getString("DrugInteractions");
                    String ContraindicationsToUse = response.getString("ContraindicationsToUse");
                    String Shape = response.getString("Shape");
                    String Company = response.getString("Company");
                    String prescription = response.getString("prescription");
                    String Price = response.getString("Price");
                    String ProbabilityOfMatching = response.getString("ProbabilityOfMatching");
                    if (Code.length()>=1) {
                        HideProgress(); // Hied Progress Dialog
//                         Show info in Text View
                        MoreInfoTextView.setText(
                                            "Rsult"+"\n"+
                                                    "Drug Code: " + Code + "\n"
                                                    + "Name Of Medicine: " + NameOfMedicine + "\n"
                                                    + "Concentration: " + Concentration + "\n"
                                                    + "Uses: " + Uses + "\n"
                                                    + "Sides Effect: " + SidesEffect + "\n"
                                                    + "Drug Interactions: " + DrugInteractions + "\n"
                                                    + "Contraindications To Use: " + ContraindicationsToUse + "\n"
                                                    + "Shape: " + Shape + "\n"
                                                    + "Company :" + Company + "\n"
                                                    + "Prescription: " + prescription + "\n"
                                                    +"Price: "+ Price + "\n"
                                                    + "Matching Probability: "+ProbabilityOfMatching);
                    }
                    if (Code.length()==0){
                        HideProgress();
                    }
                    Images(); // Image Chooser
                } catch (JSONException e) {
                    Snackbar.make(view, "Error Sending to Server", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HideProgress();
                Snackbar.make(view, "Server Did not Give Any FeedBack", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jor);
    } // Send To Server Method End

    public void ShowProgress(){
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setTitle("Detecting");
        progressDoalog.setMessage("Loading Content...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(Color.parseColor("#D81B60"),
                PorterDuff.Mode.SRC_IN);
        progressDoalog.setIndeterminateDrawable(drawable);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    } // ShowProgress end
    public void HideProgress(){
        progressDoalog.dismiss();
    }// HideProgress End


    // Drugs Images
    public void Images (){
        if (Code.equalsIgnoreCase("res2")){
            img.setBackgroundResource(drawable.res2_4mg);
        }
        // H2 And HI are The Same
        if (Code.equalsIgnoreCase("H2")){
            img.setBackgroundResource(drawable.h2);
        }
        if (Code.equalsIgnoreCase("HI")){
            img.setBackgroundResource(drawable.hi);
        }
        ///////////////////////////////
        if (Code.equalsIgnoreCase("H29")){
            img.setBackgroundResource(drawable.h29_2);
        }
        if (Code.equalsIgnoreCase("APM128")){
            img.setBackgroundResource(drawable.apm128);
        }
        if (Code.equalsIgnoreCase("H808")){
            img.setBackgroundResource(drawable.h808);
        }
        // Same Durg
        if (Code.equalsIgnoreCase("4223")){
            img.setBackgroundResource(drawable.x4223);
        }
        if (Code.equalsIgnoreCase("5/1000")){
            img.setBackgroundResource(drawable.x4223);
        }
        ///////////////////////////
        if (Code.equalsIgnoreCase("R701")){
            img.setBackgroundResource(drawable.r701);
        }
        if (Code.equalsIgnoreCase("deva")){
            img.setBackgroundResource(drawable.deva);
        }
        if (Code.equalsIgnoreCase("p53")){
            img.setBackgroundResource(drawable.p53);
        }
        if (Code.equalsIgnoreCase("MENOPACE VITABIOTICS")){
            img.setBackgroundResource(drawable.long2);
        }
        //////////////////
        if (Code.equalsIgnoreCase("GLA")){
            img.setBackgroundResource(drawable.gla);
        }
        if (Code.equalsIgnoreCase("p80")){
            img.setBackgroundResource(drawable.p80);
        }
        ////// Same Drug
        if (Code.equalsIgnoreCase("1428")){
            img.setBackgroundResource(drawable.x1428);
        }
        if (Code.equalsIgnoreCase("10")){
            img.setBackgroundResource(drawable.x1428);
        }
        /////////////////////////
        if (Code.equalsIgnoreCase("Diclogesic r 100")){
            img.setBackgroundResource(drawable.diclogesic_100);
        }
        //// samu drug
        if (Code.equalsIgnoreCase("NC2")){
            img.setBackgroundResource(drawable.nc2);
        }
        if (Code.equalsIgnoreCase("PHI")){
            img.setBackgroundResource(drawable.nc2);
        }
        //////////////////////////////
        if (Code.equalsIgnoreCase("CCH")){
            img.setBackgroundResource(drawable.cch);
        }
        //// Same Drug
        if (Code.equalsIgnoreCase("H")){
            img.setBackgroundResource(drawable.h);
        }
        if (Code.equalsIgnoreCase("50")){
            img.setBackgroundResource(drawable.h);
        }
        /////////////////////////////////////////
        if (Code.equalsIgnoreCase("C155")){
            img.setBackgroundResource(drawable.c155);
        }
        if (Code.equalsIgnoreCase("256")){
            img.setBackgroundResource(drawable.x256);
        }
        if (Code.equalsIgnoreCase("APM134")){
            img.setBackgroundResource(drawable.apm134);
        }
        if (Code.equalsIgnoreCase("h36")){
            img.setBackgroundResource(drawable.nidazole_h36);
        }
        /// same
        if (Code.equalsIgnoreCase("P70")){
            img.setBackgroundResource(drawable.panda1000);
        }
        if (Code.equalsIgnoreCase("JOSWE")){
            img.setBackgroundResource(drawable.panda1000);
        }
        if (Code.equalsIgnoreCase("PANDA-1000")){
            img.setBackgroundResource(drawable.panda1000);
        }
        //////////////////////////
    }//images end

} // Fragment End