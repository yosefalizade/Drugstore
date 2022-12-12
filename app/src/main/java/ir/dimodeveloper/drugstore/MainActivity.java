package ir.dimodeveloper.drugstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlt_drugs = findViewById(R.id.rlt_drugs);
        rlt_sickness = findViewById(R.id.rlt_sickness);
        rlt_honey = findViewById(R.id.rlt_honey);
        rlt_bmi = findViewById(R.id.rlt_bmi);
        rlt_avarez = findViewById(R.id.rlt_avarez);
        rlt_tashkhis = findViewById(R.id.rlt_tashkhis);
        txt_drugs_tedad = findViewById(R.id.txt_drugs_tedad);
        txt_sickness_tedad = findViewById(R.id.txt_sickness_tedad);
        txt_honey_tedad = findViewById(R.id.txt_honey_tedad);
        txt_avarez_tedad = findViewById(R.id.txt_avarez_tedad);
        txt_tashkhis_tedad = findViewById(R.id.txt_tashkhis_tedad);
        drawer_layout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navi_view = findViewById(R.id.navi_view);

        explosionField = ExplosionField.attach2Window(this);//مقدار دهی ابجکت مربوط به انیمیشن انفجار
        setSupportActionBar(toolbar);//ست کردن تولبار مورد نظر بعنوان اکشن بار
        navi_view.setNavigationItemSelectedListener(this);//(منوی کشویی) ست کردن رویداد کلیک بر روی ایتم های نویگیشن ویو

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open,R.string.close);
        drawer_layout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        txt_drugs_tedad.setVisibility(View.GONE);txt_sickness_tedad.setVisibility(View.GONE);txt_honey_tedad.setVisibility(View.GONE);
        txt_avarez_tedad.setVisibility(View.GONE);txt_tashkhis_tedad.setVisibility(View.GONE);
        //مقدار دهی انیمیشن نمایش دکمه ها
        Animation animation_1 = AnimationUtils.loadAnimation(this,R.anim.anim_trans);
        Animation animation_2 = AnimationUtils.loadAnimation(this,R.anim.anim_trans2);

        rlt_drugs.setAnimation(animation_1);
        rlt_sickness.setAnimation(animation_2);
        rlt_honey.setAnimation(animation_1);
        rlt_avarez.setAnimation(animation_2);
        rlt_bmi.setAnimation(animation_1);
        rlt_tashkhis.setAnimation(animation_2);


        getSharedpref();
        dataBaseManager = new DataBaseManager(this);
        //شرط گذاشتیم فقط در اجرای اول برنامه درخواست به سرور زده بشه و اطلاعات گرفته بشه
        if (first_run){
            //Toast.makeText(this, "اجرای اول برنامه ست", Toast.LENGTH_SHORT).show();
            new GetHoneyData(this).execute();
        }
    }

    @Override
    protected void onResume() {
        //شرط گذاشتیم اگر اجرای اول برنامه نبود تعداد داده ها از دیتابیس و سرور گرفته بشه جهت بررسی اینکه داده جدیدی وجود داره یا نه
        if (!first_run){
            tedad_honey_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_HONEY);
            tedad_alayem_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_ALAYEM);
            tedad_avarez_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_AVAREZ);
            tedad_drugs_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_DRUGS);
            tedad_sickness_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_SICKNESS);
            new GetTedadServer(this).execute();
        }
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int Ids = item.getItemId();
        switch (Ids) {
            case R.id.favorite:
                startActivity(new Intent(MainActivity.this,FavoriteActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.About:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("درباره مـــا")
                        .setMessage(R.string.About_us)
                        .show();
                break;
            case R.id.resource:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("منــابــع")
                        .setMessage(R.string.Resource)
                        .show();
                break;
            case R.id.exit:
                finish();
                break;
        }
        drawer_layout.closeDrawer(GravityCompat.START);

        return true;
    }

    //کلاس مربوط به گرفتن اطلاعات عسل درمانی از سرور
    private class GetHoneyData extends AsyncTask<Void,Void,String> {

        Context context;

        public GetHoneyData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
           // Toast.makeText(MainActivity.this, "در حال دریافت اطلاعات...", Toast.LENGTH_SHORT).show();
            showMessage(context,"در حال دریافت اطلاعات...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {


            return JsonClass.getJson(LIKE_GET_HONEY);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data !=null){

                try {

                    JSONArray jsonArray = new JSONArray(data);
                    long res = 0;
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String khavaseasal = jsonObject.getString("khavaseasal");

                        Items items = new Items();
                        items.id_Items = id;
                        items.name_Items = name;
                        items.khavaseAsal_Items = khavaseasal;
                        res = dataBaseManager.insertHoneyData(items);

                       // Log.e("name: ",name);
                    }
                    progressDialog.dismiss();
                    if (res == -1){
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();

                        if (first_run)new GetAlayemData().execute();else {tedad_honey_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_HONEY); hasNewData();}

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }else {
                Toast.makeText(MainActivity.this, "اطلاعاتی دریافت نشد!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


            super.onPostExecute(data);
        }
    }
    //کلاس مربوط به گرفتن اطلاعات علایم بیماری ها از سرور
    private class GetAlayemData extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {
           // Toast.makeText(MainActivity.this, "در حال دریافت اطلاعات...", Toast.LENGTH_SHORT).show();
            showMessage(MainActivity.this,"در حال دریافت اطلاعات...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {


            return JsonClass.getJson(LIKE_GET_ALAYEM);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data !=null){

                try {

                    JSONArray jsonArray = new JSONArray(data);
                    long res = 0;
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");

                        Items items = new Items();
                        items.id_Items = id;
                        items.name_Items = name;
                        res = dataBaseManager.insertAlayemData(items);

                       // Log.e("name: ",name);
                    }
                    progressDialog.dismiss();
                    if (res == -1){
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                        new GetAvarezData().execute();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }else {
                Toast.makeText(MainActivity.this, "اطلاعاتی دریافت نشد!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


            super.onPostExecute(data);
        }
    }
    //کلاس مربوط به گرفتن اطلاعات عوارض بیماری ها از سرور
    private class GetAvarezData extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {
           // Toast.makeText(MainActivity.this, "در حال دریافت اطلاعات...", Toast.LENGTH_SHORT).show();
            showMessage(MainActivity.this,"در حال دریافت اطلاعات...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {


            return JsonClass.getJson(LIKE_GET_AVAREZ);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data !=null){

                try {

                    JSONArray jsonArray = new JSONArray(data);
                    long res = 0;
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String text = jsonObject.getString("text");

                        Items items = new Items();
                        items.id_Items = id;
                        items.name_Items = name;
                        items.text_Items = text;
                        res = dataBaseManager.insertAvarezData(items);

                       // Log.e("name: ",name);
                    }
                    progressDialog.dismiss();
                    if (res == -1){
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                       if (first_run)new GetDrugsData().execute();else {tedad_avarez_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_AVAREZ);hasNewData();}

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }else {
                Toast.makeText(MainActivity.this, "اطلاعاتی دریافت نشد!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


            super.onPostExecute(data);
        }
    }
    //کلاس مربوط به گرفتن اطلاعات داروها از سرور
    private class GetDrugsData extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {
           // Toast.makeText(MainActivity.this, "در حال دریافت اطلاعات...", Toast.LENGTH_SHORT).show();
            showMessage(MainActivity.this,"در حال دریافت اطلاعات...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {


            return JsonClass.getJson(LIKE_GET_DRUGS);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data !=null){

                try {

                    JSONArray jsonArray = new JSONArray(data);
                    long res = 0;
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String grohedaro = jsonObject.getString("grohedaro");
                        String moredemasraf = jsonObject.getString("moredemasraf");
                        String mizanemasraf = jsonObject.getString("mizanemasraf");
                        String tozihat = jsonObject.getString("tozihat");

                        Items items = new Items();
                        items.id_Items = id;
                        items.name_Items = name;
                        items.gorohedaro_Items = grohedaro;
                        items.moredemasraf_Items = moredemasraf;
                        items.mizaneMasraf_Items = mizanemasraf;
                        items.tozihat_Items = tozihat;
                        res = dataBaseManager.insertDrugsData(items);

                       // Log.e("name: ",name);
                    }
                    progressDialog.dismiss();
                    if (res == -1){
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();

                        if (first_run){new GetSicknessData().execute();}else {tedad_drugs_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_DRUGS);hasNewData();}
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }else {
                Toast.makeText(MainActivity.this, "اطلاعاتی دریافت نشد!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


            super.onPostExecute(data);
        }
    }
    //کلاس مربوط به گرفتن اطلاعات بیماری ها از سرور
    private class GetSicknessData extends AsyncTask<Void,Void,String> {


        @Override
        protected void onPreExecute() {
           // Toast.makeText(MainActivity.this, "در حال دریافت اطلاعات...", Toast.LENGTH_SHORT).show();
            showMessage(MainActivity.this,"در حال دریافت اطلاعات...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {


            return JsonClass.getJson(LIKE_GET_SICKNESS);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data !=null){

                try {

                    JSONArray jsonArray = new JSONArray(data);
                    long res = 0;
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String sharhebimari = jsonObject.getString("sharhebimari");
                        String alayem = jsonObject.getString("alayem");

                        Items items = new Items();
                        items.id_Items = id;
                        items.name_Items = name;
                        items.sharhebimari_Items = sharhebimari;
                        items.alyem_Items = alayem;
                        res = dataBaseManager.insertSicknessData(items);

                       // Log.e("name: ",name);
                    }
                    progressDialog.dismiss();
                    if (res == -1){
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                        editor.putBoolean(FIRST_RUN,false).apply();
                        first_run = false;
                        tedad_sickness_database = dataBaseManager.count(DataBaseManager.TABLE_NAME_SICKNESS);hasNewData();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }else {
                Toast.makeText(MainActivity.this, "اطلاعاتی دریافت نشد!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


            super.onPostExecute(data);
        }
    }
    //کلاس مربوط به گرفتن تعداد داده ها از سرور
    private class GetTedadServer extends AsyncTask<Void,Void,String> {

        Context context;

        public GetTedadServer(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
           // Toast.makeText(MainActivity.this, "در حال دریافت اطلاعات...", Toast.LENGTH_SHORT).show();
           // showMessage(context,"در حال دریافت اطلاعات...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            return JsonClass.getJson(LIKE_GET_TEDAD);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data !=null){

                try {

                    JSONArray jsonArray = new JSONArray(data);
                    long res = 0;

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String id = jsonObject.getString("id");
                        tedad_drugs_server = Integer.parseInt(jsonObject.getString("drugs_tedad"));
                        tedad_sikcness_server = Integer.parseInt(jsonObject.getString("sickness_tedad"));
                        tedad_honey_server = Integer.parseInt(jsonObject.getString("honey_tedad"));
                        tedad_alayem_server = Integer.parseInt(jsonObject.getString("alayem_tedad"));
                        tedad_avarez_server = Integer.parseInt(jsonObject.getString("avarez_tedad"));
                        hasNewData();
                        //Toast.makeText(context, "با موفقیت گرفته شد", Toast.LENGTH_SHORT).show();

                   // progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                  //  progressDialog.dismiss();
                }

            }else {
                Toast.makeText(MainActivity.this, "اطلاعاتی دریافت نشد!", Toast.LENGTH_SHORT).show();
               // progressDialog.dismiss();
            }


            super.onPostExecute(data);
        }
    }

    //با این متد تعداد اطلاعات ذخیره شده در دیتابیس با تعداد داده های سرور مقایسه میشه و در صورت که تعداد سرور بیشتر باشه به کاربر اعلام میکنه
    private void hasNewData(){
        has_new_data_honey = tedad_honey_server - tedad_honey_database;
        if (has_new_data_honey>0){
            //Toast.makeText(this, has_new_data_honey + " تا مورد جدید برای عسل درمانی وجود دارد", Toast.LENGTH_SHORT).show();
            txt_honey_tedad.setText(String.valueOf(has_new_data_honey));txt_honey_tedad.setVisibility(View.VISIBLE);
        }else txt_honey_tedad.setVisibility(View.GONE);
        has_new_data_alayem = tedad_alayem_server - tedad_alayem_database;
        if (has_new_data_alayem>0){
            Toast.makeText(this, has_new_data_alayem + " تا مورد جدید برای علایم بیماری ها وجود دارد", Toast.LENGTH_SHORT).show();
        }
        has_new_data_avarez = tedad_avarez_server - tedad_avarez_database;
        if (has_new_data_avarez>0){
            //Toast.makeText(this, has_new_data_avarez + " تا مورد جدید برای عوارض بیماری ها وجود دارد", Toast.LENGTH_SHORT).show();
            txt_avarez_tedad.setText(String.valueOf(has_new_data_avarez));txt_avarez_tedad.setVisibility(View.VISIBLE);
        }else txt_avarez_tedad.setVisibility(View.GONE);
        has_new_data_drugs = tedad_drugs_server - tedad_drugs_database;
        if (has_new_data_drugs>0){
           // Toast.makeText(this, has_new_data_drugs + " تا مورد جدید برای داروها وجود دارد", Toast.LENGTH_SHORT).show();
            txt_drugs_tedad.setText(String.valueOf(has_new_data_drugs));txt_drugs_tedad.setVisibility(View.VISIBLE);
        }else txt_drugs_tedad.setVisibility(View.GONE);
        has_new_data_sickness = tedad_sikcness_server - tedad_sickness_database;
        if (has_new_data_sickness>0){
           // Toast.makeText(this, has_new_data_sickness + " تا مورد جدید برای بیماری ها وجود دارد", Toast.LENGTH_SHORT).show();
            txt_sickness_tedad.setText(String.valueOf(has_new_data_sickness));txt_sickness_tedad.setVisibility(View.VISIBLE);
        }else txt_sickness_tedad.setVisibility(View.GONE);
    }
    //متد نمایش پروگرس دیالوگ
    private void showMessage(Context context , String msg){
       // if (progressDialog!=null)progressDialog.dismiss();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void btnsClick(View view){

        int id = view.getId();


        switch (id){
            case R.id.rlt_drugs:
                mintent = new Intent(MainActivity.this,FehrestActivity.class);
                mintent.putExtra("datatype",DRUGS);
                mintent.putExtra("activity","main");
                //شرط گذاشتیم در صورتی که تعداد داده ای جدی در سرور بود ابتدا بروزرسانی انجام بشه
                if (has_new_data_drugs>0){
                    updateDialog(DRUGS,has_new_data_drugs+" مورد جدید دارو جهت بروز رسانی وجود دارد");
                return;}
                explosionField.explode(view);
                break;
            case R.id.rlt_sickness:
                mintent = new Intent(MainActivity.this,FehrestActivity.class);
                mintent.putExtra("datatype",SICKNESS);
                mintent.putExtra("activity","main");
                if (has_new_data_sickness>0){
                    updateDialog(SICKNESS,has_new_data_sickness+" مورد جدید بیماری جهت بروز رسانی وجود دارد");
                    return;}
                explosionField.explode(view);break;
            case R.id.rlt_honey:
                mintent = new Intent(MainActivity.this,FehrestActivity.class);
                mintent.putExtra("datatype",HONEY);
                mintent.putExtra("activity","main");
                if (has_new_data_honey>0){
                    updateDialog(HONEY,has_new_data_honey+" مورد جدید عسل درمانی جهت بروز رسانی وجود دارد");
                    return;}
                explosionField.explode(view);break;
            case R.id.rlt_avarez:
                mintent = new Intent(MainActivity.this,FehrestActivity.class);
                mintent.putExtra("datatype",AVAREZ);
                mintent.putExtra("activity","main");
                if (has_new_data_avarez>0){
                    updateDialog(AVAREZ,has_new_data_avarez+" مورد جدید عوارض دارو جهت بروز رسانی وجود دارد");
                    return;}
                explosionField.explode(view);break;
            case R.id.rlt_bmi:
                explosionField.explode(view);
                mintent = new Intent(MainActivity.this,Bmi_Activity.class);
                break;
            case R.id.rlt_tashkhis:explosionField.explode(view); mintent = new Intent(MainActivity.this,FehrestActivity.class);
                mintent.putExtra("datatype",TASHKHIS);
                mintent.putExtra("activity","main");break;
        }
        //ایجاد یک تاخیر 1 ثانیه ای جهت دیده شدن انیمیشن انفجار دکمه ها
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(mintent);
                    finish();
                }
            },1000);

    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }else {exitDialog("مطمئنی میخوای خارج بشی؟");}
        //super.onBackPressed();
    }
    //متد مربوط به نمایش دیالوگ خروج
    public void exitDialog (String peqam){

        AlertDialog.Builder Hoshdar = new AlertDialog.Builder(MainActivity.this);
        Hoshdar.setMessage(peqam);
        Hoshdar.setCancelable(false);
        Hoshdar.setPositiveButton("آره", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        Hoshdar.setNegativeButton("نه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        Hoshdar.create().show();
    }
    //متد مربوط به نمایش دیالوگ بروزرسانی داده ها
    public void updateDialog (int datatype,String peqam){

        AlertDialog.Builder Hoshdar = new AlertDialog.Builder(MainActivity.this);
        Hoshdar.setMessage(peqam);
        Hoshdar.setCancelable(false);
        Hoshdar.setPositiveButton("بروزرسانی", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (datatype){
                    case 1:new GetDrugsData().execute();break;
                    case 2: new GetSicknessData().execute();break;
                    case 3:new GetHoneyData(MainActivity.this).execute();break;
                    case 4:new GetAvarezData().execute();break;
                }

            }
        });
        Hoshdar.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        Hoshdar.create().show();
    }

}