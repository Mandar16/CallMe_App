package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import Data.MyDbHandler;
import Model.Contact;

public class Enter_Data extends AppCompatActivity {

    private static final int PICK_PHOTO = 1;
    ImageView image;
    Button btnAdding;
    Uri selectedImage;
    Button butt;
    MyDbHandler db = new MyDbHandler(Enter_Data.this);
    boolean isImageSelected =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__data);





        image = findViewById(R.id.add_photo);
        btnAdding = (Button)findViewById(R.id.add_photo_btn);
        btnAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Enter_Data.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PICK_PHOTO);

                isImageSelected = true;


            }
        });


         butt = (Button) findViewById(R.id.save_btn);
        butt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String name;
                 String number;
                 byte[] img_arr;

                EditText editText1 = (EditText) findViewById(R.id.enter_name);
                EditText editText2 = (EditText) findViewById(R.id.editTextPhone);
                name = editText1.getText().toString();
                number = editText2.getText().toString();



                Log.d("fckt","Image array added");
                if(isImageSelected){
                    if(number.length()==10){
                        img_arr = ImageViewToByte(image);

                        Contact new_contact = new Contact();

                        new_contact.setPhoneNumber(number);
                        new_contact.setName(name);
                        new_contact.setImage(img_arr);
                        db.addContact(new_contact);
                        Intent intent = new Intent(Enter_Data.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        Toast.makeText(Enter_Data.this, "Please Enter Correct Number", Toast.LENGTH_LONG).show();
                    }
                }
                else{

                    Toast.makeText(Enter_Data.this, "Avoid uploading high size image", Toast.LENGTH_LONG).show();
                }






            }
        });





    }

    private static byte[] ImageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PICK_PHOTO){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , PICK_PHOTO);
            }
            else {

                Toast.makeText(getApplicationContext(),"You dont have permission to acess this media",Toast.LENGTH_SHORT);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*private void openGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_PHOTO);

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK && requestCode == PICK_PHOTO){
            assert data != null;
            imageUri = data.getData();
            image.setImageURI(imageUri);*/
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage =  data.getData();
           // String[] filePathColumn = {MediaStore.Images.Media.DATA};
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);

                Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        }
    }

