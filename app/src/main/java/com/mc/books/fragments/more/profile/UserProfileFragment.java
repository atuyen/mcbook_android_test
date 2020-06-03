package com.mc.books.fragments.more.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.button.ExtButton;
import com.bon.customview.datetime.ExtDayMonthYearDialogFragment;
import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageLoaderUtils;
import com.bon.image.ImageUtils;
import com.bon.permission.PermissionUtils;
import com.bon.sharepreferences.AppPreferences;
import com.bon.util.ToastUtils;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.edittexts.TextInputApp;
import com.mc.models.User;
import com.mc.models.more.City;
import com.mc.models.more.Country;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import rx.functions.Action1;

import static com.mc.utilities.Constant.KEY_USER;
import static com.mc.utilities.Constant.KEY_USER_INFO;

public class UserProfileFragment extends BaseMvpFragment<IUserProfileView, IUserProfilePresenter<IUserProfileView>> implements IUserProfileView {
    private static final String TAG = UserProfileFragment.class.getSimpleName();

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.ivChangeAvatar)
    ImageView ivChangeAvatar;
    @BindView(R.id.etName)
    TextInputApp etName;
    @BindView(R.id.etBirthDay)
    TextInputApp etBirthDay;
    @BindView(R.id.etPhone)
    TextInputApp etPhone;
    @BindView(R.id.etGender)
    TextInputApp etGender;
    @BindView(R.id.etEmail)
    TextInputApp etEmail;
    @BindView(R.id.etCity)
    TextInputApp etCity;
    @BindView(R.id.etDistrict)
    TextInputApp etDistrict;
    @BindView(R.id.etAddress)
    TextInputApp etAddress;
    @BindView(R.id.btnSave)
    ExtButton btnSave;
    File fileImage = null;
    private User user;
    private ArrayList<ExtKeyValuePair> cityKeyValuePair;
    private ArrayList<ExtKeyValuePair> stateKeyValuePair;
    private List<City> cityList;
    private boolean isUpdateAvatar;

    @NonNull
    @Override
    public IUserProfilePresenter<IUserProfileView> createPresenter() {
        return new UserProfilePresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        user = (User) getArguments().getSerializable(KEY_USER);
        isUpdateAvatar = false;
        presenter.getListCity();
        initData(user);
    }

    void initData(User user) {
        try {
            if (user.getFullname() != null && !user.getFullname().isEmpty()) {
                etName.setContent(user.getFullname());
            } else {
                etName.setContent(user.getFirstName());
            }
            etBirthDay.setContent(user.getBirthday());
            etPhone.setContent(user.getPhone());
            etEmail.setContent(user.getEmail());
            etEmail.setDisable();
            etAddress.setContent(user.getAddress());
            etGender.setContent(user.getGender());

            AppUtils.setImageGlide(getAppContext(), user.getAvatar(), R.drawable.ic_default_avatar, ivAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getResourceId() {
        return R.layout.user_profile_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.account_information;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        supportActionBar.show();
    }

    void requestPermissionCamera(Action1<Boolean> callback) {
        callback.call(PermissionUtils.requestPermission(this,
                PermissionUtils.REQUEST_CODE_PERMISSION, Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    @SuppressLint("DefaultLocale")
    @OnClick(R.id.ivChangeAvatar)
    void onClickChangeAvatar() {
        requestPermissionCamera(result -> {
            if (!result) {
                ExtKeyValuePairDialogFragment.newInstance().setExtKeyValuePairs(new ArrayList<ExtKeyValuePair>() {
                    {
                        add(new ExtKeyValuePair(String.format("%d", ImageUtils.CAMERA_REQUEST), getString(R.string.camera)));
                        add(new ExtKeyValuePair(String.format("%d", ImageUtils.REQUEST_PICK_CONTENT), getString(R.string.gallery)));
                    }
                }).setOnSelectedConsumer(keyValuePair -> {
                    if (keyValuePair.getKey().equalsIgnoreCase(String.format("%d", ImageUtils.CAMERA_REQUEST))) {
                        fileImage = ImageUtils.getImageUrlPng();
                        ImageUtils.captureCamera(UserProfileFragment.this, ImageUtils.getUriFromFile(mActivity, fileImage));
                    }

                    if (keyValuePair.getKey().equalsIgnoreCase(String.format("%d", ImageUtils.REQUEST_PICK_CONTENT))) {
                        ImageUtils.chooseImageFromGallery(UserProfileFragment.this, getString(R.string.select_value));
                    }
                }).show(getFragmentManager(), null);
            }
        });
    }

    @OnClick(R.id.etBirthDay)
    void onClickBirthDay() {
        ExtDayMonthYearDialogFragment.newInstance()
                .setMaxDate(Calendar.getInstance())
                .setMinDate(AppUtils.getMinCalendar())
                .setDefaultDate(Calendar.getInstance())
                .setConditionFunction(calendar -> Calendar.getInstance().getTimeInMillis() >= calendar.getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE_DISPLAY);
                    etBirthDay.setContent(dateFormat.format(calendar.getTime()));
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.etGender)
    void onClickGender() {
        ExtKeyValuePairDialogFragment.newInstance().setExtKeyValuePairs(new ArrayList<ExtKeyValuePair>() {
            {
                add(new ExtKeyValuePair(Constant.MALE, getString(R.string.male)));
                add(new ExtKeyValuePair(Constant.FEMALE, getString(R.string.female)));
                add(new ExtKeyValuePair(Constant.OTHER, getString(R.string.other_gender)));
            }
        }).setOnSelectedConsumer(keyValuePair -> {
            user.setGender(keyValuePair.getValue());
            etGender.setContent(keyValuePair.getValue());
        }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.btnSave)
    void onClickSave() {
        user.setAddress(etAddress.getContent());
        user.setBirthday(etBirthDay.getContent());
        user.setFullname(etName.getContent());
        user.setPhone(etPhone.getContent());
        if (!isUpdateAvatar) user.setAvatar(null);
        presenter.updateUser(user);
    }

    @SuppressLint("DefaultLocale")
    @OnClick(R.id.etCity)
    void onClickCity() {
        ExtKeyValuePairDialogFragment.newInstance().setExtKeyValuePairs(cityKeyValuePair).setOnSelectedConsumer(keyValuePair -> {
            user.setCity(String.format("%d", Integer.parseInt(keyValuePair.getKey())));
            etCity.setContent(keyValuePair.getValue());
            etDistrict.setContent("");
            user.setDistrict("");
            if (cityList.size() > 0) {
                List<Country> countries = StreamSupport.stream(cityList).filter(city -> city.getId() == Integer.parseInt(keyValuePair.getKey())).collect(Collectors.toList()).get(0).getCountryList();
                stateKeyValuePair = new ArrayList<>();
                for (Country country : countries) {
                    stateKeyValuePair.add(new ExtKeyValuePair(String.format("%d", country.getId()), country.getName()));
                }
            }
        }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.etDistrict)
    void onClickState() {
        ExtKeyValuePairDialogFragment.newInstance().setExtKeyValuePairs(stateKeyValuePair).setOnSelectedConsumer(keyValuePair -> {
            user.setDistrict("" + Integer.parseInt(keyValuePair.getKey()));
            etDistrict.setContent(keyValuePair.getValue());
        }).show(getFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onClickChangeAvatar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImageUtils.CAMERA_REQUEST && fileImage != null) {
                ImageLoaderUtils.displayImage(Uri.fromFile(fileImage).toString(), ivAvatar);
                presenter.uploadAvatar(Uri.fromFile(fileImage).toString(), getAppContext());
            }

            if (requestCode == ImageUtils.REQUEST_PICK_CONTENT) {
                String uriImg = ImageUtils.getUriImage(ImageFilePath.getPath(mActivity, data.getData()));
                if (uriImg != null) {
                    ImageLoaderUtils.displayImage(ImageUtils.getUriImage(ImageFilePath.getPath(mActivity, data.getData())), ivAvatar);
                    presenter.uploadAvatar(ImageUtils.getUriImage(ImageFilePath.getPath(mActivity, data.getData())), getAppContext());
                } else {
                    ToastUtils.showToast(getContext(), getString(R.string.error_img));
                }
            }
        }
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void getListCitySuccess(List<City> cityList) {
        this.cityList = cityList;
        cityKeyValuePair = new ArrayList<>();
        for (City city : cityList) {
            cityKeyValuePair.add(new ExtKeyValuePair(String.format("%d", city.getId()), city.getName()));
            if (!user.getCity().equals("") && city.getId() == Integer.parseInt(user.getCity())) {
                etCity.setContent(city.getName());
                stateKeyValuePair = new ArrayList<>();
                for (Country country : city.getCountryList()) {
                    stateKeyValuePair.add(new ExtKeyValuePair(String.format("%d", country.getId()), country.getName()));
                    if (!user.getDistrict().equals("") && country.getId() == Integer.parseInt(user.getDistrict())) {
                        etDistrict.setContent(country.getName());
                    }
                }
            }
        }
    }

    @Override
    public void onPhoneError() {
        etPhone.setError(getString(R.string.error_phonenumber));
    }

    @Override
    public void onNameEmpty() {
        etName.setError(getString(R.string.error_fullname));
    }

    @Override
    public void onPhoneEmpty() {
        etPhone.setError(getString(R.string.error_empty_phonenumber));
    }

    @Override
    public void upLoadSuccess(String uri) {
        isUpdateAvatar = true;
        user.setAvatar(uri);
    }

    @Override
    public void updateUserSuccess(User user) {
        ToastUtils.showToast(getAppContext(), getString(R.string.update_info_success));
        AppPreferences.getInstance(getAppContext()).putJson(KEY_USER_INFO, user);
        etPhone.setError("");
        etName.setError("");
    }
}
