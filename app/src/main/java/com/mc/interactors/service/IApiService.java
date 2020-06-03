package com.mc.interactors.service;

import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.ContentResponse;
import com.mc.models.MessageResponse;
import com.mc.models.Upload;
import com.mc.models.User;
import com.mc.models.gift.AddGift;
import com.mc.models.gift.CategoryGift;
import com.mc.models.gift.InfomationExam;
import com.mc.models.gift.Point;
import com.mc.models.home.AddBook;
import com.mc.models.home.BookProcess;
import com.mc.models.home.BookProcessResponse;
import com.mc.models.home.BookResponse;
import com.mc.models.home.BookStatistic;
import com.mc.models.home.BookStatisticResponse;
import com.mc.models.home.CategoryResponse;
import com.mc.models.home.Chapter;
import com.mc.models.home.DetailQuestion;
import com.mc.models.home.DetailTraining;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.LogLesson;
import com.mc.models.home.Question;
import com.mc.models.home.RelatedBook;
import com.mc.models.home.SendLog;
import com.mc.models.home.TrainingResponse;
import com.mc.models.more.BookLeadBoadResponse;
import com.mc.models.more.BookLeadBoadUpdate;
import com.mc.models.more.BookRankingResponse;
import com.mc.models.more.CategoryLeadBoadResponse;
import com.mc.models.more.City;
import com.mc.models.more.Config;
import com.mc.models.more.FAQ;
import com.mc.models.more.ListFriendResponse;
import com.mc.models.more.ListMemberResponse;
import com.mc.models.more.UserRankingResponse;
import com.mc.models.notification.DetailNotificationAdmin;
import com.mc.models.notification.Notification;
import com.mc.models.notification.NotificationLog;
import com.mc.models.notification.NotificationResponse;
import com.mc.models.notification.UnReadNotification;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IApiService {
    @GET("/json/get/bItCmtKbAO?indent=2")
    Observable<User> login();

    // book
    @GET("books/api/v1/books/auth")
    Observable<BaseResponse<ArrayResponse<CategoryResponse>>> getCategory();

    @GET("books/api/v1/books/auth")
    Observable<BaseResponse<ArrayResponse<CategoryResponse>>> getSearchBook(@Query("filter") String filter);

    @GET("books/api/v1/books/auth/{id}")
    Observable<BaseResponse<BookResponse>> getDetailBook(@Path("id") int bookId);

    @GET("books/api/v1/books/book-total-point")
    Observable<BookStatisticResponse<BookStatistic>> getBookStatistic();

    @GET("books/api/v1/books/auth-process")
    Observable<BookProcessResponse<BookProcess>> getBookProcess();

    @POST("books/api/v1/books/auth")
    @FormUrlEncoded()
    Observable<BaseResponse<AddBook>> createBook(@Field("code") String code);

    @DELETE("books/api/v1/books/auth/{id}")
    Observable<BaseResponse> deleteBook(@Path("id") int bookId);

    @GET("books/api/v1/books/{id}/related")
    Observable<BaseResponse<ArrayResponse<RelatedBook>>> getListRelatedBook(@Path("id") int bookId);

    @POST("books/api/v1/lessons/{id}/logs")
    Observable<BaseResponse<LogLesson>> sendLogLesson(@Path("id") int lessonId);

    @GET("books/api/v1/books/{id}/questions")
    Observable<BaseResponse<ArrayResponse<Question>>> getListQuestion(@Path("id") int bookId);

    @GET("books/api/v1/books/auth/{id}/chapters")
    Observable<BaseResponse<ArrayResponse<Chapter>>> getListChapter(@Path("id") int bookId);

    @GET("books/api/v1/books/auth/{id}/chapters")
    Observable<BaseResponse<ArrayResponse<Chapter>>> searchLesson(@Path("id") int bookId, @Query("lessonName") String lessonName, @Query("lessonCode") String lessonCode);

    @GET("books/api/v1/books/{bookId}/questions/{questionId}")
    Observable<BaseResponse<DetailQuestion>> getDetailQuestion(@Path("bookId") int bookId, @Path("questionId") int questionId, @Query("childId") int childId);

    @GET("cms/api/v1/post?filter=[{\"operator\":\"eq\",\"value\":\"faq\",\"property\":\"type\"}, {\"operator\":\"eq\",\"value\":\"true\",\"property\":\"active\"}, {\"operator\":\"eq\",\"value\":\"app\",\"property\":\"platform\"}]")
    Observable<BaseResponse<ArrayResponse<FAQ>>> getFAQ();

    @GET("cms/api/v1/post/get-post/gioi-thieu")
    Observable<BaseResponse<ContentResponse>> getPostInfo();

    @GET("cms/api/v1/post/get-post/phap-ly")
    Observable<BaseResponse<ContentResponse>> getPostPolicy();

    @PUT("books/api/v1/books/{id}/reset-questions")
    Observable<MessageResponse> resetQuestion(@Path("id") int bookId);

    @GET("cms/api/v1/settings/legal")
    Observable<BaseResponse<ContentResponse>> getLegacy();

    @GET("cms/api/v1/settings/intro")
    Observable<BaseResponse<ContentResponse>> getIntro();


    @GET("cms/api/v1/settings/commomConfig")
    Observable<BaseResponse<Config>> getConfig();
    /////////

    // gift
    @GET("promotions/api/v1/giftPromotions/myGift")
    Observable<BaseResponse<ArrayResponse<CategoryGift>>> getCategoryGift();

    @GET("books/api/v1/exams/{id}/overViews")
    Observable<BaseResponse<InfomationExam>> getInfomationExam(@Path("id") int bookId, @Query("giftId") int giftId, @Query("giftUserId") int giftUserId);

    @GET("promotions/api/v1/giftPromotions/myGift")
    Observable<BaseResponse<ArrayResponse<CategoryGift>>> searchCategoryGift(@Query("name") String name);

    @POST("promotions/api/v1/giftPromotions/useGiftCode")
    @FormUrlEncoded()
    Observable<BaseResponse<AddGift>> createGift(@Field("giftCode") String code);

    @POST("books/api/v1/questions/{id}/logs")
    @FormUrlEncoded()
    Observable<BaseResponse<SendLog>> sendLogQuestion(@Path("id") int questionId, @Field("answer") boolean answer, @Field("bookId") int bookId,
                                                      @Field("submitAnswer") String submitAnswer, @Field("type") String type, @Query("childId") int childId);

    @DELETE("promotions/api/v1/giftUsers/{id}")
    Observable<BaseResponse<SendLog>> deleteGift(@Path("id") int giftId);


    @GET("notifications/api/v1/notifications/myNoti")
    Observable<BaseResponse<NotificationResponse>> getNotificationList(@QueryMap Map<String, String> maps);

    @POST("notifications/api/v1/deviceToken")
    @FormUrlEncoded()
    Observable<String> pushDeviceId(@Field("deviceId") String deviceId);

    @POST("notifications/api/v1/deviceToken/clearDevice")
    @FormUrlEncoded()
    Observable<String> clearDeviceId(@Field("deviceId") String deviceId);

    @GET("notifications/api/v1/notiManuals/{id}")
    Observable<BaseResponse<DetailNotificationAdmin>> getDetailNotification(@Path("id") int id);

    @PUT("notifications/api/v1/notifications/updateLog")
    Observable<BaseResponse<NotificationLog>> updateLogNoti();

    @PUT("notifications/api/v1/notifications/{id}")
    @FormUrlEncoded()
    Observable<BaseResponse<UnReadNotification>> unReadNoti(@Path("id") int idNoti, @FieldMap Map<String, Boolean> fields);

    @PUT("notifications/api/v1/notifications/resetBaggy")
    Observable<String> resetBadge();

    /////////

    //user
    @GET("users/api/v1/users/{id}")
    Observable<BaseResponse<User>> getDetailUser(@Path("id") String userId);

    @GET("users/api/v1/users/city")
    Observable<BaseResponse<ArrayResponse<City>>> getListCity();

    @PUT("users/api/v1/users/{id}")
    @FormUrlEncoded()
    Observable<BaseResponse<User>> updateUser(@Path("id") String userId, @FieldMap Map<String, String> fields);

    @POST("media/api/v1/upload/{type}")
    @Multipart
    Observable<BaseResponse<Upload>> uploadFile(@Path("type") String type, @Part MultipartBody.Part file);
    ///

    // SSO logout
    @POST()
    @FormUrlEncoded()
    Observable<String> logout(@Url String url, @Field("refresh_token") String refreshToken, @Field("client_id") String clientId);

    @POST()
    @FormUrlEncoded()
    Observable<String> getKeyCloarkInfo(@Url String url, @Field("grant_type") String grantType, @Field("code") String code,
                                        @Field("client_id") String clientId, @Field("redirect_uri") String redirectUri);
    //////////////

    //lead broad
    @GET("books/api/v1/leaderboard/books-ranking")
    Observable<BaseResponse<BookRankingResponse>> getBookLeadBroad(@QueryMap Map<String, String> querys);

    @GET("books/api/v1/leaderboard/users-ranking")
    Observable<BaseResponse<UserRankingResponse>> getMemberLeadBroad(@QueryMap Map<String, String> querys);

    @GET("books/api/v1/friendList/member-list")
    Observable<BaseResponse<ListMemberResponse>> getListMember(@QueryMap Map<String, String> maps);

    @POST("books/api/v1/friendList/remove-friend")
    @FormUrlEncoded()
    Observable<String> unfollowFriend(@Field("userId") String userId, @Field("userFriendId") String userFriendId);

    @POST("books/api/v1/friendList")
    @FormUrlEncoded()
    Observable<String> followFriend(@Field("userId") String userId, @Field("userFriendId") String userFriendId);

    @GET("books/api/v1/friendList/my-friend-list")
    Observable<BaseResponse<ListFriendResponse>> getMyFriendList(@QueryMap Map<String, String> maps);

    @GET("books/api/v1/leaderboard/list-book-min")
    Observable<BaseResponse<BookLeadBoadResponse>> getListBookLeadBoad(@Query("start") String start, @Query("limit") String limit);

    @GET("books/api/v1/leaderboard/list-book-min")
    Observable<BaseResponse<BookLeadBoadResponse>> searchBookFillter(@Query("start") String start, @Query("limit") String limit, @Query("filter") String fillter);

    @GET("categories/api/v1/categories")
    Observable<BaseResponse<CategoryLeadBoadResponse>> searchCategoryFillter(@Query("filter") String fillter);

    @GET("books/api/v1/leaderboard/last-update")
    Observable<BaseResponse<BookLeadBoadUpdate>> getBookLeadBoadUpdate(@Query("type") String type);

    @GET("categories/api/v1/categories")
    Observable<BaseResponse<CategoryLeadBoadResponse>> getListCategoryLeadBoad(@Query("filter") String filter);

//    @GET("books/api/v1/friendList/member-list")
//    Observable<BaseResponse<ListMemberResponse>> searchMemberFillter(@Query("value") String value);

    @GET("books/api/v1/exams/{id}/questions")
    Observable<BaseResponse<ArrayResponse<DetailQuestion>>> getListQuestionExam(@Path("id") int id, @Query("giftId") int giftId);

    @POST("books/api/v1/exams/{id}/logs")
    @FormUrlEncoded()
    Observable<BaseResponse<Point>> sendLogExam(@Path("id") int id, @Field("isSubmit") String data, @Query("giftId") int giftId);

    @GET("books/api/v1/trainingChapters")
    Observable<BaseResponse<ArrayResponse<TrainingResponse>>> getListTraining(@QueryMap Map<String, String> maps);

    @GET("books/api/v1/training/{id}")
    Observable<BaseResponse<DetailTraining>> getDetailTraining(@Path("id") int id, @Query("type") String type);

    @POST("books/api/v1/training/sendLog/{id}")
    @FormUrlEncoded()
    Observable<BaseResponse<ItemTrainingResponse>> sendLogTraining(@Path("id") int id, @FieldMap Map<String, Boolean> maps);

    @POST("/kong/users/api/v1/users/change-password")
    @FormUrlEncoded()
    Observable<BaseResponse<Object>> changePass(@Field("password") String password);
}
