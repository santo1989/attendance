package com.santosystemsandsolutions.attendance;

//import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
//import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager LayoutManager;

    ArrayList<ClassItem> classitems = new ArrayList<>();

    Toolbar toolbar;

    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());
        loadData();


        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        classAdapter = new ClassAdapter(this, classitems);
        recyclerView.setAdapter(classAdapter);

        classAdapter.setOnItemClickListener(position -> gotoIteamActivity(position));

        SetToolbar();

    }

    private void loadData() {
        Cursor cursor = dbHelper.getClassTable();

        classitems.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.C_ID));
            String className = cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY));
            classitems.add(new ClassItem(id, className, subjectName));
        }

    }

    private void SetToolbar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Student Attendance App");
        subtitle.setText("Developer:\n Md. Hasibul Islam Santo\n B.Sc in CSE, 4th year 2nd Semester\n ID:13-0-52-020-003 ");
        //subtitle.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);


    }

    private void gotoIteamActivity(int position) {
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("className", classitems.get(position).getClassName());
        intent.putExtra("subjectName", classitems.get(position).getSubjectName());
        intent.putExtra("position", position);
        intent.putExtra("cid", classitems.get(position).getCid());
        startActivity(intent);

    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.Class_ADD_DIALOG);
        dialog.setListener((className, subjectName) -> addClass(className, subjectName));


    }

    private void addClass(String className, String subjectName) {

        long cid = dbHelper.addClass(className, subjectName);
        ClassItem classItem = new ClassItem(cid, className, subjectName);
        classitems.add(classItem);
        classAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
    MyDialog dialog = new MyDialog();
    dialog.show(getSupportFragmentManager(),MyDialog.Class_UPDATE_DIALOG);
    dialog.setListener((className,subjectName)->update(position,className,subjectName));
    }

    private void update(int position, String className, String subjectName) {
        dbHelper.updateClass(classitems.get(position).getCid(),className,subjectName);
        classitems.get(position).setClassName(className);
        classitems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classitems.get(position).getCid());
        classitems.remove(position);
        classAdapter.notifyItemRemoved(position);

    }
}