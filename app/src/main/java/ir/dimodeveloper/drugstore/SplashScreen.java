package ir.dimodeveloper.drugstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import tyrantgit.explosionfield.ExplosionField;

public class SplashScreen extends AppCompatActivity {

    ImageView img_title,image;
    ExplosionField  explosionField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img_title = findViewById(R.id.img_title);
        image = findViewById(R.id.image);

        explosionField = ExplosionField.attach2Window(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                explosionField.explode(img_title);
                explosionField.explode(image);            }
        }, 2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 4000);
    }
}