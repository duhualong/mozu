package org.eenie.wgj.data.remote;

import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.GaodeResponse;
import org.eenie.wgj.model.response.MApi;
import org.eenie.wgj.model.response.Token;
import org.eenie.wgj.model.response.UploadPointPatrol;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Eenie on 2017/4/16 at 13:23
 * Email: 472279981@qq.com
 * Des:
 */

public interface FileUploadService {

    @POST("recog.do")
    @Multipart
    Call<Api> registerImg(@Part MultipartBody.Part photo, @Part("key") RequestBody key,
                          @Part("secret") RequestBody secret, @Part("typeId") RequestBody typeId,
                          @Part("format") RequestBody format);


    @POST("recog.do")
    @Multipart
    Call<Api> uploadImg(@PartMap Map<String, RequestBody> params);

    @GET("company/city")
    Call<ApiResponse> getCityCompanyList(@Query("city") String city);


    @POST("recog.do")
    Call<Api> upLoad(@Body RequestBody Body);

    //上传文件
    @POST("uploadfile")
    Call<ApiResponse> uploadFile(@Body RequestBody body);

    //注册新接口
    @POST("user/register")
    Call<ApiResponse> registerInforation(@Body RequestBody body);

    //添加关键人的信息
    @POST("ownerAdd")
    Call<ApiResponse> addKeyPersonalInformation(@Header("token") String token, @Body RequestBody body);
    //编辑交接班信息
//    @Multipart
//    @POST("precautionAdd")
//    Call<ApiResponse>addExchangeWorkList(@Header("token")String token,
//                                         @PartMap() Map<String, RequestBody> partMap,
//                                         @Part("file") MultipartBody.Part file);

    //添加交接班
    @POST("precautionAdd")
    Call<ApiResponse> addExchangeWorkList(@Header("token") String token, @Body RequestBody body);

    //编辑交接班
    @POST("precautionUpdate")
    Call<ApiResponse> editExchangeWorkList(@Header("token") String token, @Body RequestBody body);

    //添加岗位培训设置
    @POST("jobtrainingAdd")
    Call<ApiResponse> addTrainingWork(@Header("token") String token, @Body RequestBody body);

    //编辑岗位培训设置
    @POST("jobtrainingUpdate")
    Call<ApiResponse> editTrainingWork(@Header("token") String token, @Body RequestBody body);

    //编辑巡检点设置
    @POST("inspectionpointsUpdate")
    Call<ApiResponse> editInspectionItem(@Header("token") String token, @Body RequestBody body);

    @POST("inspectionpointsAdd")
    Call<ApiResponse> addInspectionItem(@Header("token") String token, @Body RequestBody body);

    //添加工作秀
    @POST("workshowAdd")
    Call<ApiResponse> addWorkShowItem(@Header("token") String token, @Body RequestBody body);

    //添加随手拍
    @POST("readilyShootAdd")
    Call<ApiResponse> addShootPhotoItem(@Header("token") String token, @Body RequestBody body);

    //会议记录
    @POST("meetingMinutes")
    Call<ApiResponse>addMeetingRecord(@Header("token")String token,@Body RequestBody body);

    //签到
    @POST("checkInAdd")
    Call<ApiResponse> signInAttendance(@Header("token") String token, @Body RequestBody body);

    //签退
    @POST("signbackAdd")
    Call<ApiResponse> signOutAttendance(@Header("token") String token, @Body RequestBody body);
    @GET("userSchedulingList")
    Call<ApiResponse> getAttendanceLists(@Header("token") String token, @Query("date") String date);


    //获取巡检轨迹点
    @GET("getPatrolRecords")
    Call<ApiResponse> getRoutingLines(@Header("token")String token, @Query("date")String date,
                                       @Query("inspectionday_id")String inspectiondayId,
                                       @Query("projectid")String projectId,
                                    @Query("user_id")String userId);

     //进行报岗
    @POST("signedtosignAdd")
    Call<ApiResponse>uploadReportPostInfo(@Header("token")String token,@Body RequestBody body);
    //上报巡检点
    @POST("inspectionAdd")
    Call<ApiResponse> uploadRoutingPointInfo(@Header("token") String token, @Body RequestBody body);
    //上传位置
    @POST("updatePatrolRecord")
    Call<ApiResponse> uploadPoiotByService(@Header("token")String token, @Body UploadPointPatrol body);

    @POST("register/user")
    Call<MApi> applyInformation(@Body RequestBody body);
    @GET("noticeList")
    Call<ApiResponse> queryNewMessageNotice(@Header("token") String token, @Query("page") int page,
                                              @Query("read") int state);


    @POST("login")
    Call<ApiResponse> login(@Body RequestBody body);

    @GET("text?output=json&offset=10&page=1&key=0324d52a856294adda99f218c7a09ad1&extensions=all")
    Call<GaodeResponse> getAddressDetail(@Query("city") String city, @Query("keywords") String keywords);


    @GET("register/checkuser")
    Call<ApiResponse> checked(@Query("username") String username);

    @POST("register/user")
    @Multipart
    Call<ApiResponse<Token>> registerWork(@Part("username") RequestBody username, @Part("password") RequestBody password,
                                          @Part("name") RequestBody name, @Part("gender") RequestBody gender, @Part("birthday") RequestBody birthday,
                                          @Part("address") RequestBody address, @Part("number") RequestBody number,
                                          @Part("publisher") RequestBody publisher, @Part("validate") RequestBody validate,
                                          @Part("height") RequestBody height, @Part("graduate") RequestBody graduate,
                                          @Part("telephone") RequestBody telephone, @Part("living_address") RequestBody living_address,
                                          @Part("emergency_contact") RequestBody emergency_contact,
                                          @Part("industry") RequestBody industry, @Part("skill") RequestBody skill, @Part("channel") RequestBody channel,
                                          @PartMap Map<String, RequestBody> params);


    @Multipart
    @POST("register/user")
    Observable<ApiResponse> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("birthday") RequestBody birthday,
            @Part("address") RequestBody address,
            @Part("number") RequestBody number,
            @Part("publisher") RequestBody publisher,
            @Part("validate") RequestBody validate,
            @Part("height") RequestBody height,
            @Part("graduate") RequestBody graduate,
            @Part("telephone") RequestBody telephone,
            @Part("living_address") RequestBody livingAddress,
            @Part("emergency_contact") RequestBody emergencyContact,
            @Part("industry") RequestBody industry,
            @Part("skill") RequestBody skill,
            @Part("channel") RequestBody channel,
            @Part MultipartBody.Part idCardPositive,
            @Part MultipartBody.Part idCardNegative,
            @Part MultipartBody.Part idCardHeadImage);
}
