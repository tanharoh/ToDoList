package io.github.tanharoh.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.mozilla.javascript.tools.debugger.Main;

import java.util.Objects;


public class EditToDoItem extends AppCompatActivity
{
    ToDoDatabaseHelper dbHelper;
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
        final EditText editText = (EditText) findViewById(R.id.edit_item_id);
        dbHelper = new ToDoDatabaseHelper(this);
        //接受内容
        Bundle bundle = this.getIntent().getExtras();
        String text = bundle.getString("text");
        if (editText != null)
        {
            editText.setText(text);
        }

        final Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT _id FROM ToDoList WHERE todo = \'" + text + "\'", null);

        if (toolbar != null)
        {
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String item = editText != null ? editText.getText().toString() : null;
                    if (item != null && !item.equals(""))
                    {
                        while (cursor.moveToNext())
                        {
                            int todo_id = cursor.getInt(0);
                            dbHelper.getWritableDatabase().execSQL("UPDATE ToDoList SET todo = \'" + item + "\' WHERE _id = " + todo_id);
                        }
                    }
                    finish();

                }
            });
        }
    }


}