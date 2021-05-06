package com.omarstudiolimited.nav_t2.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.snackbar.Snackbar;
import com.omarstudiolimited.nav_t2.MainActivity;
import com.omarstudiolimited.nav_t2.R;
import com.omarstudiolimited.nav_t2.ui.Devolopers.DevelopersFragment;
import com.omarstudiolimited.nav_t2.ui.MoreInfo.MoreInfoFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;

    View view;
    TextView toolbarTextView,resultTV;
    private static final int PICK_IMAGE=1;
    ImageView toolbarImageView;
    ImageView imageView;
    String result_val;
    ProgressDialog progressDoalog;
    String CurrentPhotoPath = null;
    TextToSpeech textToSpeech;

    Uri imageUri;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
//    private String pictureImagePath = "";
//    String CurrentImagePath=null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbarTextView  = (TextView) ((MainActivity) this.getActivity()).findViewById(R.id.tbtv);
        toolbarImageView  = (ImageView) ((MainActivity) this.getActivity()).findViewById(R.id.right_imagV);
        toolbarImageView.setImageResource(R.drawable.ic_house);
        toolbarTextView.setText("Home");
        resultTV=view.findViewById(R.id.tv);
        imageView=view.findViewById(R.id.img);

        // Open Gallery
        Button openGallery =(Button)view.findViewById(R.id.bg);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTV.setText("");
                Intent gg = new Intent();
                gg.setType("image/*");
                gg.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gg.createChooser(gg,"select"),PICK_IMAGE);
            }
        });

        // Open Camera
        Button Capture =(Button)view.findViewById(R.id.bc);
        Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "photo";
                File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile=File.createTempFile(filename,"jpg",storageDirectory);
                    CurrentPhotoPath = imageFile.getAbsolutePath();
                    Uri imageUri = FileProvider.getUriForFile(getActivity(),"com.omarstudiolimited.nav_t2.fileprovider",imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,101);
