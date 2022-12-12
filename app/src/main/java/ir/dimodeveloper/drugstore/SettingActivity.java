package ir.dimodeveloper.drugstore;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        spn_fonts = findViewById(R.id.spn_fonts);
        txt_show =  findViewById(R.id.txt_show);
        txt_font =  findViewById(R.id.txt_font);
        seek_size = findViewById(R.id.seekbar_size);
        seek_space = findViewById(R.id.seekbar_space);
        mySwitch = findViewById(R.id.mySwitch);

        //گرفتن تنظیمات ذخیره شده
        getSharedpref();
        //بازنشانی تنظیمات ذخیره شده
        seek_size.setProgress(font_size);
        txt_show.setTextSize(font_size);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/"+font+".ttf");
        txt_show.setTypeface(typeface);
        mySwitch.setChecked(isScreenOn);
        txt_show.setLineSpacing(line_spacing,1.0f);

        //مقدار دهی اداپتر اسپینر و متصل کردن ان به اسپینر
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,fontsList);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spn_fonts.setAdapter(adapter);

        //رویداد کلیک بر روی ایتم های اسپینر
        spn_fonts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/"+fontsList[position]+".ttf");
                txt_show.setTypeface(typeface);
                editor.putString(FONT,fontsList[position]).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(SCREEN,b).apply();
            }
        });
        //رویداد مربوط به سیکبار تغییر سایز
        seek_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                font_size = progress;
                txt_show.setTextSize(font_size);
                editor.putInt(FONT_SIZE,font_size).apply();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //رویداد مربوط به سیکبار تغییر فاصله خطوط
        seek_space.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

               line_spacing = progress;
               txt_show.setLineSpacing(line_spacing,1.0f);
               editor.putInt(LINE_SPACING,line_spacing).apply();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}