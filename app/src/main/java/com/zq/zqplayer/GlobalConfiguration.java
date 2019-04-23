package com.zq.zqplayer;

import android.content.Context;

import com.ziq.base.baserx.dagger.module.ConfigModule;
import com.ziq.base.baserx.dagger.module.GlobalConfigModule;
import com.zq.zqplayer.common.Constants;

import androidx.annotation.NonNull;

public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyCustomConfig(@NonNull Context context, @NonNull GlobalConfigModule.Builder builder) {
        builder.setBaseurl(Constants.baseUrl);
    }
}
