package com.pedromassango.programmers.extras;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.mvp.main.activity.MainActivity;
import com.pedromassango.programmers.ui.dialogs.ProgressFragmentDialog;

import java.io.File;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

public class IntentUtils {


    private static ProgressFragmentDialog progressFragmentDialog;

    public static void showProgressFragmentDialog(Activity activity, boolean show) {
        FragmentManager fm = ((AppCompatActivity) activity).getSupportFragmentManager();

        if (show && progressFragmentDialog == null) {
            progressFragmentDialog = new ProgressFragmentDialog();
            progressFragmentDialog.show(fm, null);
            return;
        }

        if (show) {
            progressFragmentDialog.show(fm, null);
            return;
        }

        progressFragmentDialog.dismiss();
    }

    public static void startAppOnFacebookIntent(Context context) {

        Intent iFacebook;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            iFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/pedromassango.programmers"));
            showLog("FACEBOOK SUCESS");

        } catch (Exception e) {
            iFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pedromassango.programmers/"));
            showLog("FACEBOOK THROW");
        }

        context.startActivity(iFacebook);
    }

    public static void startBrowserIntent(Context context, String url) {

        Intent iBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(iBrowser, context.getString(R.string.open_with)));
    }

    public static void startActivity(Context context, Intent intent) {

        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> activityClass) {

        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public static void startActivityCleaningTask(Context context, Class<? extends Activity> activityClass) {

        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void startActivityCleaningTask(Context context, Bundle data, Class<? extends Activity> activityClass) {

        Intent intent = new Intent(context, activityClass);
        intent.putExtras(data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Bundle data, Class<? extends Activity> activityClass) {

        Intent intent = new Intent(context, activityClass);
        intent.putExtras(data);
        context.startActivity(intent);
    }

    public static void startService(Context context, Intent intentService) {

        context.startService(intentService);
    }

    public static void stopService(Context context, Intent intentService) {

        context.stopService(intentService);
    }

    public static void startActivityForResult(Activity context, Intent intent, int requestCode) {

        context.startActivityForResult(intent, requestCode);
    }

    public static Intent getIntent(Context context, Class<? extends Activity> activityClass) {
        return new Intent(context, activityClass);
    }

    public static void startShareApp(Activity activity) {
        try {
            String packageName = activity.getPackageName();
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(packageName, 0);
            String dir = packageInfo.applicationInfo.sourceDir;
            File tempFile = new File(dir);

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setDataAndType(Uri.fromFile(tempFile), "application/vnd.android.package-archive");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));

            // Testing...
            //Only use bluetooth to share the app
            i.setPackage("com.android.bluetooth");
            activity.startActivity(i);
        } catch (Exception e) {
            showLog("SHAREAPP: ", e.getMessage());
        }
    }

    public static void showFragment(Activity activity, Bundle bundle, AppCompatDialogFragment fragmentClass) {
        FragmentTransaction ft = ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();
        fragmentClass.setArguments(bundle);
        fragmentClass.show(ft, null);
    }

    public static void showFragment(Activity activity, AppCompatDialogFragment fragmentClass) {
        FragmentTransaction ft = ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();
        fragmentClass.show(ft, null);
    }

    public static void startSendEmailIntent(Context context, String email) {
        Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        Intent choserIntent = Intent.createChooser(contactIntent, context.getString(R.string.send_with));
        context.startActivity(choserIntent);
    }

    public static void startPlaystoreAppPage(MainActivity context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
