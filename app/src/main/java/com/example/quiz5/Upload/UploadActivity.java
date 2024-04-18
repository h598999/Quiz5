package com.example.quiz5.Upload;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Data.PhotoInfoRepository;
import com.example.quiz5.R;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UploadActivity extends AppCompatActivity {

    private static final int YOUR_PERMISSION_REQUEST_CODE = 1;
    private Uri imageUri;

    private Button uploadButton;
    private Button submitButton;
    private Button goButton;
    private ImageView view;
    private EditText input;
    private PhotoInfoRepository repo;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle instanceViewState){
        super.onCreate(instanceViewState);
        setContentView(R.layout.upload_activity);

        repo = new PhotoInfoRepository(getApplication());
        uploadButton = findViewById(R.id.uploadPhoto_Button);
        submitButton = findViewById(R.id.submit_Button);
        goButton = findViewById(R.id.goButton);
        view = findViewById(R.id.imageView);
        input = findViewById(R.id.inputText_input);

        uploadButton.setOnClickListener( v -> openGallery());
        submitButton.setOnClickListener(v -> {
            try {
                submit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        goButton.setOnClickListener(v -> go());

    }

    // Starts an implicit intent given the parameters, the intent is meant to open the gallery for
    // retrieving an image from the users photo library
    private void openGallery(){
        Intent intent = new Intent();
        // Set the action to be Intent.ACTION_GET_CONTENT
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Set the category to be Intent.CATEGORY_OPENABLE
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Set the type to be "image/*"
        intent.setType("image/*");
        //Starts the activity for result
        startActivityForResult(intent, YOUR_PERMISSION_REQUEST_CODE);
    }

    //Finishes the uploadActicity and returns to the GalleryActivity
    private void go(){
        finish();
    }

    //
    private void submit() throws IOException {
        // Checks that there is a name and image selected
        String enteredText = getEnteredText();
        Uri imageUri = getUri();

        if (enteredText == null || imageUri == null){
            return;
        }

        //Inserts a new PhotoInfo object into the database with the given name, and the imagedata of the selected image
        repo.insert(new PhotoInfo(enteredText, this.convertUriToByteArray(imageUri)));
        Toast.makeText(this, "Image added", Toast.LENGTH_SHORT).show();
        //int uid = Binder.getCallingUid();
        //String callingPackage = getPackageManager().getNameForUid(uid);
        //getApplication().grantUriPermission(callingPackage, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        reset();
    }

    // Is called when pressing submit button, clears the view
    private void reset(){
        view = findViewById(R.id.imageView);
        view.setImageResource(0);
        input = findViewById(R.id.inputText_input);
        input.setText("");
        this.imageUri = null;
    }

    // Is called when the submit button is pressed, if the text entered is null or empty a error message will display
    private String getEnteredText(){
        input = findViewById(R.id.inputText_input);
        String enteredText = input.getText().toString();

        if (enteredText == null || enteredText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return null;
        }
        return enteredText;
    }
    // Is called when the submit button is pressed, if no image is selected an error message will display
    private Uri getUri(){
        if (this.imageUri == null){
            Toast.makeText(getApplicationContext(), "Please enter a image", Toast.LENGTH_SHORT).show();
            return null;
        }
        return this.imageUri;
    }

    // Is called when the implicit intent from the openGallery method is finished
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Checks that the correct request and resultcode and the data is not null
        if (requestCode == YOUR_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            //Sets the imageUri field to be the data returned by the intent
            imageUri = data.getData();
            Log.d("Image", "Image selected: " + imageUri.toString());
            //Sets the image in the imageView to be the picture selected
            view = findViewById(R.id.imageView);
            view.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing the image URI
                // For example, you can call openGallery() method here again
                openGallery();
            } else {
                // Permission denied, handle this case (e.g., show a message to the user)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public byte[] convertUriToByteArray(Uri imageUri){
        try{
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream != null){
                int length = inputStream.available();
                byte[] byteArray = new byte[length];
                inputStream.read(byteArray);
                inputStream.close();
                return byteArray;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
