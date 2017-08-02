package org.eenie.wgj.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AddArrangeClass;
import org.eenie.wgj.model.requset.AddKeyPersonalInformation;
import org.eenie.wgj.model.requset.AddProjectDay;
import org.eenie.wgj.model.requset.AttendanceAlertRequest;
import org.eenie.wgj.model.requset.BirthdayDetail;
import org.eenie.wgj.model.requset.BuildNewProject;
import org.eenie.wgj.model.requset.CaptchaChecked;
import org.eenie.wgj.model.requset.ClassMeetingRequest;
import org.eenie.wgj.model.requset.CompanyPersonalRequest;
import org.eenie.wgj.model.requset.CreataCompanyRequest;
import org.eenie.wgj.model.requset.GiveBirthday;
import org.eenie.wgj.model.requset.JoinCompany;
import org.eenie.wgj.model.requset.MLogin;
import org.eenie.wgj.model.requset.ModifyInfo;
import org.eenie.wgj.model.requset.PostWorkRequest;
import org.eenie.wgj.model.requset.ProjectTimeRequest;
import org.eenie.wgj.model.requset.ReportPost;
import org.eenie.wgj.model.requset.RoundPointRequest;
import org.eenie.wgj.model.requset.StartRoutingRecord;
import org.eenie.wgj.model.requset.UpdateRoundPoint;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.AttendanceDay;
import org.eenie.wgj.model.response.ShootList;
import org.eenie.wgj.model.response.meeting.AuditMeetingRequest;
import org.eenie.wgj.model.response.meeting.MeetingData;
import org.eenie.wgj.model.response.message.MessageStatus;
import org.eenie.wgj.model.response.project.QueryService;
import org.eenie.wgj.model.response.reportpost.QueryReportPostMonth;

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
    Single<ApiResponse>
    createCompany(@Header("token") String token, @Body CreataCompanyRequest creataCompanyRequest);

    //获取所有公司列表
    @GET("company/all")
    Single<ApiResponse> getCompanyList();

    //获取某一城市下的公司
    @GET("company/city")
    Single<ApiResponse> getCityCompanyList(@Query("city") String city);

    //加入公司
    @POST("company/join")
    Single<ApiResponse> joinCompany(@Header("token") String token, @Body JoinCompany joinCompany);

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

    //添加关键人物信息
    @POST("ownerAdd")
    Single<ApiResponse> addKeyPersonal(@Header("token") String token, @Body AddKeyPersonalInformation
            addKeyPersonalInformation);

    //获取交接班列表
    @GET("precautionList")
    Single<ApiResponse> getExchangeWorkList(@Header("token") String token, @Query("projectid")
            String projectId);

    //交接班删除
    @GET("precautionDelete")
    Single<ApiResponse> deleteExchangeWorkItem(@Header("token") String token, @Query("id") int id);

    //获取岗位培训列表
    @GET("jobtrainingList")
    Single<ApiResponse> getTrainingWorkList(@Header("token") String token, @Query("projectid")
            String projectId);

    //删除岗位培训
    @GET("jobtrainingDelete")
    Single<ApiResponse> deleteTrainingWorkItem(@Header("token") String token, @Query("id") int id);

    //班次列表
    @GET("serviceList")
    Single<ApiResponse> getClassWideList(@Header("token") String token, @Query("projectid")
            String projectId);

    //班次删除
    @GET("serviceDelete")
    Single<ApiResponse> deleteClassItem(@Header("token") String token, @Query("id") int id);

    //班次添加
    @POST("serviceAdd")
    Single<ApiResponse> addClassItem(@Header("token") String token, @Body ClassMeetingRequest request);

    //班次编辑
    @POST("serviceUpdate")
    Single<ApiResponse> editClassItem(@Header("token") String token, @Body ClassMeetingRequest
            classMeetingRequest);

    //考勤设置
    @GET("attendanceList")
    Single<ApiResponse> getAttendanceInformation(@Header("token") String token, @Query("projectid")
            String projectId);

    //考勤添加编辑
    @POST("attendanceUpdate")
    Single<ApiResponse> UpdateAttendanceDaySetting(@Header("token") String token, @Body AttendanceDay request);

    //添加接口
    @POST("attendanceAdd")
    Single<ApiResponse> addAttendanceDaySetting(@Header("token") String token, @Body AttendanceDay request);

    //岗位设置
    @GET("postList")
    Single<ApiResponse> getPostList(@Header("token") String token, @Query("projectid")
            String projectId);

    //岗位设置删除
    @GET("postDelete")
    Single<ApiResponse> deletePostItem(@Header("token") String token, @Query("id") int id);

    //岗位设置编辑
    @POST("postUpdate")
    Single<ApiResponse> editPostItem(@Header("token") String token, @Body PostWorkRequest request);

    //岗位添加
    @POST("postAdd")
    Single<ApiResponse> addPostItem(@Header("token") String token, @Body PostWorkRequest request);

    //报岗设置
    @GET("newspaperpostList")
    Single<ApiResponse> getReportPostList(@Header("token") String token, @Query("projectid")
            String projectId);

    //删除报岗设置
    @GET("newspaperpostDelete")
    Single<ApiResponse> deleteReportPostItem(@Header("token") String token, @Query("id") int id);

    //编辑报岗设置
    @POST("newspaperpostUpdate")
    Single<ApiResponse> editReportPostList(@Header("token") String token,
                                           @Body ReportPost reportPost);

    @POST("newspaperpostAdd")
    Single<ApiResponse> addReportPostList(@Header("token") String token,
                                          @Body ReportPost reportPost);

    //巡检点列表
    @GET("inspectionpointsList")
    Single<ApiResponse> getRoundPointList(@Header("token") String token, @Query("projectid")
            String projectId);

    //巡检点删除
    @GET("inspectionpointsDelete")
    Single<ApiResponse> deleteRoundPointItem(@Header("token") String token, @Query("id") int id);

    //更新巡检点
    @POST("inspectionpointsUpdate")
    Single<ApiResponse> updateRoundItem(@Header("token") String token, @Body
            UpdateRoundPoint updateRoundPoint);

    //巡检路线
    @GET("lineList")
    Single<ApiResponse> getLineList(@Header("token") String token, @Query("projectid")
            String projectId);

    //添加巡检路线
    @POST("lineAdd")
    @FormUrlEncoded
    Single<ApiResponse> addLineItem(@Header("token") String token, @Field("projectid")
            String projectId,
                                    @Field("name") String name, @Field("difference")
                                            String timeSpace);
    //编辑巡检路线

    @POST("lineUpdata")
    @FormUrlEncoded
    Single<ApiResponse> editLineItem(@Header("token") String token, @Field("projectid")
            String projectId,
                                     @Field("name") String name, @Field("difference")
                                             String timeSpace,
                                     @Field("id") int id);

    //删除巡检线路
    @GET("lineDelete")
    Single<ApiResponse> deleteLineItem(@Header("token") String token, @Query("id") int id);

    //巡检圈数
    @GET("inspectiondayList")
    Single<ApiResponse> getCycleNumber(@Header("token") String token, @Query("projectid")
            String projectId,
                                       @Query("lineid") int id);

    //巡检线路删除圈数
    @GET("inspectiondayDelete")
    Single<ApiResponse> deleteCycleNumber(@Header("token") String token, @Query("id") int id);

    //添加巡检点
    @POST("inspectiondayAdd")
    Single<ApiResponse> addRoundPoint(@Header("token") String token, @Body RoundPointRequest data);

    //编辑巡检点
    @POST("inspectiondayUpdate")
    Single<ApiResponse> updateRoundPoint(@Header("token") String token, @Body RoundPointRequest
            request);

    //获取项目工时每月
    @GET("personalHours/UserList")
    Single<ApiResponse> getProjectTime(@Header("token") String token, @Query("date") String date,
                                       @Query("projectid") String projectId);

    //获取每月每天设置的项目工时
    @GET("hoursList")
    Single<ApiResponse> getMonthDayTime(@Header("token") String token, @Query("date") String date,
                                        @Query("projectid") String projectId);

    //删除项目工时
    @GET("hoursDelete")
    Single<ApiResponse> deleteMonthDay(@Header("token") String token, @Query("id") int id);

    //项目工时添加，编辑
    @POST("hoursAdd")
    Single<ApiResponse> addMonthDay(@Header("token") String token, @Body ProjectTimeRequest request);

    //获取当月每个人工时
    @POST("personalHoursAdd")
    Single<ApiResponse> addPersonalProjectDay(@Header("token") String token, @Body
            AddProjectDay request);

    //获取当月工时
    @GET("personalHoursList")
    Single<ApiResponse> getMonthDay(@Header("token") String token, @Query("date") String date,
                                    @Query("projectid") String projectId);

    //排班设置列表
    @GET("schedulingList")
    Single<ApiResponse> getArrangeClassList(@Header("token") String token, @Query("date") String date,
                                            @Query("projectid") String projectId);

    //排班设置添加
    @POST("schedulingAdd")
    Single<ApiResponse> addArrangeClassItem(@Header("token") String token, @Body AddArrangeClass
            addArrangeClass);

    //工作秀列表
    @GET("workshowList")
    Single<ApiResponse> getWorkShowList(@Header("token") String token);

    //点赞工作秀
    @GET("workshowLike")
    Single<ApiResponse> thumbUp(@Header("token") String token, @Query("workshowid") int id);

    //随手拍列表
    @GET("readilyShootList")
    Single<ApiResponse> getTakePhoto(@Header("token") String token);

    //获取部门的人员
    @GET("readilyShootNumberlist")
    Single<ApiResponse> getShootNumber(@Header("token") String token);

    //获取考勤班次
    @GET("userSchedulingList")
    Single<ApiResponse> getAttendanceList(@Header("token") String token, @Query("date") String date);

    //获取当月每天的考勤情况
    @GET("attendance/statistics")
    Single<ApiResponse> getAttendanceDayOfMonth(@Header("token") String token, @Query("date")
            String date, @Query("userid") String userId);

    //获取当天的考勤地点和范围
    @GET("check/attendanceList")
    Single<ApiResponse> getAttendanceAddress(@Header("token") String token);

    //获取项目总工时

    @GET("project_total_hours_per_month")
    Single<ApiResponse> getProjectTotalTime(@Header("token") String token, @Query("projectid")
            String projectId, @Query("date") String date);

    @GET("project_person_lists")
    Single<ApiResponse> getCompanyPersonalListAll(@Header("token") String token,
                                                  @Query("companyid") String companyId,
                                                  @Query("projectid") String projectId);

    //添加项目下的人员
    @POST("project_person_add")
    Single<ApiResponse> addProjectPersonal(@Header("token") String token,
                                           @Body CompanyPersonalRequest request);

    //删除项目下的人员
    @POST("project_person_del")
    Single<ApiResponse> deleteProjectPersonal(@Header("token") String token,
                                              @Body CompanyPersonalRequest request);

    //获取签退的信息
    @GET("signOff")
    Single<ApiResponse> getSignOutInfor(@Header("token") String token);

    //获取项目下的每月考勤信息
    @GET("total/date")
    Single<ApiResponse> getProjectAttendanceMonth(@Header("token") String token, @Query("project_id")
            String projectId);

    //获取所有的年月考勤统计
    @GET("total/indexlist")
    Single<ApiResponse> getMonthSortItem(@Header("token") String token, @Query("projectid") String projectId,
                                         @Query("date") String date);

    //获取月度综合排名
    @GET("month/integrated")
    Single<ApiResponse> getMonthAllSort(@Header("token") String token, @Query("projectid") String projectId,
                                        @Query("date") String date);

    //月度团队排名
    @GET("month/rank")
    Single<ApiResponse> getMonthTeamSort(@Header("token") String token, @Query("date") String date,
                                         @Query("projectid") String projectId);

    //月度加油榜
    @GET("month/refue")
    Single<ApiResponse> getMonthFightingSort(@Header("token") String token, @Query("date") String date,
                                             @Query("projectid") String projectId);

    //考勤迟到
    @GET("month_late_list")
    Single<ApiResponse> getLateInformation(@Header("token") String token, @Query("date") String date,
                                           @Query("projectid") String projectId);

    //考勤旷工
    @GET("month/absenteeism")
    Single<ApiResponse> getAbsentInformation(@Header("token") String token, @Query("date") String date,
                                             @Query("projectid") String projectId);

    //考勤早退
    @GET("month_early_list")
    Single<ApiResponse> getEarlyInformation(@Header("token") String token, @Query("date") String date,
                                            @Query("projectid") String projectId);

    //    //考勤外出
