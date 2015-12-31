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

            String sql = "create table if not exists contact" +
                    "( `id` VARCHAR(45) NOT NULL,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `phone` VARCHAR(45) NOT NULL,\n" +
                    "  `email` VARCHAR(45) NULL,\n" +
                    "  `group` VARCHAR(45) NOT NULL,\n" +
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

    public void insert(String id,String name,String phone,String email) {
        try {
            String sql="insert into contact values (?,?,?,?,'1',null,null,null,null,null)";
            sld.execSQL(sql, new String[]{id, name, phone, email});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer position) {
        try {
            String sql = "delete from contact where id="+position.toString();
            sld.execSQL(sql);
//            sld.delete("contact","id",new String[]{position.toString()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            //TODO:update fail to execute.
            String sql = "update contact set group='2' where id='1';";
            sld.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCount(){
        Cursor cursor=sld.rawQuery("select id from contact",null);
        return cursor.getCount();
    }

    public ArrayList<String> getAllNames(){
        ArrayList<String> names=new ArrayList<>();
        Cursor cursor=sld.rawQuery("select name from contact",null);
        while (cursor.moveToNext()){
            String name=cursor.getString(0);
            names.add(name);
        }
        return names;
    }

    public int findUserByName(String name){
        int idx=-1;
        Cursor cursor=sld.rawQuery("select * from contact where name=?",new String[]{name});
        if(cursor.moveToNext()){
            idx=Integer.parseInt(cursor.getString(0));
        }

        return idx;
    }

    public UserEntity findUserById(String id){
        UserEntity user;
        Cursor cursor=sld.rawQuery("select * from contact where id=?",new String[]{id});
        if(cursor.moveToNext()){
            String name=cursor.getString(0);
            String phone=cursor.getString(1);
            String email=cursor.getString(2);
            String group=cursor.getString(3);

            user=new UserEntity(name,phone,email,group);
        }else{
            user=new UserEntity();
        }

        return user;
    }
    public ArrayList<String> findUserByGroup(String group){
        if(group.equals("0")) return getAllNames();

        ArrayList<String> usernames=new ArrayList<>();
        String name;
        Cursor cursor=sld.rawQuery("select * from contact",null);
        while(cursor.moveToNext()){
            if(cursor.getString(4).equals(group)) {
                name = cursor.getString(1);
                usernames.add(name);
            }
        }

        //sld.rawQuery("select name from contact where group=?",new String[]{group} );
        return usernames;
    }

}

