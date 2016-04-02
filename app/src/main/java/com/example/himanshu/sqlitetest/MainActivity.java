package com.example.himanshu.sqlitetest;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    EditText userName,password,name,nameandpass;
    ImageView imageView;
    HimDatabaseAdapter himHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName= (EditText) findViewById(R.id.userNameValue);
        password= (EditText) findViewById(R.id.passwordValue);
        name= (EditText) findViewById(R.id.name);
        nameandpass= (EditText) findViewById(R.id.nameandpass);
        imageView= (ImageView) findViewById(R.id.imageView);


        himHelper=new HimDatabaseAdapter(this);
        Bitmap m1= BitmapFactory.decodeResource(getResources(),R.drawable.m1);
        Bitmap m2= BitmapFactory.decodeResource(getResources(),R.drawable.m2);
        Bitmap m3= BitmapFactory.decodeResource(getResources(),R.drawable.m3);
        Bitmap m4= BitmapFactory.decodeResource(getResources(),R.drawable.m4);
       // addImage(m1);
        //addImage(m2);
        //addImage(m3);
        // addImage(m4);
    }

    public void addImage(Bitmap bitmap){
        byte[] bytes=DbBitmapUtility.getBytes(bitmap);
        Logger.message(this,"pushing image");
        himHelper.addImage(bytes);
    }

    public void nextImage(View view) {
        Bitmap loadedBitmap = himHelper.loadNextImage();
        imageView.setImageBitmap(loadedBitmap);
    }
    public void addUser(View view){
        String user=userName.getText().toString();
        String pass=password.getText().toString();

        long id= himHelper.insertData(user,pass);

        if(id<0){
            Logger.message(this,"Unsuccesful");
        }
        else {
            Logger.message(this,"Succesfully inseted  Row no "+id);
        }
    }

    public void viewDetails(View view){
        String data=himHelper.getAllData();
        Logger.message(this,data);
    }

    public void getDetails(View view){
        String s1=name.getText().toString();
        String s2=himHelper.getData(s1);
        Logger.message(this,s2);
    }

    public void getId(View view){
        String s1=nameandpass.getText().toString();
        String sub1=s1.substring(0, s1.indexOf(" "));
        String sub2=s1.substring(s1.indexOf(" ") + 1);
        String s3=himHelper.getId(sub1, sub2);
        Logger.message(this,s3);
    }


    public void update(View view){
        himHelper.updateName("himanshu","himu");
    }
    public void delete(View view){
        int count=himHelper.deleteRow();
        Logger.message(this,"no. of row deleted ="+count);
    }

}
