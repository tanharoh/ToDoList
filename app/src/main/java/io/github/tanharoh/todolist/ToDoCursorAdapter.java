package io.github.tanharoh.todolist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ToDoCursorAdapter extends CursorAdapter
{
    public ToDoCursorAdapter(Context context,Cursor cursor,int flags)
    {
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.item_main,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView todo_item = (TextView) view.findViewById(R.id.item_textview);
        String todo = cursor.getString(cursor.getColumnIndexOrThrow("todo"));
        todo_item.setText(todo);
    }

}

