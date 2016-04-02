package com.example.himanshu.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * Created by himanshu on 02/04/16.
 */


    public class HimDatabaseAdapter{

    TextView text;
    HimHelper himHelper;
    public  HimDatabaseAdapter(Context context)
    {
        himHelper=new HimHelper(context);
    }

    //********Insert part****************
    public long insertData(String name,String password)
    {
        SQLiteDatabase db=himHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(HimHelper.NAME,name);
        contentValues.put(HimHelper.PASSWORD,password);
        long id=db.insert(HimHelper.TABLE_NAME,null,contentValues);
        return id;
    }

    //************QUERY **********************************************
    /*
    method 1
    query="select from ..."
    Cursor cursor =db.rawQuery(query,null);

    Cursor provide read write access, result set is navigated by cursor, async is to be done when cursor from multiple thread
    getColumnName give the name of the column
    getCount ,no of rows returned
    moveTONEXT ETC

    method 2
    Cursor query(tqble,cloumn,selec, selecArg,group,having,orderby)
    db.query(......);
     */
    public String getAllData(){
        SQLiteDatabase db=himHelper.getWritableDatabase();
        //select _id,name,password from himtable
        String[] columns={HimHelper.UID,HimHelper.NAME,HimHelper.PASSWORD};
       Cursor cursor=db.query(HimHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer=new StringBuffer();
        while(cursor.moveToNext()){
            int index1=cursor.getColumnIndex(HimHelper.UID);
            int cid=cursor.getInt(index1);
            int index2=cursor.getColumnIndex(HimHelper.NAME);
            String name=cursor.getString(index2);
            int index3=cursor.getColumnIndex(HimHelper.PASSWORD);
            String password=cursor.getString(index3);
            if(name!=null)
            buffer.append(cid+" "+name+" "+password+"\n");
        }

        return buffer.toString();
    }

    //************************getData function************
    public String getData(String name){
        //select name,password from himtable where name ="himanshu;"
        SQLiteDatabase db=himHelper.getWritableDatabase();
        String[] columns={HimHelper.NAME,HimHelper.PASSWORD};
        Cursor cursor=db.query(HimHelper.TABLE_NAME, columns,HimHelper.NAME+" = '"+name+"'", null, null, null, null);
        StringBuffer buffer=new StringBuffer();
        while(cursor.moveToNext()){

            int index1=cursor.getColumnIndex(HimHelper.NAME);
            String personName=cursor.getString(index1);
            int index2=cursor.getColumnIndex(HimHelper.PASSWORD);
            String password=cursor.getString(index2);

            buffer.append(name+" "+password+"\n");
        }
        return buffer.toString();
    }

    public String  getId(String name,String password){
        //select _id from himtable where name=? and password=?;
        SQLiteDatabase db=himHelper.getWritableDatabase();
        String[] columns={HimHelper.UID};
        String[] selecArgs={name,password};
        Cursor cursor=db.query(HimHelper.TABLE_NAME, columns,HimHelper.NAME+" = ? AND "+HimHelper.PASSWORD+"=?", selecArgs, null, null, null);
        StringBuffer buffer=new StringBuffer();
        while(cursor.moveToNext()){

            int index0=cursor.getColumnIndex(HimHelper.UID);
            int peronId=cursor.getInt(index0);
            buffer.append(peronId+"\n");
        }
        return buffer.toString();
    }

//*********************************deleting the data
   // get the database from helper class , the delete method returns the no of effected rows
    public int deleteRow(){
        //delete * from himtable where name="him";
        SQLiteDatabase db=himHelper.getWritableDatabase();
        String[] whereArgs={"himanshu"};
        int count=db.delete(HimHelper.TABLE_NAME,HimHelper.NAME+"=?",whereArgs);
        return count;

    }
    //***************************how to update the data
    //get the database,and update method is there with the contentValues to be updated
    public int updateName(String oldName,String newName){
        //update himtable set name='himanshu' where name='himu';
        SQLiteDatabase db=himHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(HimHelper.NAME, newName);
        String[] whereArgs={oldName};
        int c=db.update(HimHelper.TABLE_NAME, contentValues, HimHelper.NAME + "=?", whereArgs);

        return c;
    }


    //**************ENTRING THE IMAGE***************************
    public void addImage(byte[] image){
        SQLiteDatabase db=himHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(HimHelper.IMAGE,image);
        db.insert(HimHelper.TABLE_NAME, HimHelper.NAME, contentValues);

    }

    //***************load next iamge****************
    public Bitmap loadNextImage(){
        SQLiteDatabase db=himHelper.getWritableDatabase();
        String[] columns={HimHelper.IMAGE};
        Cursor cursor=db.query(HimHelper.TABLE_NAME,columns,null,null,null,null,null);
        HimHelper.imageCounter++;
       // HimHelper.imageCounter=HimHelper.imageCounter%4;
        if(HimHelper.imageCounter>4)
        {
            HimHelper.imageCounter=1;
        }
        cursor.move(HimHelper.imageCounter);

        int indexOfImage=cursor.getColumnIndex(HimHelper.IMAGE);
        Bitmap bitmap=DbBitmapUtility.getImage(cursor.getBlob(indexOfImage));
        return bitmap;
    }



    //an adapter has to be made as to protect the data of sqlite table from threat, thus provides security
    //grant something only the minimum access
    //static class can access only the static field of the outer class
    //A nonstatic nested class exist as a seperate instance for every diff object of the outer class
    static class HimHelper extends SQLiteOpenHelper{
        private static final String DATABSE_NAME="HIMDATABASE";
        private static final int DATABASE_VERSION=8;
        private static final String TABLE_NAME="himtable";
        private static final String UID="_id";
        private static final String NAME="Name";
        private static final String IMAGE="Image";
        private static final String PASSWORD="Password";
        private static int imageCounter=1;
        private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
                UID+" INTEGER  PRIMARY KEY AUTOINCREMENT,"+
                NAME+" VARCHAR(255),"+
                IMAGE+" BLOB,"+
                PASSWORD+" VARCHAR(255)"+
                ");";
        private static final String DROP_TABLE="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public HimHelper(Context context) {
            super(context,DATABSE_NAME,null,DATABASE_VERSION);
            this.context=context;
            Logger.message(context,"constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //CREATE TABLE HIMTABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(255), PASSWORD VARCHAR(255)

            try {
                db.execSQL(CREATE_TABLE);
                Logger.message(context," on Create called");
            }
            catch (SQLException e){
                Logger.message(context,e+"");
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                Logger.message(context," onUpgrade Called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }



}