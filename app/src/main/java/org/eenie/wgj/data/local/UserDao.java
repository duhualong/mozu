package org.eenie.wgj.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import org.eenie.wgj.di.ApplicationContext;
import org.eenie.wgj.model.response.Login;
import org.eenie.wgj.util.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User data dao
 */
@Singleton
public class UserDao {
    private DBHelper mDBHelper;

    @Inject
    public UserDao(@ApplicationContext Context context) {
        mDBHelper = new DBHelper(context);
    }

    /**
     * 初始化用户数据
     *
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long initUserData(Login login) {
        SQLiteDatabase database = null;
        try {


            database = mDBHelper.getWritableDatabase();
            clearAll(database, login.getToken());
            ContentValues values = new ContentValues();
            values.put(DBHelper.TOKEN, login.getToken());
            values.put(DBHelper.UNIQUE, login.getUnique());
            values.put(DBHelper.TYPE, login.getType());
            values.put(DBHelper.PERMISSIONS, login.getPermissions());
            values.put(DBHelper.PERMISSIONS_NAME, login.getPermissions_name());
            values.put(DBHelper.USERNAME, login.getUsername());
            values.put(DBHelper.PROJECT_ID, login.getProject_id());
            values.put(DBHelper.BANK_CARD, login.getBank_card());
            values.put(DBHelper.SECURITY_CARD, login.getSecurity_card());
            values.put(DBHelper.NAME, login.getName());
            values.put(DBHelper.PEOPLE, login.getPeople());
            values.put(DBHelper.AVATAR, login.getAvatar());
            values.put(DBHelper.GENDER, login.getGender());
            values.put(DBHelper.BIRTHDAY, login.getBirthday());
            values.put(DBHelper.ADDRESS, login.getAddress());
            values.put(DBHelper.NUMBER, login.getNumber());
            values.put(DBHelper.PUBLISHER, login.getPublisher());
            values.put(DBHelper.VALIDATE, login.getValidate());
            values.put(DBHelper.ID_CARD_POSITIVE, login.getId_card_positive());
            values.put(DBHelper.ID_CARD_NEGATIVE, login.getId_card_negative());
            values.put(DBHelper.ID_CARD_HEAD_IMAGE, login.getId_card_head_image());
            values.put(DBHelper.HEIGHT, login.getHeight());
            values.put(DBHelper.GRADUATE, login.getGraduate());
            values.put(DBHelper.TELEPHONE, login.getTelephone());
            values.put(DBHelper.LIVING_ADDRESS, login.getLiving_address());
            values.put(DBHelper.EMERGENCY_CONTACT, login.getEmergency_contact());
            values.put(DBHelper.INDUSTY, login.getIndustry());
            values.put(DBHelper.SKILL, login.getSkill());
            values.put(DBHelper.CHANNEL, login.getChannel());
            values.put(DBHelper.EMPLOYER, login.getEmployer());
            values.put(DBHelper.WORK_CONTENT, login.getWork_content());
            values.put(DBHelper.WORK_EXPERIENCE, login.getWork_experience());
            return database.insert(DBHelper.TABLE_USER, null, values);

        } finally {
            Utils.close(database);
        }


    }


    /**
     * 清除SQLite所有数据
     *
     * @param database SQLiteDatabase database
     */
    public int clearAll(SQLiteDatabase database, String token) {
        return database.delete(DBHelper.TABLE_USER, DBHelper.TOKEN + "=?", new String[]{token});
    }


