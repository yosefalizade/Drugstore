package ir.dimodeveloper.drugstore;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DrugsStore.db";
    private static final int VERSION = 1;
    public static final String TABLE_NAME_HONEY = "honey_tbl";
    public static final String TABLE_NAME_ALAYEM = "alayem_tbl";
    public static final String TABLE_NAME_AVAREZ = "avarez_tbl";
    public static final String TABLE_NAME_DRUGS = "drugs_tbl";
    public static final String TABLE_NAME_SICKNESS = "sickness_tbl";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KHAVASEASAL = "khavaseasal";
    private static final String GOROHE_DARO = "grohedaro";
    private static final String MOREDEMASRAF = "moredemasraf";
    private static final String MIZANEMASRAF = "mizanemasraf";
    private static final String TOZIHAT = "tozihat";
    private static final String SHARHE_BIMARI = "sharhebimari";
    private static final String ALAYEM = "alayem";
    private static final String FAVORITE = "favorite";
    private static final String TEXT = "text";

    //متد سازنده کلاس
    public DataBaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //کوئری ساخت یک جدول های مورد نیاز در دیتابیس
        String myQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_HONEY + " ( " + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + KHAVASEASAL + " TEXT, " + FAVORITE + " INTEGER );";
      sqLiteDatabase.execSQL(myQuery);
        String alayemQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ALAYEM + " ( " + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT );";
        sqLiteDatabase.execSQL(alayemQuery);
        String avarezQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_AVAREZ + " ( " + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + TEXT + " TEXT, " + FAVORITE + " INTEGER );";
        sqLiteDatabase.execSQL(avarezQuery);
        String drugsQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DRUGS + " ( " + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + GOROHE_DARO + " TEXT, " + MOREDEMASRAF + " TEXT, " + MIZANEMASRAF + " TEXT, " + TOZIHAT + " TEXT, " + FAVORITE + " INTEGER );";
        sqLiteDatabase.execSQL(drugsQuery);
        String sicknessQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SICKNESS + " ( " + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + SHARHE_BIMARI + " TEXT, " + ALAYEM + " TEXT, "  + FAVORITE + " INTEGER );";
        sqLiteDatabase.execSQL(sicknessQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
    //متد واردی کردن اطلاعات مربوط به عسل درمانی در دیتابیس
    public long insertHoneyData(Items items){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, items.id_Items);
        contentValues.put(NAME, items.name_Items);
        contentValues.put(KHAVASEASAL, items.khavaseAsal_Items);
        contentValues.put(FAVORITE, 0);
        return sqLiteDatabase.insert(TABLE_NAME_HONEY,null,contentValues);
    }
    //متد واردی کردن اطلاعات مربوط به علایم بیماری ها در دیتابیس
    public long insertAlayemData(Items items){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, items.id_Items);
        contentValues.put(NAME, items.name_Items);
        return sqLiteDatabase.insert(TABLE_NAME_ALAYEM,null,contentValues);
    }
    //متد واردی کردن اطلاعات مربوط به عوارض بیماری ها در دیتابیس
    public long insertAvarezData(Items items){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, items.id_Items);
        contentValues.put(NAME, items.name_Items);
        contentValues.put(TEXT, items.text_Items);
        contentValues.put(FAVORITE, 0);
        return sqLiteDatabase.insert(TABLE_NAME_AVAREZ,null,contentValues);
    }
    //متد واردی کردن اطلاعات مربوط به داروها در دیتابیس
    public long insertDrugsData(Items items){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, items.id_Items);
        contentValues.put(NAME, items.name_Items);
        contentValues.put(GOROHE_DARO, items.gorohedaro_Items);
        contentValues.put(MOREDEMASRAF, items.moredemasraf_Items);
        contentValues.put(MIZANEMASRAF, items.mizaneMasraf_Items);
        contentValues.put(TOZIHAT, items.tozihat_Items);
        contentValues.put(FAVORITE, 0);
        return sqLiteDatabase.insert(TABLE_NAME_DRUGS,null,contentValues);
    }
    //متد واردی کردن اطلاعات مربوط به بیماری ها در دیتابیس
    public long insertSicknessData(Items items){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, items.id_Items);
        contentValues.put(NAME, items.name_Items);
        contentValues.put(SHARHE_BIMARI, items.sharhebimari_Items);
        contentValues.put(ALAYEM, items.alyem_Items);
        contentValues.put(FAVORITE, 0);
        return sqLiteDatabase.insert(TABLE_NAME_SICKNESS,null,contentValues);
    }
    //متد شمارش تعداد داده های (ردیف های) ذخیره شده در جدول مورد نظر در دیتابیس
    public int count(String table_name){
        String query = "SELECT * FROM "+table_name;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        int res = cursor.getCount();
        return res;
    }
    //متد شمارش تعداد ردیف هایی که به لیست علاقمندی ها اضافه شده
    public int countFavorite(String table_name){
        String query = "SELECT * FROM "+table_name+" WHERE "+FAVORITE+"=1";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        int res = cursor.getCount();
        return res;
    }
    //متد نمایش اطلاعات یک داروی بر اساس نام دارو
    @SuppressLint("Range")
    public Items showDrugs(String name){

        String showQuery = "SELECT * FROM " + TABLE_NAME_DRUGS + " WHERE " + NAME + "='" + name + "'";
        //String showQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
       // Cursor cursor = sqLiteDatabase.rawQuery(showQuery,new String[]{name});
        Items items = new Items();
        if (cursor.moveToFirst()){
            items.id_Items = cursor.getString(0);
            items.name_Items = cursor.getString(cursor.getColumnIndex(NAME));
            items.gorohedaro_Items = cursor.getString(cursor.getColumnIndex(GOROHE_DARO));
            items.moredemasraf_Items = cursor.getString(cursor.getColumnIndex(MOREDEMASRAF));
            items.mizaneMasraf_Items = cursor.getString(cursor.getColumnIndex(MIZANEMASRAF));
            items.tozihat_Items = cursor.getString(cursor.getColumnIndex(TOZIHAT));
            items.favorite = cursor.getInt(cursor.getColumnIndex(FAVORITE));
        }
        return items;
    }
    //متد نمایش اطلاعات یک بیماری بر اساس نام بیماری
    @SuppressLint("Range")
    public Items showSickness(String name){

        String showQuery = "SELECT * FROM " + TABLE_NAME_SICKNESS + " WHERE " + NAME + "='" + name + "'";
        //String showQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
        // Cursor cursor = sqLiteDatabase.rawQuery(showQuery,new String[]{name});
        Items items = new Items();
        if (cursor.moveToFirst()){
            items.id_Items = cursor.getString(0);
            items.name_Items = cursor.getString(cursor.getColumnIndex(NAME));
            items.sharhebimari_Items = cursor.getString(cursor.getColumnIndex(SHARHE_BIMARI));
            items.alyem_Items = cursor.getString(cursor.getColumnIndex(ALAYEM));
            items.favorite = cursor.getInt(cursor.getColumnIndex(FAVORITE));
        }
        return items;
    }
    @SuppressLint("Range")
    //متد جستجو در لیست بیماری ها بر اساس علایم بیماری
    public ArrayList<String> searchSickness(String alayem){

        String showQuery = "SELECT * FROM " + TABLE_NAME_SICKNESS + " WHERE " + ALAYEM + " LIKE '%" + alayem + "%'";
        //String showQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
        // Cursor cursor = sqLiteDatabase.rawQuery(showQuery,new String[]{name});
        ArrayList<String> name_list = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                name_list.add(cursor.getString(cursor.getColumnIndex(NAME)));
            }while (cursor.moveToNext());

        }
        return name_list;
    }
    //متد نمایش اطلاعات عسل درمانی بر اساس عنوان
    @SuppressLint("Range")
    public Items showHoney(String name){

        String showQuery = "SELECT * FROM " + TABLE_NAME_HONEY + " WHERE " + NAME + "='" + name + "'";
        //String showQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
       // Cursor cursor = sqLiteDatabase.rawQuery(showQuery,new String[]{name});
        Items items = new Items();
        if (cursor.moveToFirst()){
            items.id_Items = cursor.getString(0);
            items.name_Items = cursor.getString(cursor.getColumnIndex(NAME));
            items.khavaseAsal_Items = cursor.getString(cursor.getColumnIndex(KHAVASEASAL));
            items.favorite = cursor.getInt(cursor.getColumnIndex(FAVORITE));
        }
        return items;
    }
    //متد نمایش اطلاعات عوارض بیماری ها
    @SuppressLint("Range")
    public Items showAvarez(String name){

        String showQuery = "SELECT * FROM " + TABLE_NAME_AVAREZ + " WHERE " + NAME + "='" + name + "'";
        //String showQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
        // Cursor cursor = sqLiteDatabase.rawQuery(showQuery,new String[]{name});
        Items items = new Items();
        if (cursor.moveToFirst()){
            items.id_Items = cursor.getString(0);
            items.name_Items = cursor.getString(cursor.getColumnIndex(NAME));
            items.text_Items = cursor.getString(cursor.getColumnIndex(TEXT));
            items.favorite = cursor.getInt(cursor.getColumnIndex(FAVORITE));
        }
        return items;
    }
    //متد نمایش ستون نام جدول ها بر اساس نام جدول
    public ArrayList<String> showNames(String table_name){

        String showQuery = "SELECT * FROM " + table_name +"";
        //String showQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
       // Cursor cursor = sqLiteDatabase.rawQuery(showQuery,new String[]{name});
        ArrayList<String> names_list = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(1);
                names_list.add(name);
            } while (cursor.moveToNext());

        }
        return names_list;
    }
    //متد نمایش لیست علاقمندی ها
    @SuppressLint("Range")
    public ArrayList<String> showFavorite(String table_name){

        String showQuery = "SELECT * FROM " + table_name + " WHERE " + FAVORITE + "=1";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(showQuery,null);
        ArrayList<String> name_list = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                name_list.add(cursor.getString(cursor.getColumnIndex(NAME)));
            }while (cursor.moveToNext());

        }
        return name_list;
    }
    //متد حذف کردن یک ردیف از جدول
//    public boolean deleteDrugs(String name){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        int res = sqLiteDatabase.delete(TABLE_NAME_HONEY,NAME+ "=?",new String[] {name});
//        if (res==0){
//            return false;
//        }else return true;
//    }
   //متد ویرایش اطلاعات یک دارو (یک ردیف) بر اساس نام دارو
//    public boolean updateDrugs(Items items){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        //contentValues.put(NAME,items.Name_Items);
//        contentValues.put(KHAVASEASAL,items.khavaseAsal_Items);
//        contentValues.put(FAVORITE,items.text_Items);
//        contentValues.put(TEXT,items.mizaneMasraf_Items);
//
//        int res = sqLiteDatabase.update(TABLE_NAME_HONEY,contentValues,NAME+"=?",new String[] {items.name_Items});
//        if (res==0){
//            return false;
//        }else
//        return true;
//    }
    //متد ویرایش ردیف مربوط به علاقمندی ها
    public boolean updateFavorite(int id,int fav,String table_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVORITE,fav);
        int res = sqLiteDatabase.update(table_name,contentValues,ID+" = "+id,null);
        if (res==0){
            return false;
        }else
        return true;
    }
}
