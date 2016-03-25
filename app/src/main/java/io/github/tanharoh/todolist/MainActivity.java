package io.github.tanharoh.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.stetho.Stetho;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

public class MainActivity extends AppCompatActivity
{
    ToDoDatabaseHelper dbHelper;
    ToDoCursorAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SlideAndDragListView listview = (SlideAndDragListView) findViewById(R.id.list_item);
        assert listview != null;
        Menu menu = new Menu(new ColorDrawable(Color.WHITE), true, 0);//第2个参数表示滑动item是否能滑的过量(true表示过量，就像Gif中显示的那样；false表示不过量，就像QQ中的那样)
        menu.addItem(new MenuItem.Builder().setWidth(200)//单个菜单button的宽度
                .setBackground(new ColorDrawable(Color.RED))//设置菜单的背景
                .setText("删除")//set text string
                .setTextColor(Color.WHITE)//set text color
                .setTextSize(15)//set text size
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(120)
                .setBackground(new ColorDrawable(Color.WHITE))
                .setDirection(MenuItem.DIRECTION_RIGHT)//设置方向 (默认方向为DIRECTION_LEFT)
                .setIcon(getResources().getDrawable(R.drawable.trash))// set icon
                .build());


        listview.setMenu(menu);
        dbHelper = new ToDoDatabaseHelper(this);
        final Cursor todoCursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM ToDoList", null);
        todoAdapter = new ToDoCursorAdapter(this, todoCursor, 0);
        listview.setAdapter(todoAdapter);


        listview.setOnListItemClickListener(new SlideAndDragListView.OnListItemClickListener()
        {
            @Override
            public void onListItemClick(View view, int position)
            {
                //获取点击item的内容
                if (view != null)
                {
                    TextView tv = (TextView) view.findViewById(R.id.item_textview);
                    String text = tv.getText().toString();
                    Intent intent = new Intent(MainActivity.this, EditToDoItem.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("text", text);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

        });


        listview.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener()
        {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction)
            {
//                switch (direction)
//                {
//                    case MenuItem.DIRECTION_LEFT:
//                        switch (buttonPosition)
//                        {
//                            case 0://One
//                                return Menu.ITEM_SCROLL_BACK;
//                        }
//                        break;
//                    case MenuItem.DIRECTION_RIGHT:
//                        switch (buttonPosition)
//                        {
//                            case 0://icon
//                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
//                        }
//                        break;
//                    default:
//                        return Menu.ITEM_NOTHING;
//                }
                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
            }
        });

        listview.setOnItemDeleteListener(new SlideAndDragListView.OnItemDeleteListener()
        {
            @Override
            public void onItemDelete(View view, int position)
            {
                String text = ((TextView) view.findViewById(R.id.item_textview)).getText().toString();
                dbHelper.getWritableDatabase().execSQL("DELETE FROM ToDoList WHERE todo = \'" + text + "\'");
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM ToDoList", null);
                todoAdapter.swapCursor(cursor);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CreatToDoItem.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        dbHelper = new ToDoDatabaseHelper(this);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM ToDoList", null);
        todoAdapter.changeCursor(cursor);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //按两次返回键退出程序
    private long mPressedTime = 0;

    @Override
    public void onBackPressed()
    {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000)
        {//比较两次按键时间差
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else
        {//退出程序
            this.finish();
            System.exit(0);
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //退出时关闭SqlDatabase
        if (dbHelper != null)
        {
            dbHelper.close();

        }
    }
}

