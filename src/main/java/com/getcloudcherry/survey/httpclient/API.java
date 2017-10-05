package com.getcloudcherry.survey.httpclient;


import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.LoginToken;
import com.getcloudcherry.survey.model.SurveyAnswers;
import com.getcloudcherry.survey.model.SurveyResponse;
import com.getcloudcherry.survey.model.SurveyToken;
import com.getcloudcherry.survey.model.ThrottleResponse;
import com.getcloudcherry.survey.model.ThrottlingLogicResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {

    @GET(APIHelper.GET_QUESTIONS)
    Call<SurveyResponse> getQuestions(@Path("token") String token, @Path("deviceId") String deviceId);

    @POST(APIHelper.POST_ANSWER_PARTIAL)
    Call<ResponseBody> postAnswerPartial(@Path("id") String id, @Path("complete") boolean complete, @Body ArrayList<Answer> response);

    @POST(APIHelper.POST_ANSWER_ALL)
    Call<ResponseBody> postAnswerAll(@Path("token") String token, @Body SurveyAnswers response);

    @POST(APIHelper.POST_CREATE_SURVEY_TOKEN)
    Call<SurveyToken> createSurveyToken(@Body SurveyToken response);

    @FormUrlEncoded
    @POST(APIHelper.POST_LOGIN_TOKEN)
    Call<LoginToken> login(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password);

    @GET(APIHelper.GET_SURVEY_THROTTLE_LOGIC)
    Call<List<ThrottlingLogicResponse>> getSurveyThrottlingLogic(@Path("location") String location);

    @POST(APIHelper.POST_THROTTLING)
    Call<List<ThrottleResponse>> checkThrottling(@Body ThrottlingLogicResponse.ThrottlingLogic logic);
}