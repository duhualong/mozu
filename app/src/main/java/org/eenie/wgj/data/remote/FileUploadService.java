package org.eenie.wgj.data.remote;

import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Token;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

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
    Call<ApiResponse<Token>> applyInformation( @Body RequestBody body);
}
