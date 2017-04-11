package org.eenie.wgj.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBHelper
 * Created by Zac on 2016/6/12.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "msql.db";
    public static final String TABLE_USER = "user";

    public static final String TOKEN = "token";
    public static final String UNIQUE = "unique";
    public static final String TYPE = "type";
    public static final String PERMISSIONS = "permissions";
    public static final String PERMISSIONS_NAME = "permissions_name";
    public static final String USERNAME = "username";
    public static final String PROJECT_ID = "project_id";
    public static final String BANK_CARD = "bank_card";
    public static final String SECURITY_CARD = "security_card";
    public static final String NAME = "name";
    public static final String PEOPLE = "people";
    public static final String AVATAR = "avatar";
    public static final String GENDER = "gender";
    public static final String BIRTHDAY = "birthday";
    public static final String ADDRESS = "address";
    public static final String NUMBER = "number";
    public static final String PUBLISHER = "publisher";
    public static final String VALIDATE = "validate";
    public static final String ID_CARD_POSITIVE = "id_card_positive";
    public static final String ID_CARD_NEGATIVE = "id_card_negative";
    public static final String ID_CARD_HEAD_IMAGE = "id_card_head_image";
    public static final String  HEIGHT="height";
    public static final String GRADUATE="graduate";
    public static final String TELEPHONE="telephone";
    public static final String LIVING_ADDRESS="living_address";
    public static final String EMERGENCY_CONTACT="emergency_contact";
    public static final String  INDUSTY="industry";
    public static final String SKILL="skill";
    public static final String CHANNEL="channel";
    public static final String EMPLOYER="employer";
    public static final String WORK_CONTENT="work_content";
    public static final String WORK_EXPERIENCE="work_experience";




    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "(" + TOKEN + " INTEGER PRIMARY KEY," +
                UNIQUE + " TEXT," +
                TYPE + " TEXT," +
                PERMISSIONS + " TEXT, " +
                PERMISSIONS_NAME + " TEXT, " +
                USERNAME + " TEXT, " +
                PROJECT_ID + " TEXT, " +
                BANK_CARD + " TEXT, " +
                SECURITY_CARD + " TEXT, " +
                NAME + " TEXT, " +
                PEOPLE + " TEXT, " +
                AVATAR + " TEXT, " +
                GENDER + " TEXT, " +
                BIRTHDAY + " TEXT, " +
                ADDRESS + " TEXT, " +
                NUMBER + " TEXT, " +
                PUBLISHER + " TEXT, " +
                VALIDATE + " TEXT, " +
                ID_CARD_POSITIVE + " TEXT, " +
                ID_CARD_NEGATIVE + " TEXT, " +
                ID_CARD_HEAD_IMAGE + " TEXT, " +
                HEIGHT + " TEXT, " +
                GRADUATE + " TEXT, " +
                TELEPHONE + " TEXT, " +
                LIVING_ADDRESS + " TEXT, " +
                EMERGENCY_CONTACT + " TEXT, " +
                SKILL + " TEXT, " +
                CHANNEL + " TEXT, " +
                EMPLOYER + " TEXT, " +
                WORK_CONTENT + " TEXT, " +
                WORK_EXPERIENCE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    }
}
