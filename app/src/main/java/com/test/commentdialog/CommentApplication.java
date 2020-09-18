package com.test.commentdialog;

import android.app.Application;

/*** @author ganhuanhui
 * Time:2019/12/17 0017
 * Description：
 */
public class CommentApplication extends Application {

    public static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
