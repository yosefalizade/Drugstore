package ir.dimodeveloper.drugstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FavoriteActivity extends AppCompatActivity {

    Intent mintent;
    DataBaseManager dataBaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        dataBaseManager = new DataBaseManager(this);

    }

    public void onClick(View view){
        int id = view.getId();
        mintent = new Intent(FavoriteActivity.this,FehrestActivity.class);
        //برای همه حالت ها شرط گذاشتیم تعداد علاقمندی ها رو شمارش کنه در صورتی تعداد صفر بود به کاربر اعلام کنه و در غیر اینصورت اینتنت بخوره به صفحه بعد برای نمایش لیست علاقمندی ها
        int fav_size =0;
        switch (id){
            case R.id.btn_drugs_fav:
                fav_size = dataBaseManager.countFavorite(DataBaseManager.TABLE_NAME_DRUGS);
                if (fav_size==0){
                    Toast.makeText(this, "موردی به علاقمندی ها اضافه نشده!", Toast.LENGTH_SHORT).show();
                }else {mintent.putExtra("datatype",MainActivity.DRUGS);}
                break;
            case R.id.btn_sickness_fav:
                fav_size = dataBaseManager.countFavorite(DataBaseManager.TABLE_NAME_SICKNESS);
                if (fav_size==0){
                    Toast.makeText(this, "موردی به علاقمندی ها اضافه نشده!", Toast.LENGTH_SHORT).show();
                }else {mintent.putExtra("datatype",MainActivity.SICKNESS);}break;
            case R.id.btn_honey_fav:
                fav_size = dataBaseManager.countFavorite(DataBaseManager.TABLE_NAME_HONEY);
                if (fav_size==0){
                    Toast.makeText(this, "موردی به علاقمندی ها اضافه نشده!", Toast.LENGTH_SHORT).show();
                }else {mintent.putExtra("datatype",MainActivity.HONEY);}break;
            case R.id.btn_avarez_fav:
                fav_size = dataBaseManager.countFavorite(DataBaseManager.TABLE_NAME_AVAREZ);
                if (fav_size==0){
                    Toast.makeText(this, "موردی به علاقمندی ها اضافه نشده!", Toast.LENGTH_SHORT).show();
                }else {mintent.putExtra("datatype",MainActivity.AVAREZ);}break;
        }
        if (fav_size>0){
            mintent.putExtra("activity","fav");
            startActivity(mintent);
        }

    }
}