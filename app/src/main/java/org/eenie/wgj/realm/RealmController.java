package org.eenie.wgj.realm;

import android.content.Context;

import org.eenie.wgj.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;


@Singleton
public class RealmController {

  private static final String REALM_FILE_NAME = "area.realm";

  private static Realm realm;
  private Context context;

  @Inject
  public RealmController(Context context) {
    this.context = context;
  }

  /**
   * Area file realm configuration
   *
   * @return area realm configuration.
   */
  public RealmConfiguration getAreaConfig() {
    copyBundledRealmFile(context, context.getResources().openRawResource(R.raw.area),
        REALM_FILE_NAME);
    return new RealmConfiguration.Builder(context).name(REALM_FILE_NAME).build();
  }

  /**
   * 获取地址查询Realm
   *
   * @return area realm instance
   */
  public Realm getAreaRealm() {
    if (realm == null) {
      realm = Realm.getInstance(getAreaConfig());
    }
    return realm;
  }

  /**
   * 将res/raw文件夹下的realm文件拷贝到/data/data/.../files/目录
   *
   * @param inputStream 输入流
   * @param outFileName 输出文件名称
   * @return 拷贝文件的绝对路径
   */
  public static String copyBundledRealmFile(Context context, InputStream inputStream,
                                            String outFileName) {
    try {
      File file = new File(context.getFilesDir(), outFileName);
      FileOutputStream outputStream = new FileOutputStream(file);
      byte[] buf = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buf)) > 0) {
        outputStream.write(buf, 0, bytesRead);
      }
      outputStream.close();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
