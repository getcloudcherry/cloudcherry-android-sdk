package com.getcloudcherry.survey.httpclient;


import com.getcloudcherry.survey.model.SurveyAnswers;
import com.getcloudcherry.survey.model.SurveyResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {

    @GET(APIHelper.GET_QUESTIONS)
    Call<SurveyResponse> getQuestions(@Path("token") String token, @Path("deviceId") String deviceId);

    @FormUrlEncoded
    @POST(APIHelper.POST_ANSWER_PARTIAL)
    Call<ResponseBody> postAnswerPartial(@Path("id") String id, @Path("complete") boolean complete, @Body String response);

    @POST(APIHelper.POST_ANSWER_ALL)
    Call<ResponseBody> postAnswerAll(@Path("token") String token, @Body SurveyAnswers response);

    @POST(APIHelper.POST_CREATE_SURVEY_TOKEN)
    Call<ResponseBody> createSurveyToken(@Body SurveyAnswers response);

}