package org.eenie.wgj.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.NewResponse;
import org.eenie.wgj.model.requset.AddKeyPersonalInformation;
import org.eenie.wgj.model.requset.BirthdayDetail;
import org.eenie.wgj.model.requset.BuildNewProject;
import org.eenie.wgj.model.requset.CaptchaChecked;
import org.eenie.wgj.model.requset.CreataCompanyRequest;
import org.eenie.wgj.model.requset.GiveBirthday;
import org.eenie.wgj.model.requset.JoinCompany;
import org.eenie.wgj.model.requset.MLogin;
import org.eenie.wgj.model.requset.ModifyInfo;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.MApi;
import org.eenie.wgj.model.response.ShootList;

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

    //检验用户名
    @GET("user/exist")
    Single<ApiResponse> checkedPhone(@Query("username") String username);

    //登录
    @POST("user/login")
    Single<ApiResponse> logined(@Body MLogin login);

    @POST("login")
    Single<ApiResponse> postLogin(@Body MLogin login);

    //发送验证码
    @POST("sms/get")
    @FormUrlEncoded
    Single<ApiResponse> fetchMessageCode(@Field("telephone") String telephone);

    //验证码校验
    @POST("sms/check")
    Single<ApiResponse> verifyCode(@Body CaptchaChecked captchaChecked);

    //重置密码
    @POST("user/resetpasswd")
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
    Single<ApiResponse> getContacts(@Header("token") String token);

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
    Single<ApiResponse> getToDoNotice(@Header("token") String token);

    //查询待办事项详情
    @GET("matterRemind/Info")
    Single<ApiResponse> getMessageById(@Header("token") String token,
                                       @Query("id") int id);

    //通知
    @GET("noticeList")
    Single<ApiResponse> getNotice(@Header("token") String token);

    //生日列表
    @GET("birthdaylist")
    Single<ApiResponse> getBirthdayList(@Header("token") String token);

    //查询个人生日详情
    @GET("birthdayInfo")
    Single<ApiResponse<BirthdayDetail>> getBirthdayById(@Header("token") String token,
                                                        @Query("id") String id);

    //赠送生日祝福
    @POST("birthdayBlessing")
    Single<ApiResponse> giveBirthdayBlessing(@Header("token") String token,
                                             @Body GiveBirthday giveBirthday);

    //获取异常处理信息列表
    @GET("readilyShoot/ListInfo")
    Single<ApiResponse> getAbnormalHandleList(@Header("token") String token);

    //通过userid获取用户信息
    @POST("user/getinfo")
    Single<ApiResponse> getUserInfoById(@Header("token") String token, @Body UserId userId);

    //修改个人信息
    @POST("user/update")
    Single<ApiResponse> modifyInforById(@Header("token") String token, @Body ModifyInfo modifyInfo);

    //创建公司
    @POST("company/create")
    Single<MApi>
    createCompany(@Header("token") String token, @Body CreataCompanyRequest creataCompanyRequest);

    //获取所有公司列表
    @GET("company/all")
    Single<ApiResponse> getCompanyList();

    //获取某一城市下的公司
    @GET("company/city")
    Single<ApiResponse> getCityCompanyList(@Query("city") String city);

    //加入公司
    @POST("company/join")
    Single<NewResponse> joinCompany(@Header("token") String token, @Body JoinCompany joinCompany);

    //查询加入申请公司状态
    @POST("user/apply")
    Single<ApiResponse> joinCompanyState(@Header("token") String token);

    //获取所有项目的列表

    /**
     * 获取项目列表
     *
     * @param token
     * @return
     */
    @GET("project/list")
    Single<ApiResponse> getProjectList(@Header("token") String token);

    //新建项目列表
    @POST("project/add")
    Single<ApiResponse> newProject(@Header("token") String token,
                                   @Body BuildNewProject buildNewProject);

    //删除项目
    @POST("project/delete")
    Single<ApiResponse> deleteProject(@Header("token") String token,
                                      @Body BuildNewProject buildNewProject);

    //修改项目
    @POST("project/modify")
    Single<ApiResponse> modifyProject(@Header("token") String token,
                                      @Body BuildNewProject buildNewProject);

    //获取关键人物信息列表
    @GET("ownerIndexList")
    Single<ApiResponse> getKeyProjectContact(@Header("token") String token, @Query("projectid")
            String projectId);

    //删除关键人物信息
    @GET("ownerDelete")
    Single<ApiResponse> deleteKeyPersonal(@Header("token") String token, @Query("id") int id);

    //修改关键人物信息
    @POST("ownerUpdate")
    Single<ApiResponse> updateKeyPersonal(@Header("token") String token,
                                          @Body AddKeyPersonalInformation addKeyPersonalInformation);

    //获取交接班列表
    @GET("precautionList")
    Single<ApiResponse> getExchangeWorkList(@Header("token") String token, @Query("projectid")
            String projectId);

    //交接班删除
    @GET("precautionDelete")
    Single<ApiResponse> deleteExchangeWorkItem(@Header("token") String token, @Query("id") int id);


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
