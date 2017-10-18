package cl.jsalgado.smartboxtest.device;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class Description {

    private User user;
    private Device device;
    private App app;

    public Description() {
        user = new User();
        device = new Device();
        app = new App();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void getDeviceInfo(Context context){
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> username = new LinkedList<>();
        for (Account account : accounts) {
            username.add(account.name);
        }
        String name = username.get(0);
        if(name == null){
            name = "myPhone";
        }

        device.setModel(Build.MANUFACTURER + " " + Build.MODEL);
        device.setDeviceId(Build.ID);
        device.setWidth(String.valueOf(context.getResources().getDisplayMetrics().widthPixels));
        device.setHeigth(String.valueOf(context.getResources().getDisplayMetrics().heightPixels));
        device.setPlatform("android");
        device.setVersion(Build.VERSION.RELEASE);
        device.setName(name);
        Profile profile = new Profile();
        profile.setLanguage(Locale.getDefault().getLanguage());
        user.setProfile(profile);
        app.setVersion("1.0.0");
    }

}