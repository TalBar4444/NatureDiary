package com.myapps.growdiary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.myapps.growdiary.activities.SettingsActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.TextStyle;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MyUtils {

    public static void openHtmlTextDialog(Activity activity, String fileNameInAssets){
        String str = "";
        InputStream is = null;

        try{
            is = activity.getAssets().open(fileNameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        }catch (IOException e){
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        }else{
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str));
        }
        materialAlertDialogBuilder.setPositiveButton("Close", (dialogInterface, i) -> {
        });
        AlertDialog al = materialAlertDialogBuilder.show();
        TextView alertTextView = al.findViewById(android.R.id.message);
        alertTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void shareAppPopUp(Context context){
        String title = "Do you enjoy our app?";
        String message = "Share Natures Diary with your friends";
        String shareButton = "Share";
        String cancelButton = "No, Thanks";
        int icon = R.drawable.ic_share;
        openDialog(context, icon, title, message, shareButton, cancelButton, new Runnable() {
            @Override
            public void run() {
                try {
                    shareApp(context);
                } catch (Exception ex) {

                }
            }
        });
    }

    private static void shareApp(Context context){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String body = "https://github.com/TalBar4444";
        String subject = "Let me recommend you about this great app ";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT,body);
        context.startActivity(Intent.createChooser(sharingIntent,"share using"));
    }

    public static void rateAppPopup(Activity activity) {
        AppRate.with(activity)
                .setInstallDays(4) // dialog is launched 4 days after installation
                .setLaunchTimes(5) // dialog is launched 5 times
                .setRemindInterval(1) // dialog is launched 1 days after Remind me later BTN clicked
                .setTitle(R.string.rate_dialog_title)
                .setMessage(R.string.rate_dialog_message)
                .setTextLater(R.string.rate_dialog_neutral)
                .setTextNever(R.string.rate_dialog_cancel)
                .setTextRateNow(R.string.rate_dialog_ok)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(activity);
        AppRate.with(activity).clearAgreeShowDialog();
    }

    public static void newVersionPopup(Context context){
        String title = "What's new in Version 1.0.2";
        String message ="•  Enable reminders for various garden activities\n\n" +
                "•  A visual timeline tracking the plant's development";
        String updateButton = "UPDATE";
        String cancelButton = "No, Thanks";
        int icon = R.drawable.ic_new_version;
        openDialog(context, icon, title, message, updateButton, cancelButton, new Runnable() {
            @Override
            public void run() {
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getMyPackageName(context) )));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getMyPackageName(context) )));
                } catch (Exception ex) {

                }
            }
        });
    }

    public static void proLimitationPopup(Context context,Class<?> secondActivity){
        String title = "Wild Discovery Feature";
        String message ="This feature is available for subscribers only.";
        String upgradeButton = "Learn More";
        String cancelButton = "No, Thanks";
        int icon = R.drawable.ic_lock_outline;
        openDialog(context,icon, title, message, upgradeButton, cancelButton, new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(context,secondActivity);
                    intent.putExtra("activity",""+context.getClass().getSimpleName());
                    context.startActivity(intent);
                } catch (Exception ex) {

                }
            }
        });
    }

    private static void openDialog(final Context context
            , int icon
            , String title
            , String message
            , String positiveButton
            , String cancelButton
            , final Runnable positiveButtonAction){
        AlertDialog.Builder adb;
        try {
            adb = new AlertDialog.Builder(context, 0);
        } catch (Exception ex) {
            // for older android versions
            adb = new AlertDialog.Builder(context);
        }
        adb.setIcon(icon);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                positiveButtonAction.run(); // Execute the action provided for the positive button
            }
        });
        adb.setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        try {
            adb.show();
        } catch (Exception ex) {
            // activity closed
            ex.printStackTrace();
        }
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }
        return false;
    }

    public static void noInternetPopup(Context context){
        String title = "No Internet connection";
        String message ="Please check your internet connection and restart the app.";
        String positiveButton = "go to settings";
        String cancelButton = "";
        int icon = R.drawable.ic_network;
        openDialog(context,icon, title, message, positiveButton, cancelButton, new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    context.startActivity(intent);
                } catch (Exception ex) {

                }
            }
        });
    }


    private static String getMyPackageName(Context context) {
        return context.getPackageName();
    }
}
