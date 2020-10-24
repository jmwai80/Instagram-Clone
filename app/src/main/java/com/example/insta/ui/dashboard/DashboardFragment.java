package com.example.insta.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.insta.Post;
import com.example.insta.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private EditText etDescription;
    private Button btnCaptureImage;
    public ImageView ivPostImage;
    private Button btnSubmit;
    public  File photoFile;
    public String photoFileName = "photo.jpg";

    public static final int CAPTURE_IMAGE_FRAGMENT_REQUEST_CODE = 42;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upload, container, false);

        etDescription = root.findViewById(R.id.etDescription);

        btnCaptureImage = root.findViewById(R.id.btnImage);
        btnSubmit = root.findViewById(R.id.btnPost);
        ivPostImage = root.findViewById(R.id.ivPostImage);
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();

                if(description.isEmpty())
                {
                    Toast.makeText(getActivity(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(photoFile == null || ivPostImage.getDrawable() == null)
                {
                    Toast.makeText(getActivity(), "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });

        return root;
    }

    private void launchCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_FRAGMENT_REQUEST_CODE);
        }
        etDescription.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        btnCaptureImage.setVisibility(View.INVISIBLE);
    }

    public File getPhotoFileUri(String fileName)
    {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MainActivity");

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdir())
        {
            Log.d(getTag(), "Failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_FRAGMENT_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                if (takenImage.getWidth() >= takenImage.getHeight()){

                    takenImage = Bitmap.createBitmap(
                            takenImage,
                            takenImage.getWidth()/2 - takenImage.getHeight()/2,
                            0,
                            takenImage.getHeight(),
                            takenImage.getHeight()
                    );

                }else{

                    takenImage = Bitmap.createBitmap(
                            takenImage,
                            0,
                            takenImage.getHeight()/2 - takenImage.getWidth()/2,
                            takenImage.getWidth(),
                            takenImage.getWidth()
                    );
                }
                ivPostImage.setImageBitmap(takenImage);
            }
            else
            {
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost(String description, ParseUser currentUser, File photoFile)
    {
        Post post = new Post();
        post.setKeyDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                {
                    Log.e(getTag(), "error while saving", e);
                    Toast.makeText(getActivity(), "error while saving", Toast.LENGTH_SHORT).show();
                }

                Log.i(getTag(), "Post save was successful!!");
                etDescription.setText("");
                ivPostImage.setImageResource(0);
                btnSubmit.setVisibility(View.INVISIBLE);
                btnCaptureImage.setVisibility(View.VISIBLE);
                etDescription.setVisibility(View.INVISIBLE);
            }
        });
    }

}