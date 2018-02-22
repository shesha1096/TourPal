package com.example.shesha.tourpal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shesha.tourpal.R;

import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shesha on 25-01-2018.
 */

public class ImageRecognition extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE  =1;
    public ImageView cameraview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_recognition,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonid =  (Button) view.findViewById(R.id.myCameraID);
        cameraview = (ImageView) view.findViewById(R.id.cameraimgid);
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            buttonid.setEnabled(false);
        }
        buttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

            }
        });

    }



   
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            cameraview.setImageBitmap(photo);
        }
    
    }
}
