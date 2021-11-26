package com.fbu.placemakerstrade.stc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import com.salesforce.marketingcloud.MCLogListener;
import com.salesforce.marketingcloud.MarketingCloudConfig;
import com.salesforce.marketingcloud.MarketingCloudSdk;
import com.salesforce.marketingcloud.messages.inbox.InboxMessageManager;
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions;
import com.salesforce.marketingcloud.notifications.NotificationManager;
import com.salesforce.marketingcloud.notifications.NotificationMessage;
import com.salesforce.marketingcloud.notifications.NotificationManager;

import java.util.Random;
import java.util.stream.DoubleStream;
import com.salesforce.marketingcloud.MarketingCloudConfig;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MarketingCloudSdk.init(this, MarketingCloudConfig.builder()
                .setApplicationId("0a34441b-4ee9-413d-83ef-116002b68d87")
                .setAccessToken("K1E1ZNb6JnwOfoEK9nVmxJwR")
                .setMarketingCloudServerUrl("https://mcl1k6vm1h5wwt1s6fdk3311zj14.device.marketingcloudapis.com")
                .setSenderId("852309005899")
                .setNotificationCustomizationOptions(
                        NotificationCustomizationOptions.create(R.drawable.common_full_open_on_phone))
                .build(this), initializationStatus -> Log.d("Initialization Status", initializationStatus.toString()));

        MarketingCloudSdk.requestSdk(marketingCloudSdk -> {
            marketingCloudSdk.getInboxMessageManager().refreshInbox(b -> {
                Log.v("Inbox Refresh: ", "" + b);
            });
        });
        MarketingCloudSdk.requestSdk(marketingCloudSdk -> {

            marketingCloudSdk.getNotificationManager().setShouldShowNotificationListener(new NotificationManager.ShouldShowNotificationListener() {
                @Override
                public boolean shouldShowNotification(@NonNull NotificationMessage notificationMessage) {
                    Log.d("Should notification", notificationMessage.toString());
                    if (notificationMessage.customKeys().get("NotifyType") != null && notificationMessage.customKeys().get("NotifyType").equals("STC")) {
                        Bundle bundle = new Bundle();
                        try {
                            JSONObject data = new JSONObject();
                            data.put("NotifyType", "STC");
                            bundle.putString("data", data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        bundle.putString("id", new Random(System.currentTimeMillis()).nextInt() + "");
                        bundle.putBoolean("ignoreInForeground", false);
                        bundle.putString("title", notificationMessage.title());
                        bundle.putString("message", notificationMessage.alert());
                        bundle.putString("smallIcon", "logo");

                        return false;
                    } else {
                        return true;
                    }
                }
            });

            Log.d("SDK State", marketingCloudSdk.getSdkState().toString());
        });
    }
}
