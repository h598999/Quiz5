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

    private void openGallery(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, YOUR_PERMISSION_REQUEST_CODE);
    }

    private void requestExternalStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    private void go(){
        finish();
    }

    private void submit() throws IOException {
        String enteredText = getEnteredText();
        Uri imageUri = getUri();

        if (enteredText == null || imageUri == null){
            return;
        }
        repo.insert(new PhotoInfo(enteredText, this.convertUriToByteArray(imageUri)));
        Toast.makeText(this, "Image added", Toast.LENGTH_SHORT).show();
        int uid = Binder.getCallingUid();
        String callingPackage = getPackageManager().getNameForUid(uid);
        getApplication().grantUriPermission(callingPackage, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        reset();
    }

    private void goBack(){
        Log.d("BackButton Upload", "Pressed back");
    }

    private void reset(){
        view = findViewById(R.id.imageView);
        view.setImageResource(0);
        input = findViewById(R.id.inputText_input);
        input.setText("");
        this.imageUri = null;
    }

    private String getEnteredText(){
        input = findViewById(R.id.inputText_input);
        String enteredText = input.getText().toString();

        if (enteredText == null || enteredText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return null;
        }
        return enteredText;
    }
    private Uri getUri(){
        if (this.imageUri == null){
            Toast.makeText(getApplicationContext(), "Please enter a image", Toast.LENGTH_SHORT).show();
            return null;
        }
        return this.imageUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Log.d("Image", "Image selected: " + imageUri.toString());
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