    /**
     * Save user info
     *
     * @param
     * @return the row ID of the newly inserted row, or -1 if an error occurrence.
     */
//  public long saveUserData(User user) {
//    SQLiteDatabase database = null;
//    try {
//      database = mDBHelper.getWritableDatabase();
//      ContentValues values = new ContentValues();
//      values.put(DBHelper.FIELD_UID, Integer.parseInt(user.getId()));
//      values.put(DBHelper.FIELD_PHONE, user.getPhone());
//      values.put(DBHelper.FIELD_NAME, user.getName());
//      values.put(DBHelper.FIELD_BALANCE, user.getBalance());
//      values.put(DBHelper.FIELD_RATING, user.getGade());
//      values.put(DBHelper.FIELD_JOB_TYPE, user.getIdentitytype());
//      values.put(DBHelper.FIELD_INTRO, user.getIntroduce());
//      values.put(DBHelper.FIELD_GALLERY, user.getMien());
//      values.put(DBHelper.FIELD_LONGITUDE, user.getLongitude());
//      values.put(DBHelper.FIELD_LATITUDE, user.getLatitude());
//      values.put(DBHelper.FIELD_AREAID, user.getAreaid());
//      values.put(DBHelper.FIELD_ADDRESS, user.getSpecificaddress());
//      values.put(DBHelper.FIELD_AVATAR, user.getPicture());
//      values.put(DBHelper.FIELD_INVITE_CODE, user.getByinvite()); //邀请我的码
//      values.put(DBHelper.FIELD_MY_INVITE_CODE, user.getInvite()); //我的邀请码
//      values.put(DBHelper.FIELD_MY_BANK_ACCOUNT, user.getCardno());//我的卡号
//      values.put(DBHelper.FIELD_MY_AMOUNT, user.getAmount());//我的余额
//
//      return database.insert(DBHelper.TABLE_USER, null, values);
//    } finally {
//      Utils.close(database);
//    }
//  }
//
//  /**
//   * Save user location
//   *
//   * @param user user data object
//   * @return the row ID of the newly inserted row, or -1 if an error occurrence.
//   */
//  public long updateUserLocation(User user) {
//    SQLiteDatabase database = null;
//    try {
//      database = mDBHelper.getWritableDatabase();
//      ContentValues values = new ContentValues();
//      values.put(DBHelper.FIELD_LONGITUDE, user.getLongitude());
//      values.put(DBHelper.FIELD_LATITUDE, user.getLatitude());
//      values.put(DBHelper.FIELD_AREAID, user.getAreaid());
//      values.put(DBHelper.FIELD_ADDRESS, user.getSpecificaddress());
//      values.put(DBHelper.FIELD_MY_AMOUNT, user.getAmount());
//
//      return database.update(DBHelper.TABLE_USER, values, DBHelper.FIELD_UID + "=?",
//          new String[] { user.getId() });
//    } finally {
//      Utils.close(database);
//    }
//  }
//
//  /**
//   * Save user location
//   *
//   * @param userId user id
//   * @param latLng user location info
//   * @return the row ID of the newly inserted row, or -1 if an error occurrence.
//   */
//  public long updateUserLocation(String userId, LatLng latLng) {
//    SQLiteDatabase database = null;
//    try {
//      database = mDBHelper.getWritableDatabase();
//      ContentValues values = new ContentValues();
//      values.put(DBHelper.FIELD_LATITUDE, latLng.getLat());
//      values.put(DBHelper.FIELD_LONGITUDE, latLng.getLng());
//      return database.update(DBHelper.TABLE_USER, values, DBHelper.FIELD_UID + "=?",
//          new String[] { userId });
//    } finally {
//      Utils.close(database);
//    }
//  }
//
//  /**
//   * 获取用户地址信息
//   *
//   * @param userId user id
//   * @return 用户地址信息
//   */
//  public ModifyAddress getUserLocation(String userId) {
//    ModifyAddress modifyAddress = null;
//
//    String[] projections = {
//        DBHelper.FIELD_LATITUDE, DBHelper.FIELD_LONGITUDE, DBHelper.FIELD_AREAID,
//        DBHelper.FIELD_ADDRESS
//    };
//
//    SQLiteDatabase database = null;
//    Cursor cursor = null;
//    try {
//      database = mDBHelper.getReadableDatabase();
//      cursor = database.query(DBHelper.TABLE_USER, projections, DBHelper.FIELD_UID + "=?",
//          new String[] { userId }, null, null, null);
//      if (cursor.moveToFirst()) {
//        String latitude = cursor.getString(0);
//        String longitude = cursor.getString(1);
//        String areaId = cursor.getString(2);
//        String address = cursor.getString(3);
//        modifyAddress = new ModifyAddress(userId, areaId, latitude, longitude, address);
//      }
//      return modifyAddress;
//    } finally {
//      Utils.close(cursor);
//      Utils.close(database);
//    }
//  }
//
//  /**
//   * 提现到银行卡
//   */
//  public User getUserAccountInfo(String userId) {
//    User user = null;
//    String[] account = { DBHelper.FIELD_MY_BANK_ACCOUNT, DBHelper.FIELD_MY_AMOUNT };
//    SQLiteDatabase database = null;
//    Cursor cursor = null;
//    try {
//      database = mDBHelper.getReadableDatabase();
//      cursor = database.query(DBHelper.TABLE_USER, account, DBHelper.FIELD_UID + "=?",
//          new String[] { userId }, null, null, null);
//      if (cursor.moveToFirst()) {
//        String bankAccount = cursor.getString(0);
//        String amount = cursor.getString(1);
//        user = new User(userId, bankAccount, amount);
//      }
//      return user;
//    } finally {
//      Utils.close(cursor);
//      Utils.close(database);
//    }
//  }
//  /**
//   *
//   */
//
//
//  /**
//   * Get User profile data by user id
//   *
//   * @param userId user id
//   * @return User object
//   */
//  public Profile getProfileById(String userId) {
//    Profile profile = null;
//    SQLiteDatabase database = null;
//    Cursor cursor = null;
//    String whereClause = DBHelper.FIELD_UID + "=?";
//    String[] selectProjection = {
//        DBHelper.FIELD_UID, DBHelper.FIELD_PHONE, DBHelper.FIELD_NAME, DBHelper.FIELD_MY_AMOUNT,
//        DBHelper.FIELD_AVATAR, DBHelper.FIELD_JOB_TYPE, DBHelper.FIELD_RATING,
//        DBHelper.FIELD_ADDRESS, DBHelper.FIELD_INTRO, DBHelper.FIELD_MY_INVITE_CODE,
//        DBHelper.FIELD_GALLERY
//    };
//
//    try {
//      database = mDBHelper.getReadableDatabase();
//      cursor = database.query(DBHelper.TABLE_USER, selectProjection, whereClause,
//          new String[] { userId }, null, null, null);
//      if (cursor.moveToFirst()) {
//        String id = cursor.getString(0);
//        String phone = cursor.getString(1);
//        String name = cursor.getString(2);
//        String assets = cursor.getString(3);
//        String avatar = cursor.getString(4);
//        String jobType = cursor.getString(5);
//        String rating = cursor.getString(6);
//        String address = cursor.getString(7);
//        String intro = cursor.getString(8);
//        String inviteCode = cursor.getString(9);
//        String gallery = cursor.getString(10);
//
//        profile = new Profile(id, phone, name, assets, avatar);
//        profile.setJobType(jobType);
//        profile.setRating(rating);
//        profile.setAddress(address);
//        profile.setIntroduce(intro);
//        profile.setInviteCode(inviteCode);
//        profile.setGallery(gallery);
//      }
//      return profile;
//    } finally {
//      Utils.close(cursor);
//      Utils.close(database);
//    }
//  }
//
//  /**
//   * Update user data
//   *
//   * @param userId user id
//   * @param pair user data pair
//   * @return the number of rows affected
//   */
  public int update(String userId, Pair<String, String> pair) {
    SQLiteDatabase database = null;
    try {
      database = mDBHelper.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(pair.first, pair.second);
      return database.update(DBHelper.TABLE_USER, values, DBHelper.TOKEN + "=?",
          new String[] { userId });
    } finally {
      Utils.close(database);
    }
  }
}
