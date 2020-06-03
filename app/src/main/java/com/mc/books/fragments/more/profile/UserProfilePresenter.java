package com.mc.books.fragments.more.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.events.SignInEvent;
import com.mc.models.ArrayResponse;
import com.mc.models.BaseResponse;
import com.mc.models.Upload;
import com.mc.models.User;
import com.mc.models.more.City;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mc.utilities.Constant.AVATAR_SIZE;
import static com.mc.utilities.Constant.KEY_TYPE_UPLOAD;

public class UserProfilePresenter<V extends IUserProfileView> extends BaseDataPresenter<V> implements IUserProfilePresenter<V> {
    protected UserProfilePresenter(AppComponent appComponent) {
        super(appComponent);
        bus.subscribe(this, SignInEvent.class, signInEvent -> {
        });
    }

    @Override
    public void getListCity() {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.getListCity().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseResponse<ArrayResponse<City>>>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                            Log.e("getListCity err", e.toString());
                        }

                        @Override
                        public void onNext(BaseResponse<ArrayResponse<City>> arrayResponseBaseResponse) {
                            v.getListCitySuccess(arrayResponseBaseResponse.getData().getRows());
                        }
                    });
        });
    }

    @Override
    public void updateUser(User user) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            if (user.getFullname().isEmpty()) {
                v.onNameEmpty();
                return;
            }
            if (user.getPhone().isEmpty()) {
                v.onPhoneEmpty();
                return;
            } else if (user.getPhone().length() < 10 || user.getPhone().length() > 11) {
                v.onPhoneError();
                return;
            }

            v.onShowLoading(true);

            Map<String, String> userMaps = new HashMap<>();
            userMaps.put("id", user.getId());
            userMaps.put("fullname", user.getFullname());
            userMaps.put("enabled", user.getEnabled() + "");
            userMaps.put("expertise", user.getExpertise());
            userMaps.put("description", user.getDescription());
            userMaps.put("phone", user.getPhone());
            userMaps.put("job", user.getJob());
            userMaps.put("address", user.getAddress());
            userMaps.put("cv", user.getCv());
            userMaps.put("city", user.getCity());
            userMaps.put("district", user.getDistrict());
            userMaps.put("gender", user.getGender());
            userMaps.put("birthday", user.getBirthday());

            if (user.getAvatar() != null) {
                userMaps.put("avatar", user.getAvatar());
            }

            apiService.updateUser(user.getId(), userMaps).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<BaseResponse<User>>() {
                        @Override
                        public void onCompleted() {
                            v.onShowLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            v.onShowLoading(false);
                            Log.e("updateUser err", e.toString());
                        }

                        @Override
                        public void onNext(BaseResponse<User> userBaseResponse) {
                            v.updateUserSuccess(userBaseResponse.getData());
                        }
                    });
        });

    }

    @Override
    public void uploadAvatar(String uri, Context context) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            Bitmap bitmap = AppUtils.resizeBitmap(Uri.parse(uri).getPath(), AVATAR_SIZE, AVATAR_SIZE);
            File mFileAvatar = AppUtils.persistImage(bitmap, "UPLOAD_AVATAR", context);

            RequestBody requestFile = RequestBody.create(MediaType.parse(Constant.KEY_TYPE_BODY_IMG_REQUEST), mFileAvatar);
            MultipartBody.Part body = MultipartBody.Part.createFormData(Constant.KEY_FILE, mFileAvatar.getName(), requestFile);
            apiService.uploadFile(KEY_TYPE_UPLOAD, body).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<Upload>>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("uploadAvatar err", e.toString());
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(BaseResponse<Upload> uploadBaseResponse) {
                    Log.e("avatar: ", uploadBaseResponse.getData().getUri());
                    v.upLoadSuccess(uploadBaseResponse.getData().getPath());
                }
            });
        });

    }
}
