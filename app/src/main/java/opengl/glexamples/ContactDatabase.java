package opengl.glexamples;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Angel on 15/12/24.
 */
public class ContactDatabase {
    SQLiteDatabase sld;

    // Create or Open ContactDatabase
    ContactDatabase() {
        try {
            sld = SQLiteDatabase.openDatabase(
                    "/data/data/opengl.glexamples/mydb",
                    null,
                    SQLiteDatabase.OPEN_READWRITE |
                            SQLiteDatabase.CREATE_IF_NECESSARY
            );

            String sql = "create table if not exists contacts" +
                    "(sno char(5),stuname varchar(20)," +
                    "sage integer,sclass char(5))";
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

    public void insert() {
        try {
            String sql = "insert into student values" +
                    "('001','Android',22,'283')";
            sld.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            String sql = "delete from student;";
            sld.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //query()


}

