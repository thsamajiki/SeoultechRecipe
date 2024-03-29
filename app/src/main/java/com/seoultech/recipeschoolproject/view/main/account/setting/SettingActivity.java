package com.seoultech.recipeschoolproject.view.main.account.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.seoultech.recipeschoolproject.R;
import com.seoultech.recipeschoolproject.authentication.FirebaseAuthentication;
import com.seoultech.recipeschoolproject.databinding.ActivitySettingBinding;
import com.seoultech.recipeschoolproject.listener.OnCompleteListener;
import com.seoultech.recipeschoolproject.listener.Response;
import com.seoultech.recipeschoolproject.view.login.LoginActivity;
import com.seoultech.recipeschoolproject.view.main.account.setting.notice.NoticeListActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<Void>  {

    private ActivitySettingBinding binding;
    private final FirebaseAuthentication firebaseAuthentication = FirebaseAuthentication.getInstance();
    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setOnClickListener();
//        readyPlayStoreReview();
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.layoutItemNotice.setOnClickListener(this);
        binding.layoutItemFont.setOnClickListener(this);
        binding.layoutItemDeleteCache.setOnClickListener(this);
        binding.layoutItemInquiry.setOnClickListener(this);
        binding.layoutItemReview.setOnClickListener(this);
        binding.layoutItemOpenSource.setOnClickListener(this);
        binding.layoutItemDropOut.setOnClickListener(this);

        firebaseAuthentication.setOnCompleteListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_item_notice:
                onNoticeListItemLayoutClick();
                break;
            case R.id.layout_item_font:
                openFontPopUp();
                break;
            case R.id.layout_item_delete_cache:
                openDeleteCachePopUp();
                break;
            case R.id.layout_item_inquiry:
                openInquiryPopUp();
                break;
            case R.id.layout_item_review:
//                launchReviewDialog(reviewManager, reviewInfo);
                break;
            case R.id.layout_item_open_source:
                openOpenSource();
                break;
            case R.id.layout_item_drop_out:
                openDropOutDialog();
                break;
        }
    }

    private void onNoticeListItemLayoutClick() {
        Intent intent = new Intent(this, NoticeListActivity.class);
        startActivity(intent);
    }

    private void openFontPopUp() {
        // 선택 가능한 폰트들이 나오는 창이 아래에서 위로 나오게 하기
    }

    private void openDeleteCachePopUp() {
        String deleteCachePopUpMessage = "캐시 데이터 삭제 완료";
        String negativeText = "닫기";

        new MaterialAlertDialogBuilder(this).setMessage(deleteCachePopUpMessage)
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void openInquiryPopUp() {
        String title = "문의하기";
        String message = "chs8275@gmail.com으로\n문의 부탁드립니다 :)";
        String positiveText = "확인";

        new MaterialAlertDialogBuilder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

//    private void readyPlayStoreReview() {
//        // 플레이스토어에 있는 리뷰 작성 페이지로 바로 이동하도록 링크를 만들기
//
//        //reviewManager = ReviewManagerFactory.create(this);
//        reviewManager = new FakeReviewManager(this);
//        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
//        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
//            @Override
//            public void onComplete(boolean isSuccess, Response<ReviewInfo> response) {
//                if (isSuccess) {
//                    // We can get the ReviewInfo object
//                    reviewInfo = response.getData();
//                    reviewManager.launchReviewFlow(SettingActivity.this, reviewInfo)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//
//                                }
//                            });
//                }
//    }
//
//    private void launchReviewDialog(ReviewManager reviewManager, ReviewInfo reviewInfo) {
//        if (reviewInfo == null) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=my packagename "));
//            startActivity(intent);
//        }
//        Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
//        flow.addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
//    }

    private void openOpenSource() {
        OpenSourceLicenseDialog openSourceLicenseDialog = new OpenSourceLicenseDialog(this);
        openSourceLicenseDialog.getOpenSourceLicenseDialog();
    }

    private void openDropOutDialog() {
        MaterialAlertDialogBuilder openDropOutAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        String openDropOutTitle = "탈퇴";
        String openDropOutMessage = "정말로 탈퇴하시겠습니까?";
        String positiveText = "예";
        String negativeText = "아니오";

        new MaterialAlertDialogBuilder(this).setTitle(openDropOutTitle)
                .setMessage(openDropOutMessage)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dropOut();
                    }
                })
                .setNegativeButton(negativeText, null)
                .create()
                .show();
    }

    private void dropOut() {
        firebaseAuthentication.dropOut(this);
    }

    @Override
    public void onComplete(boolean isSuccess, Response<Void> response) {
        if(isSuccess) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        } else {
            Log.e("SettingActivity", "onComplete: Failed");
            Toast.makeText(this, "탈퇴에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}