package ir.dimodeveloper.drugstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDataActivity extends BaseActivity {

    TextView txt_Name, txt_GroheDaro, txt_MoredeMasraf, txt_Mizanemasraf, txt_tozihat,gorohedaro,MoredeMasraf, Mizanemasraf, tozihat;
    View layout1,layout2,layout3,layout4;
    ImageView imgCopy,imgShare,imgSms,imgFav;
    SeekBar seek_size;
    int dataType, isFavorite,id;
    String name, table_name, text_share="";
    DataBaseManager dataBaseManager;
    Items items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

       findViews();
        //گرفتن تنظیمات ذخیره شده
       getSharedpref();
        //بازنشانی تنظیمات ذخیره شده
        restoreSetting();

        dataType = getIntent().getIntExtra("datatype",0);
        name = getIntent().getStringExtra("name");

        dataBaseManager = new DataBaseManager(this);
        items = new Items();

        if (dataType== MainActivity.DRUGS){
            table_name = DataBaseManager.TABLE_NAME_DRUGS;
         items = dataBaseManager.showDrugs(name);
         txt_Name.setText(name);
         txt_GroheDaro.setText(items.gorohedaro_Items);
         txt_MoredeMasraf.setText(items.moredemasraf_Items);
         txt_Mizanemasraf.setText(items.mizaneMasraf_Items);
         txt_tozihat.setText(items.tozihat_Items);
         text_share = name + "\n"+items.gorohedaro_Items+"\n"+items.moredemasraf_Items+"\n"+items.mizaneMasraf_Items+"\n"+items.tozihat_Items;
        }else if (dataType== MainActivity.SICKNESS || dataType== MainActivity.TASHKHIS){
            table_name = DataBaseManager.TABLE_NAME_SICKNESS;
            items = dataBaseManager.showSickness(name);
            txt_Name.setText(name);
            gorohedaro.setText("شرح بیماری:");
            txt_GroheDaro.setText(items.sharhebimari_Items);
            MoredeMasraf.setText("علائم:");
            txt_MoredeMasraf.setText(items.alyem_Items);
            txt_Mizanemasraf.setVisibility(View.GONE);
            txt_tozihat.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
            Mizanemasraf.setVisibility(View.GONE);
            tozihat.setVisibility(View.GONE);
            text_share = name + "\n"+items.sharhebimari_Items+"\n"+items.alyem_Items;

        }else if (dataType== MainActivity.HONEY){
            table_name = DataBaseManager.TABLE_NAME_HONEY;
            items = dataBaseManager.showHoney(name);
            txt_Name.setText(name);
            txt_GroheDaro.setText(items.khavaseAsal_Items);
            txt_MoredeMasraf.setVisibility(View.GONE);
            txt_Mizanemasraf.setVisibility(View.GONE);
            txt_tozihat.setVisibility(View.GONE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
            gorohedaro.setVisibility(View.GONE);
            MoredeMasraf.setVisibility(View.GONE);
            Mizanemasraf.setVisibility(View.GONE);
            tozihat.setVisibility(View.GONE);
            text_share = name + "\n"+items.khavaseAsal_Items;
        }else if (dataType== MainActivity.AVAREZ){
            table_name = DataBaseManager.TABLE_NAME_AVAREZ;
            items = dataBaseManager.showAvarez(name);
            txt_Name.setText(name);
            txt_GroheDaro.setText(items.text_Items);
            txt_MoredeMasraf.setVisibility(View.GONE);
            txt_Mizanemasraf.setVisibility(View.GONE);
            txt_tozihat.setVisibility(View.GONE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
            gorohedaro.setVisibility(View.GONE);
            MoredeMasraf.setVisibility(View.GONE);
            Mizanemasraf.setVisibility(View.GONE);
            tozihat.setVisibility(View.GONE);
            text_share = name + "\n"+items.text_Items;
        }

        //شرط گذاشتیم اگر مورد نمایش داده شده جزو علاقمندی ها بود قلب تو پر نشون بده در غیر اینصورت ایگون قلب تو خالی نشون بده
        isFavorite = items.favorite;
        id = Integer.parseInt(items.id_Items);
        if (isFavorite==0){imgFav.setImageResource(R.drawable.heart_outline);
        }else {imgFav.setImageResource(R.drawable.heart_icon);}

        //رویداد مربوط به سیکبار تغییر سایز
        seek_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_GroheDaro.setTextSize(progress);
                txt_Mizanemasraf.setTextSize(progress);
                txt_MoredeMasraf.setTextSize(progress);
                txt_tozihat.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void findViews() {
        txt_Name =findViewById(R.id.txt_Name);
        txt_GroheDaro =findViewById(R.id.txt_grohedaro);
        txt_MoredeMasraf =findViewById(R.id.txt_moredemasraf);
        txt_Mizanemasraf =findViewById(R.id.txt_mizanemasraf);
        txt_tozihat =findViewById(R.id.txt_tozihat);
        seek_size = findViewById(R.id.seekbar_size);
        gorohedaro =findViewById(R.id.gorohedaro);
        MoredeMasraf =findViewById(R.id.moredemasraf);
        Mizanemasraf =findViewById(R.id.mizanemasraf);
        tozihat =findViewById(R.id.tozihat);
        imgCopy = findViewById(R.id.img_copy);
        imgShare = findViewById(R.id.img_share);
        imgSms = findViewById(R.id.img_sms);
        imgFav = findViewById(R.id.img_fav);
        layout1 =  findViewById(R.id.layout1);
        layout2 =  findViewById(R.id.layout2);
        layout3 =  findViewById(R.id.layout3);
        layout4 =  findViewById(R.id.layout4);
    }
    //بازنشانی تنظیمات ذخیره شده
    private void restoreSetting(){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/"+font+".ttf");
        txt_Name.setTypeface(typeface);
        txt_GroheDaro.setTypeface(typeface);
        txt_MoredeMasraf.setTypeface(typeface);
        txt_Mizanemasraf.setTypeface(typeface);
        txt_tozihat.setTypeface(typeface);
        gorohedaro.setTypeface(typeface);
        MoredeMasraf.setTypeface(typeface);
        Mizanemasraf.setTypeface(typeface);
        tozihat.setTypeface(typeface);

        txt_GroheDaro.setTextSize(font_size);
        txt_MoredeMasraf.setTextSize(font_size);
        txt_Mizanemasraf.setTextSize(font_size);
        txt_tozihat.setTextSize(font_size);
        /*gorohedaro.setTextSize(font_size);
        MoredeMasraf.setTextSize(font_size);
        Mizanemasraf.setTextSize(font_size);
        tozihat.setTextSize(font_size);*/

        txt_GroheDaro.setLineSpacing(line_spacing,1.0f);
        txt_MoredeMasraf.setLineSpacing(line_spacing,1.0f);
        txt_Mizanemasraf.setLineSpacing(line_spacing,1.0f);
        txt_tozihat.setLineSpacing(line_spacing,1.0f);
        //روشن ماندن صفحه نمایش
        if (isScreenOn)getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    public void btnsClick(View view){

        int id_ = view.getId();
        switch (id_){

            case R.id.img_copy:
                //اضافه کردن متن مورد نظر به کلیپبورد
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("Text",text_share));
                Toast.makeText(this, "به کلیپبورد اضافه شد", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_fav:
                int fav;
                if (isFavorite==0){
                    Toast.makeText(this, "به لیست علاقمندی ها اضافه شد", Toast.LENGTH_SHORT).show();
                    imgFav.setImageResource(R.drawable.heart_icon);
                    fav=1;isFavorite=1;}else {Toast.makeText(this, "از لیست علاقمندی ها حذف شد", Toast.LENGTH_SHORT).show();fav=0;
                    isFavorite=0;imgFav.setImageResource(R.drawable.heart_outline);}
                dataBaseManager.updateFavorite(id,fav,table_name);
                break;
            case R.id.img_share:
                //شیر کردن متن مورد نظر با تمام اپ هایی که توانایی ارسال متن رو دارند
                Intent intent_share = new Intent(Intent.ACTION_SEND);
                intent_share.putExtra(Intent.EXTRA_TEXT,text_share);
                intent_share.setType("text/plain");
                startActivity(intent_share);
                break;
            case R.id.img_sms:
                //متد ارسال یک متن از طریق اس مس
                Intent intent_sms = new Intent(Intent.ACTION_VIEW);
                intent_sms.putExtra("sms_body",text_share);
                intent_sms.setData(Uri.parse("sms:"));
                startActivity(intent_sms);
                break;

        }
  }

}