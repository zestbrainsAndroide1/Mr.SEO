package com.zb.mrseo.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zb.moodlist.utility.Prefs1;
import com.zb.mrseo.utility.AppConstants;
import com.zb.mrseo.utility.Prefs2;


public class FcmTokenRegistrationService extends IntentService {

    public FcmTokenRegistrationService() {
        super("FcmTokenRegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        Prefs2.setValue(FcmTokenRegistrationService.this, AppConstants.PUSH_TOKEN,task.getResult().getToken());
                        Log.e("Push Token",task.getResult().getToken());

                    }
                });
    }

}