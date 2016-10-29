package bluetooth1.example.com.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class databaseconnection {
    public static Context context;
    static SQLiteDatabase sqLiteDatabase;

    public static void init() {
        try {
            sqLiteDatabase = context.openOrCreateDatabase("aa", SQLiteDatabase.OPEN_READWRITE, null);
            String sql = "create table if not exists reg(f_name varchar(45),l_name varchar(45),address varchar(300),pin varchar(30))";
            sqLiteDatabase.execSQL(sql);
            sql = "create table if not exists activation(val int)";
            sqLiteDatabase.execSQL(sql);
            sql = "create table if not exists contact_numbers(name varchar(45),number varchar(45))";
            sqLiteDatabase.execSQL(sql);
            sql = "create table if not exists contact_mail(name varchar(45),mail varchar(45))";
            sqLiteDatabase.execSQL(sql);
            sql = "create table if not exists word(word varchar(45))";
            sqLiteDatabase.execSQL(sql);
            sql = "create table if not exists action(action varchar(45),state int)";
            sqLiteDatabase.execSQL(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Cursor getData(String sql) {
        System.out.println("sql = " + sql);
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cursor;
    }
    public static int delete(){
        String sql="delete from contact_numbers;";
        sqLiteDatabase.execSQL(sql);
        return 0;
    }

    public static int putData(String sql) {
        System.out.println("sql = " + sql);
        int i = 0;
        try {
            sqLiteDatabase.execSQL(sql);
            i++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public static void close() {
        try {
            sqLiteDatabase.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
