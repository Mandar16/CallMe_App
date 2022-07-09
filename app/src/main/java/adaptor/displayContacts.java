package adaptor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contacts.MainActivity;
import com.example.contacts.R;

import Data.MyDbHandler;


public class displayContacts extends AppCompatActivity {
     String number;
     int id;
    byte[] image;
    int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);


        Intent intent = getIntent();
        String name = intent.getStringExtra("RName");
         number = intent.getStringExtra("RNumber");
         id = getIntent().getExtras().getInt("RId");
          image = getIntent().getExtras().getByteArray("RImage");

                Bitmap ImageBitmap= BitmapFactory.decodeByteArray(image, 0, image.length);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.rName);
        textView.setText(name);

        // Capture the layout's TextView and set the string as its text
        TextView textView2 = findViewById(R.id.rNumber);
        textView2.setText(number);

        ImageButton img = findViewById(R.id.delete);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler db = new MyDbHandler(displayContacts.this);
                db.deleteContactById(id);

                Intent intent = new Intent(displayContacts.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        ImageView display_image = findViewById(R.id.contact_dp);
        display_image.setImageBitmap(ImageBitmap);
        ImageView typeMessage = findViewById(R.id.typeMessage);

        typeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms", number, null));
                startActivity(intent);
            }
        });

        ImageView callImageView = (ImageView)findViewById(R.id.call_butt);
        callImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(displayContacts.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(displayContacts.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + number;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });







    }
}