//                    Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(camera, 101);
                }catch (Exception e){
                    Toast.makeText(getActivity(),"الرجاء الموافقة على صلاحية الكاميرا والمحاولة مرة ثانية",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 5);
                }
            }
        });

        //Detecting Button (Run Detecting Algorithm)
        Button detect = view.findViewById(R.id.bd);
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();
                    if (!textRecognizer.isOperational())
                    {
                     Toast.makeText(getActivity().getApplicationContext(),"TextRecognizer is not Operational",Toast.LENGTH_SHORT).show();
                    }else {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = textRecognizer.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        for (int i=0;i<items.size();i++)
                        {
                            TextBlock myitem = items.valueAt(i);
                            sb.append(myitem.getValue());
                            sb.append("\n");
                        }
                        resultTV.setText(sb.toString());
                        result_val = sb.toString();
                        sendToServer(); // Run Send to Server Method
                        if (result_val.length() == 0){
                            Toast.makeText(getActivity().getApplicationContext(), "Can't Detect Text - لم يُستكشف نص", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e) {
                    Snackbar.make(view, "Please Select Or Capture Image", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        // Text To speech Function
        textToSpeech = new TextToSpeech(getActivity()
                , new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        return view;
    }
    // Handle The Picture and show it on The Main ImageView (Either From Gallery Or Camera)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(CurrentPhotoPath);
            rotateBitmap(bitmap);
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
        }
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            if(data != null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    try {
                        InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap2);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Rotate Image
    private void rotateBitmap(Bitmap bitmap){
        ExifInterface exifInterface=null;
        try {
            exifInterface=new ExifInterface(CurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orantition = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix= new Matrix();
        switch (orantition){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(90);
                break;
            default:
        }
        Bitmap rotateBitmap=bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        imageView.setImageBitmap(rotateBitmap);
    }

    public void sendToServer (){
                    ShowProgress(); // Show The Progress Dialog While Fetching
                    result_val = result_val.replace(" ", "");
                    String url = "http://momenghazouli.pythonanywhere.com/?input="+result_val;
                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String Code = response.getString("Code");
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
                                    // Show info in Normal Dialog
                                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                            .setIcon(R.drawable.ic_pills)
                                            .setTitle("Result")
                                            .setMessage(
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
                                                            + "Price: "+Price + "\n"
                                                            + "Matching Probability: "+ProbabilityOfMatching)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    textToSpeech.stop();
                                                    dialog.dismiss();
                                                }
                                            })
                                            //.create();
                                            .setNegativeButton("Need More Info ?", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            textToSpeech.stop(); // Stop Text To Speech
                                                            Fragment someFragment = new MoreInfoFragment();
                                                            Bundle args = new Bundle();
                                                            args.putString("Code", Code);
                                                            someFragment.setArguments(args);
                                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                            transaction.replace(R.id.nav_host_fragment, someFragment ); // give your fragment container id in first parameter
                                                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                                            transaction.commit();
                                                        }
                                                    }).show();
                                    // Reading The Text Part
                                    String TextToRead=
                                            "Drug Code: " + Code + "\n"
                                            +"Name Of Medicine: " + NameOfMedicine + "\n"
                                            + "Concentration: " + Concentration + "\n"
                                            + "Uses: " + Uses + "\n"
                                            + "Sides Effect: " + SidesEffect + "\n"
                                            + "Drug Interactions: " + DrugInteractions + "\n"
                                            + "Contraindications To Use: " + ContraindicationsToUse + "\n"
                                            + "Shape: " + Shape + "\n"
                                            + "Company :" + Company + "\n"
                                            + "Prescription: " + prescription + "\n"
                                            + "Price: "+Price + "\n"
                                            + "Matching Probability: "+ProbabilityOfMatching;

                                    int speech = textToSpeech.speak(TextToRead,TextToSpeech.QUEUE_FLUSH,null);

                                    // Show info in Text View
//                                    resultTV.setText(
//                                            "Rsult"+"\n"+
//                                                    "Drug Code: " + Code + "\n"
//                                                    + "Name Of Medicine: " + NameOfMedicine + "\n"
//                                                    + "Uses: " + Uses + "\n"
//                                                    + "Sides Effect: " + SidesEffect + "\n"
//                                                    + "Drug Interactions: " + DrugInteractions + "\n"
//                                                    + "Contraindications To Use: " + ContraindicationsToUse + "\n"
//                                                    + "Shape: " + Shape + "\n"
//                                                    + "Company :" + Company + "\n"
//                                                    + "Prescription: " + prescription);
                                }
                                if (Code.length()==0){
                                    HideProgress();
                                }
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
} // Home Fragment Class End


























//    // Dailog Void
//    public void dilog(){
//        AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                .setTitle("Result")
//                .setMessage(
//                        "Drug Code: "+Code+"\n"
//                        +"Name Of Medicine: "+NameOfMedicine+"\n"
//                        +"Uses: "+Uses+"\n"
//                        +"Sides Effect: "+SidesEffect+"\n"
//                        +"Drug Interactions: "+DrugInteractions+"\n"
//                        +"Contraindications To Use: "+ContraindicationsToUse+"\n"
//                        +"Shape: "+Shape+"\n"
//                        +"Company :"+Company+"\n"
//                        +"Prescription: "+prescription)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();
//    } // Diloge End


    // Eliminate ImageView To Save Some Space
//    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
//                                    imageView.setLayoutParams(layoutParams);
//Then Show THe Dialog







//        final ProgressDialog progressDoalog = new ProgressDialog(getActivity());
//        progressDoalog.setMax(100);
//        progressDoalog.setMessage("Please wait...");
//        progressDoalog.setTitle("Checking");
//        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
//        drawable.setColorFilter(Color.parseColor("#2764a5"),
//                PorterDuff.Mode.SRC_IN);
//        progressDoalog.setIndeterminateDrawable(drawable);
//        progressDoalog.setCancelable(false);
//        progressDoalog.show();


//                  String result_val = resultTV.getText().toString();



//    public void dilog(){
//        AlertDialog dialog = new AlertDialog.Builder(getActivity())
//                .setTitle("Result")
//                .setMessage("dfjdfhdhfo")
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();
//    }


