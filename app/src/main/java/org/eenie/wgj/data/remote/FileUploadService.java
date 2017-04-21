package org.eenie.wgj.data.remote;

import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Token;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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


    @POST("recog.do")
    Call<Api> upLoad(@Body RequestBody Body);

    @POST("register/user")
    Call<ApiResponse> applyInformation(@Header("token")String token,@Body RequestBody body);



    @POST("register/user")
    @Multipart
    Call<ApiResponse<Token>> registerWork(@Part("username")RequestBody  username,@Part("password")RequestBody  password,
                                          @Part("name")RequestBody  name,@Part("gender")RequestBody  gender,@Part("birthday")RequestBody  birthday,
                                          @Part("address")RequestBody  address,@Part("number")RequestBody  number,
                                          @Part("publisher")RequestBody  publisher,@Part("validate")RequestBody  validate,
                                          @Part("height")RequestBody  height,@Part("graduate")RequestBody  graduate,
                                          @Part("telephone")RequestBody  telephone,@Part("living_address")RequestBody  living_address,
                                          @Part("emergency_contact")RequestBody  emergency_contact,
                                          @Part("industry")RequestBody  industry,@Part("skill")RequestBody  skill,@Part("channel")RequestBody  channel,
                                          @PartMap Map<String, RequestBody> params);



    @Multipart
    @POST("register/user")
    Observable<ApiResponse<Token>> register(
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