//    @GET("month/goout")
//    Single<ApiResponse> getOutInformation(@Header("token") String token, @Query("date") String date,
//                                          @Query("projectid") String projectId);
//考勤外出
    @GET("att_month_goout_list")
    Single<ApiResponse> getOutInformation(@Header("token") String token, @Query("date") String date,
                                          @Query("projectid") String projectId);


    //考勤请假
    @GET("month/leave")
    Single<ApiResponse> getLeaveInformation(@Header("token") String token, @Query("date") String date,
                                            @Query("projectid") String projectId);

    //考勤借调
    @GET("month/seconded")
    Single<ApiResponse> getSecondInformation(@Header("token") String token, @Query("date") String date,
                                             @Query("projectid") String projectId);

    //考勤实习
    @GET("month/practice")
    Single<ApiResponse> getPracticeInformation(@Header("token") String token, @Query("date") String date,
                                               @Query("projectid") String projectId);

    //考勤新增人员
    @GET("month/newemployees")
    Single<ApiResponse> getNewEmployees(@Header("token") String token, @Query("date") String date,
                                        @Query("projectid") String projectId);

    //巡检线路
    @GET("inspection/lineList")
    Single<ApiResponse> getRoutingLineList(@Header("token") String token);

    //获取巡检线路的详情
    @GET("inspectionRate")
    Single<ApiResponse> getLineDetail(@Header("token") String token, @Query("lineid") String lineId);

    //获取巡检上报信息的记录
    @GET("warrantyList")
    Single<ApiResponse> getReportRoutingList(@Header("token") String token, @Query("type") int type);

    //获取巡检路线上的点位
    @GET("inspectionList")
    Single<ApiResponse> getRoutingByLine(@Header("token") String token, @Query("lineid") String lineId);

    //获取通知人员（巡检）
    @GET("management/userList")
    Single<ApiResponse> getNoticePeopleList(@Header("token") String token, @Query("projectid") String projectId);

    //开始巡检
    @POST("addPatrolRecord")
    Single<ApiResponse> startRoutingRecord(@Header("token") String token, @Body
            StartRoutingRecord request);

    //获取某段时间段的巡检详情
    @GET("inspection/statistics")
    Single<ApiResponse> getTimeRoutingRecordList(@Header("token") String token,
                                                 @Query("starttime") String startTime,
                                                 @Query("endtime") String endTime,
                                                 @Query("userid") String userId);

    //获取巡检轨迹点
    @GET("getPatrolRecords")
    Single<ApiResponse> getRoutingLine(@Header("token") String token, @Query("date") String date,
                                       @Query("inspectionday_id") String inspectiondayId,
                                       @Query("projectid") String projectId,
                                       @Query("user_id") String userId);

    //获取每月全部的巡检信息
    @GET("inspection/total")
    Single<ApiResponse> getRoutingInfoList(@Header("token") String token, @Query("date") String date,
                                           @Query("projectid") String projectId);

    //获取报岗数据
    @GET("signedtosign/postList")
    Single<ApiResponse> getReportLine(@Header("token") String token);

    //获取岗位下报岗信息
    @GET("signedtosign/newspaperpostList")
    Single<ApiResponse> getReportInfoByLine(@Header("token") String token,
                                            @Query("postsettingid") String posyId);

    //报岗统计列表

    @GET("signedtosign/statisticsIndex")
    Single<ApiResponse> getReportStatisticsList(@Header("token") String token,
                                                @Query("projectid") String projectId);

    //每月未报岗统计
    @GET("signedtosign/NotInfo")
    Single<ApiResponse> getReportMonthNoStatisticsList(@Header("token") String token,
                                                       @Query("projectid") String projectId,
                                                       @Query("date") String date);

    //实际每月报岗查询
    @POST("project_post_query")
    Single<ApiResponse> queryMonthReportPostList(@Header("token") String token,
                                                 @Body QueryReportPostMonth request);

    //培训
    @GET("training")
    Single<ApiResponse> getTrainInfoList(@Header("token") String token);

    //查询培训学习
    @GET("trainingInfo")
    Single<ApiResponse> lookTrainingInfoList(@Header("token") String token, @Query("page") int page,
                                             @Query("type") int type);

    //培训统计
    @GET("training/total")
    Single<ApiResponse> getTrainingStatisticList(@Header("token") String token,
                                                 @Query("projectid") String projectId);


    //考勤提醒
    @GET("remind/AttendanceList")
    Single<ApiResponse> getAttendanceAlert(@Header("token") String token);

    //打开或关闭考勤提醒
    @GET("remind/AttendanceOpen")
    Single<ApiResponse> openCloseAttendanceAlert(@Header("token") String token);

    //添加考勤时间
    @POST("remind/AttendanceAdd")
    Single<ApiResponse> addAttendanceTime(@Header("token") String token,
                                          @Body AttendanceAlertRequest request);

    //报岗提醒
    @GET("remind/PostList")
    Single<ApiResponse> getReportAlert(@Header("token") String token);

    //打开或关闭报岗
    @GET("remind/PostOpen")
    Single<ApiResponse> openClosePostAlert(@Header("token") String token);

    //添加报岗时间
    @POST("remind/PostAdd")
    Single<ApiResponse> addPostTime(@Header("token") String token,
                                    @Body AttendanceAlertRequest request);


    //巡检
    @GET("remind/InspectionList")
    Single<ApiResponse> getRoutingAlert(@Header("token") String token);

    //打开或关闭巡检
    @GET("remind/InspectionOpen")
    Single<ApiResponse> openCloseInspectionAlert(@Header("token") String token);

    //添加巡检时间
    @POST("remind/InspectionAdd")
    Single<ApiResponse> addInspectionTime(@Header("token") String token,
                                          @Body AttendanceAlertRequest request);


    //参会人员
    @GET("conferencePersonnel")
    Single<ApiResponse> getMeetingPeopleList(@Header("token") String token);

    //添加会议
    @POST("meetingAdd")
    Single<ApiResponse> addMeetingContent(@Header("token") String token, @Body MeetingData request);

    //会议室列表
    @GET("meeting_roomList")
    Single<ApiResponse> getMeetingClassList(@Header("token") String token, @Query("start")
            String startTime, @Query("end") String endTime);

    @POST("application_roomAdd")
    Single<ApiResponse> applyMeetingClass(@Header("token") String token, @Body MeetingData request);

    //获取部门
    @GET("departmentList")
    Single<ApiResponse> getPartList(@Header("token") String token);


    //会议申请反馈
    @GET("application_roomList")
    Single<ApiResponse> getMeetingFeedbackList(@Header("token") String token);

    //详情
    @GET("application_roomList/info")
    Single<ApiResponse> getMeetingDetailInfo(@Header("token") String token,
                                             @Query("applicationid") String applyId);

    //未开始会议
    @GET("meeting/notStart")
    Single<ApiResponse> getUnStartMeetingList(@Header("token") String token);

    //进行中
    @GET("meeting/start")
    Single<ApiResponse> getMeetingProgressList(@Header("token") String token);

    //结束会议
    @GET("meeting/end")
    Single<ApiResponse> getEndMeetingList(@Header("token") String token);

    @GET("meeting/info")
    Single<ApiResponse> getMeetingArrangeDetail(@Header("token") String token, @Query("id") String id);

    //会议签到
    @GET("meeting/checkin")
    Single<ApiResponse> checkInMeeting(@Header("token") String token, @Query("meetingid") String meetingId);
    //交接班模块 交接班列表

    @GET("handoverList")
    Single<ApiResponse> getExchangeList(@Header("token") String token);

    @GET("handoverListinfo")
    Single<ApiResponse> getExchangeWorkDetailById(@Header("token") String token,
                                                  @Query("precautionid") String id);

    @GET("handoverSubmitter")
    Single<ApiResponse> getExchangeWorkContact(@Header("token") String token);


    @POST("handoverAdd")
    Single<ApiResponse> addExchangeWorkItem(@Header("token") String token, @Body MeetingData data);

    //会议室审核
    @POST("audit_meeting_room_apply")
    Single<ApiResponse> auditMeeting(@Header("token") String token, @Body AuditMeetingRequest request);

    //查询常日班人数
    @POST("project_service_query")
    Single<ApiResponse> queryServicePeople(@Header("token") String token,
                                           @Body QueryService queryService);

    //    //新增员工
