package org.eenie.wgj.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiRes;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AbnormalMessage;
import org.eenie.wgj.model.requset.BirthdayAlert;
import org.eenie.wgj.model.requset.BirthdayDetail;
import org.eenie.wgj.model.requset.CaptchaChecked;
import org.eenie.wgj.model.requset.CreataCompanyRequest;
import org.eenie.wgj.model.requset.GiveBirthday;
import org.eenie.wgj.model.requset.MLogin;
import org.eenie.wgj.model.requset.MeetingNotice;
import org.eenie.wgj.model.requset.MessageDetail;
import org.eenie.wgj.model.requset.ModifyInfo;
import org.eenie.wgj.model.requset.NoticeMessage;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.Contacts;
import org.eenie.wgj.model.response.LoginData;
import org.eenie.wgj.model.response.MApi;
import org.eenie.wgj.model.response.ShootList;
import org.eenie.wgj.model.response.Token;
import org.eenie.wgj.model.response.UserInforById;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Single;

/**
 * Remote service api module
 */
@Singleton
public interface RemoteService {
    String DOMAIN = "http://118.178.88.132:8000/api/";


    @GET("register/checkuser")
    Single<ApiResponse> checkedPhone(@Query("username") String username);


    @POST("logina")
    Single<ApiResponse<LoginData>> logined(@Body MLogin login);

    @POST("login")
    Single<ApiResponse> postLogin(@Body MLogin login);

    //发送验证码
    @POST("fetch_sms_code")
    @FormUrlEncoded
    Single<ApiResponse<Token>> fetchMessageCode(@Field("telephone") String telephone);

    //验证码校验
    @POST("fetch_sms_code/verification")
    Single<ApiRes> verifyCode(@Body CaptchaChecked captchaChecked);


    @POST("login/forgetpassword")
    @FormUrlEncoded
    Single<ApiResponse> modifyPassword(@Field("verify") String captcha,
                                       @Field("newpwd") String newPwd,
                                       @Field("username") String username);


    @Headers("token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTE0NjcwODQ" +
            "sIm5iZiI6MTQ5MTQ2NzA4NSwiZXhwIjoxNTIyNTcxMDg1LCJkYXRhIjp7ImlkIjoxfX0.60X8vq" +
            "CQ-VJ7uKPbkIqxOsZDqZDuudwi-U4E3ebCkTg")
    @POST("readilyShootList")
    Single<ApiResponse<List<ShootList>>> getList();

    @GET("contacts/userList")
    Single<ApiResponse<List<Contacts>>> getContacts(@Header("token") String token);

    @FormUrlEncoded
    @POST("recog.do")
    Single<Api> getPlans(@FieldMap Map<String, Object> data);

    @POST("recog.do")
    @FormUrlEncoded
    Single<Api> upload(@Field("key") String key, @Field("secret") String secret,
                       @Field("typeId") int typeId,
                       @Field("format") String format, @Field("file") File file);

    @POST("register/user")
    Single<ApiResponse> register(@Body RequestBody body);

    //待办事项处理
    @GET("matterRemind")
    Single<ApiResponse<List<MeetingNotice>>> getToDoNotice(@Header("token") String token);

    //查询待办事项详情
    @GET("matterRemind/Info")
    Single<ApiResponse<MessageDetail>> getMessageById(@Header("token") String token,
                                                      @Query("id") int id);

    //通知
    @GET("noticeList")
    Single<ApiResponse<List<NoticeMessage>>> getNotice(@Header("token") String token);

    //生日列表
    @GET("birthdaylist")
    Single<ApiResponse<List<BirthdayAlert>>> getBirthdayList(@Header("token") String token);

    //查询个人生日详情
    @GET("birthdayInfo")
    Single<ApiResponse<BirthdayDetail>> getBirthdayById(@Header("token") String token,
                                                        @Query("id") String id);
    //赠送生日祝福
    @POST("birthdayBlessing")
    Single<ApiResponse>giveBirthdayBlessing(@Header("token")String token,
                                            @Body GiveBirthday giveBirthday);
    //获取异常处理信息列表
    @GET("readilyShoot/ListInfo")
    Single<ApiResponse<List<AbnormalMessage>>>getAbnormalHandleList(@Header("token")String token);

    //通过userid获取用户信息
    @POST("login/getuserinfo")
    Single<ApiResponse<UserInforById>>getUserInfoById(@Body UserId userId);
    //修改个人信息
    @POST("login/updateuserinfo")
    Single<ApiResponse>modifyInforById(@Header("toen")String token, @Body ModifyInfo modifyInfo);
    //创建公司
    @POST("company/create")
    Single<MApi>
    creataCompany(@Body CreataCompanyRequest creataCompanyRequest );



    class Creator {

        @Inject
        public RemoteService createService() {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();


            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(DOMAIN)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RemoteService.class);
        }
    }
}
