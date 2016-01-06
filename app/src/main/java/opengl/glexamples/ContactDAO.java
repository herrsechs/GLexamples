package opengl.glexamples;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Angel on 16/1/6.
 */
public class ContactDAO {

        private DBHelper dbHelper;

        public ContactDAO(Context context) {
            dbHelper = new DBHelper(context);
        }

        public int getCount(){
            SQLiteDatabase db=dbHelper.getReadableDatabase();
            Cursor cursor=db.rawQuery("select id from contact", null);
            return cursor.getCount();
        }

        public void insert(UserEntity userEntity) {

            //Open connection to write data
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String id=userEntity.getId();
            String name=userEntity.getName();
            String phone=userEntity.getPhone();
            String email=userEntity.getEmail();
            String category=userEntity.getCategory();
            String style=userEntity.getStyle();


            String sql="insert into contact('id','name','phone','email','category','style') " +
                    "values (?,?,?,?,?,?)";
            db.execSQL(sql, new String[]{id, name, phone, email, category, style});
            db.close(); // Closing database connection
            return;
        }

        public void delete(int contactId) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // It's a good practice to use parameter ?, instead of concatenate string
            db.delete("contact", "id" + "= ?", new String[] { String.valueOf(contactId) });
            db.close(); // Closing database connection
        }

        public void update(UserEntity userEntity) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql="UPDATE contact SET name=?,phone=?,email=?,category=?,style=? where id=?";
            db.execSQL(sql,new String[]{userEntity.getName(),
                                        userEntity.getPhone(),
                                        userEntity.getEmail(),
                                        userEntity.getCategory(),
                                        userEntity.getStyle(),
                                        userEntity.getId()}
                                        );
            db.close(); // Closing database connection
        }

        public ArrayList<String> getUserByCategory(String category) {
            //Open connection to read only
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT * FROM contact" ;

            ArrayList<String> usernames = new ArrayList<String>();

            Cursor cursor = db.rawQuery(selectQuery, null);


            if (cursor.moveToFirst()) {
                do {
                    String _id=cursor.getString(0);
                    String _name=cursor.getString(1);
                    String _phone=cursor.getString(2);
                    String _email=cursor.getString(3);
                    String _category=cursor.getString(4);
                    String _style=cursor.getString(5);

                    if(_category.equals(category)){
                        usernames.add(_name);
                    }


                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return usernames;

        }

        public UserEntity getUserById(String id){
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT * FROM contact where id=?";

            int iCount =0;
            UserEntity user = new UserEntity();

            Cursor cursor = db.rawQuery(selectQuery, new String[] { id } );

            if (cursor.moveToFirst()) {
                do {
                    user.setId(cursor.getString(0));
                    user.setName(cursor.getString(1));
                    user.setPhone(cursor.getString(2));
                    user.setEmail(cursor.getString(3));
                    user.setCategory(cursor.getString(4));
                    user.setStyle(cursor.getString(5));

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return user;
        }

    public int getUserByName(String name){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT id FROM contact where name=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { name } );

        String id=null;

        if (cursor.moveToFirst()) {
           id=cursor.getString(0);
        }

        cursor.close();
        db.close();
        if(id.isEmpty()){
            return -1;  //find no user
        }else {
            return Integer.parseInt(id);
        }

    }
}