//    @GET("month/newemployees")
//    Single<ApiResponse>queryAddPeople(@Header("token")String token,@Query("date")String date,
//                                   @Query("projectid")String projectId);
//通知接口
    @GET("noticeList")
    Single<ApiResponse> queryNewMessageNotice(@Header("token") String token, @Query("page") int page,
                                              @Query("read") int state);

    //消息已读
    @POST("notice_hasread")
    Single<ApiResponse> changeMessageStatus(@Header("token") String token, @Body MessageStatus request);


    //根据项目id获取考勤统计得月份

    @GET("att_statistics_month_list")
    Single<ApiResponse> getAttendanceMonthByProjectId(@Header("token") String token,
                                                      @Query("projectid") String projectId);

    //根据项目id和月份获取当月劳模榜的排名
    @GET("att_statistics_rank_list")
    Single<ApiResponse> getAttendanceStatisticRankAllMonth(@Header("token") String token,
                                                           @Query("projectid") String projectId,
                                                           @Query("date") String date);


    //根据项目id月份获取当月的勤奋榜排名
    @GET("att_statistics_first_list")
    Single<ApiResponse> getAttendanceStatisticRankFirstMonth(@Header("token") String token,
                                                             @Query("projectid") String projectId,
                                                             @Query("date") String date);


    //根据项目id月份获取当月的加油榜排名

    @GET("att_statistics_refue_list")
    Single<ApiResponse> getAttendanceStatisticRankRefue(@Header("token") String token,
                                                        @Query("projectid") String projectId,
                                                        @Query("date") String date);

    //获取项目下当月的考勤统计数据
    @GET("att_statistics_index")
    Single<ApiResponse> getAttendanceStatisticTotalData(@Header("token") String token,
                                                        @Query("projectid") String projectId,
                                                        @Query("date") String date);

    //获取项目下当月离职人员
    @GET("att_month_resign_list")
    Single<ApiResponse> getLeaveOutPeopleList(@Header("token") String token,
                                              @Query("projectid") String projectId,
                                              @Query("date") String date);

    //获取项目下当月的旷工数据
    @GET("att_month_absent_list")
    Single<ApiResponse> getNewAbsolutePeopleList(@Header("token") String token,
                                                 @Query("projectid") String projectId,
                                                 @Query("date") String date);

    //获取项目下某个用户当月的考勤数据和异常数据

    @GET("att_statistics_by_user")
    Single<ApiResponse> getUserAttendanceStatisticData(@Header("token") String token,
                                                       @Query("userid") String userId,
                                                       @Query("projectid") String projectId,
                                                       @Query("date") String date);

    //我的交班
    @GET("handover_sent")
    Single<ApiResponse> getHandOverClass(@Header("token") String token);

    //我的接班
    @GET("handover_received")
    Single<ApiResponse> getReceiveClass(@Header("token") String token);


    //获取个人考勤情况
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
