package com.mc.customizes.audioPlayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MediaService extends Service {
     private MediaBindService mediaBindService = new MediaBindService();
    public MediaService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mediaBindService;
    }

}
