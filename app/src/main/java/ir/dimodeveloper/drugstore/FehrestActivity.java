package ir.dimodeveloper.drugstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FehrestActivity extends AppCompatActivity {

    int dataType;
    DataBaseManager dataBaseManager;
    ArrayList<String> names_list;
    ArrayList<String> search_list = new ArrayList<>();
    ArrayList<String> sickness_names_list;
    RecyclerView rcl_show;
    TextView txt_title;
    Adapter adapter;
    EditText edt_search;
    String activity;
    boolean isSearching=false, foundSickness=false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fehrest);

        rcl_show = findViewById(R.id.rcl_show);
        txt_title = findViewById(R.id.txt_title);
        edt_search = findViewById(R.id.edt_search);

        //با متغییر اول مشخص میکنیم که اطلاعات مربوط به کدوم موضوع گرفته بشه و با متغییر دوم مشخص میشه که از مین اکتیویتی به این صفحه اومده یا از علاقمندی ها
        dataType = getIntent().getIntExtra("datatype",0);
        activity = getIntent().getStringExtra("activity");

        dataBaseManager = new DataBaseManager(this);
        //شرط گذاشتیم متناسب با موضوع مورد نظر درخواست به دیتابیس زده بشه و اطلاعات مورد نیاز گرفته بشه
        if (dataType== MainActivity.DRUGS){
            if (activity.equals("main")){names_list = dataBaseManager.showNames(DataBaseManager.TABLE_NAME_DRUGS);}else {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_DRUGS);}
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(R.string.searchText_drugs);
        }else if (dataType== MainActivity.SICKNESS){
            if (activity.equals("main")){names_list = dataBaseManager.showNames(DataBaseManager.TABLE_NAME_SICKNESS);}else {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_SICKNESS);}
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(R.string.searchText_Sickness);
        }else if (dataType== MainActivity.HONEY){
            if (activity.equals("main")){names_list = dataBaseManager.showNames(DataBaseManager.TABLE_NAME_HONEY);}else {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_HONEY);}
        }else if (dataType== MainActivity.AVAREZ){
            if (activity.equals("main")){names_list = dataBaseManager.showNames(DataBaseManager.TABLE_NAME_AVAREZ);}else {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_AVAREZ);}
        }else if (dataType== MainActivity.TASHKHIS){
            names_list = dataBaseManager.showNames(DataBaseManager.TABLE_NAME_ALAYEM);
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(R.string.searchText_Tashkhis);
            rcl_show.setVisibility(View.GONE);
        }

        //مقدار دهی اداپتر و متصل کردن آن به ریسایکلر ویو
        adapter = new Adapter(this,names_list);
        rcl_show.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rcl_show.setAdapter(adapter);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                foundSickness=false;
                searchName(editable.toString());
                if (editable.toString().trim().length() == 0){isSearching=false;}else isSearching=true;
                if (dataType == MainActivity.TASHKHIS)txt_title.setVisibility(View.GONE);

            }
        });

        rcl_show.addOnItemTouchListener(new RecyclerItemClick(this, new RecyclerItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (dataType!= MainActivity.TASHKHIS){
                    String name;
                    if (isSearching)name = search_list.get(position);else name = names_list.get(position);
                    Intent intent = new Intent(FehrestActivity.this, ShowDataActivity.class);
                    intent.putExtra("datatype",dataType);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
                if (dataType==MainActivity.TASHKHIS && foundSickness){
                    Intent intent = new Intent(FehrestActivity.this, ShowDataActivity.class);
                    intent.putExtra("datatype",dataType);
                    intent.putExtra("name",sickness_names_list.get(position));
                    startActivity(intent);
                }else if (dataType==MainActivity.TASHKHIS && !foundSickness){
                  if (sickness_names_list!=null) sickness_names_list.clear();
                  sickness_names_list =  dataBaseManager.searchSickness(search_list.get(position));
                    adapter = new Adapter(FehrestActivity.this,sickness_names_list);
                    rcl_show.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    foundSickness = true;
                    txt_title.setVisibility(View.VISIBLE);txt_title.setText("لیست تمام بیماری هایی که علائم "+search_list.get(position)+" را نشان میدهند:");
                }



            }
        }));
    }

    @Override
    protected void onResume() {
        //شرط گذاشتیم در صورتی که در حالت نمایش علاقمند های بود لیست با هر بار اومدن به اکتیویتی ساخته بشه تا مواردی که از لیست حذف میشن نمایش داده نشن
        if (activity.equals("fav")) {
            if (dataType == MainActivity.DRUGS) {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_DRUGS);
                txt_title.setVisibility(View.VISIBLE);
                txt_title.setText(R.string.searchText_drugs);
            } else if (dataType == MainActivity.SICKNESS) {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_SICKNESS);
                txt_title.setVisibility(View.VISIBLE);
                txt_title.setText(R.string.searchText_Sickness);
            } else if (dataType == MainActivity.HONEY) {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_HONEY);
            } else if (dataType == MainActivity.AVAREZ) {
                names_list = dataBaseManager.showFavorite(DataBaseManager.TABLE_NAME_AVAREZ);}
            adapter = new Adapter(this, names_list);
            rcl_show.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rcl_show.setAdapter(adapter);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(FehrestActivity.this, MainActivity.class));
        finish();
    }
    //متد جستجو در ریسایکلر ویو
    private void searchName(String text){
        search_list.clear();
        for (String n : names_list) {
            if (n.toLowerCase().trim().contains(text.toLowerCase().trim())){
                search_list.add(n);
            }
        }
        adapter = new Adapter(this,search_list);
        rcl_show.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        rcl_show.setVisibility(View.VISIBLE);
    }
}