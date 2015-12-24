package opengl.glexamples;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

/**
 * Created by Angel on 15/12/24.
 */
public class ContactDatabase {
    SQLiteDatabase sld;

    // Create or Open ContactDatabase
    public ContactDatabase() {
        try {
            sld = SQLiteDatabase.openDatabase(
                    "/data/data/opengl.glexamples/mydb",
                    null,
                    SQLiteDatabase.OPEN_READWRITE |
                            SQLiteDatabase.CREATE_IF_NECESSARY
            );

            String sql = "create table if not exists contacts" +
                    "( `id` VARCHAR(45) NOT NULL,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `phone` VARCHAR(45) NOT NULL,\n" +
                    "  `email` VARCHAR(45) NULL,\n" +
                    "  `group` INT NOT NULL DEFAULT 0,\n" +
                    "  `DATA1` VARCHAR(45) NULL,\n" +
                    "  `DATA2` VARCHAR(45) NULL,\n" +
                    "  `DATA3` VARCHAR(45) NULL,\n" +
                    "  `DATA4` VARCHAR(45) NULL,\n" +
                    "  `DATA5` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`id`))";
            sld.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        try {
            sld.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String id,String name,String phone,String email,int group) {
        try {
            String sql="insert into contacts values (?,?,?,?,0,null,null,null,null,null)";
            sld.execSQL(sql, new String[]{id, name, phone, email});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            String sql = "delete from contacts;";
            sld.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            String sql = "UPDATE contacts\n" +
                    "SET group = 1\n" +
                    "WHERE name='安俊平';";
            sld.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount(){
        Cursor cursor=sld.rawQuery("select id from contacts",null);
        return cursor.getCount();
    }

    public ArrayList<String> getAllNames(){
        ArrayList<String> names=new ArrayList<>();
        Cursor cursor=sld.rawQuery("select name from contacts",null);
        while (cursor.moveToNext()){
            String name=cursor.getString(0);
            names.add(name);
        }
        return names;
    }

    public int findUserByName(String name){
        int idx=-1;
        Cursor cursor=sld.rawQuery("select * from contacts where name=?",new String[]{name});
        if(cursor.moveToNext()){
            idx=Integer.parseInt(cursor.getString(0));
        }

        return idx;
    }


}

