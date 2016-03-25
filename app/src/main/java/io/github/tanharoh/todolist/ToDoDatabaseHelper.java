package io.github.tanharoh.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDatabaseHelper extends SQLiteOpenHelper
{
    final String CREAT_TABLE_SQL = "create table ToDoList(_id integer primary " +
            "key autoincrement ,todo)";
    private static String versionName = "ToDoDB";
    private static int versionCode = 1;

    public ToDoDatabaseHelper(Context context)
    {
        super(context, versionName, null, versionCode);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //第一次使用数据库时自动创建表
        db.execSQL(CREAT_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        System.out.println("-----------------onUpgrade Called-------------------"
                + oldVersion + "---->" + newVersion);
    }
}