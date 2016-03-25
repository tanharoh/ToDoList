package io.github.tanharoh.todolist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CreatToDoItem extends AppCompatActivity
{
    ToDoDatabaseHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null)
        {
            toolbar.setNavigationIcon(R.drawable.item_back);
        }
        final EditText addText = (EditText) findViewById(R.id.edit_item_id);
        dbOpenHelper = new ToDoDatabaseHelper(this);

        if (toolbar != null)
        {
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String item = addText != null ? addText.getText().toString() : null;
                    if (item != null)
                    {
                        insertData(dbOpenHelper.getWritableDatabase(), item);
                        finish();
                    }
                }
            });
        }
    }

    private void insertData(SQLiteDatabase db, String todo)
    {
        //执行插入语句
        db.execSQL("insert into ToDoList values(null, ?)", new String[]{todo});
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("todo",todo);
//        long id = db.insert("ToDoList",null,contentValues);
    }
}