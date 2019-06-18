//package com.kevinas.smkncibione;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.AppCompatTextView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.ImageButton;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class MainCore extends AppCompatActivity {
//
//    /***temp var***/
//    static int sudah0 = 0, sudah1 = 0;
//    /***temp var***/
//
//    static String Info = "Loading...";
//
//    public static Context context;
//
//    static String version = "1.1";
//    static int netBufferLimit = 100000000;
//
//    static ArrayList<Integer> layoutlist = new ArrayList<>();
//    static ArrayList<Integer> contentlist = new ArrayList<>();
//    static ArrayList<ArrayList<Integer>> fragmentlist = new ArrayList<>();
//    static ArrayList<Intent> intentlist = new ArrayList<>();
//
//    static String nik;
//    static String pass;
//
//    protected static HashMap<String, String> allfiles = new HashMap<String, String>();
//    protected static HashMap<String, String> statusfiles = new HashMap<String, String>();
//
//    static LoginActivity login;
//    static Button try_again;
//    static String auth = "Error33";
//
//    public static Tools tools;
//
//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
//
//    public void plusFragment(int x) {
//        fragmentlist.get(fragmentlist.size() - 1).add(x);
//    }
//
//    public int lastFragment() {
//        ArrayList<Integer> a = fragmentlist.get(fragmentlist.size() - 1);
//        return a.get(a.size() - 1);
//    }
//
//    public int lastContent() {
//        return contentlist.get(contentlist.size() - 1);
//    }
//
//    public int lastLayout() {
//        return layoutlist.get(layoutlist.size() - 1);
//    }
//
//    public void registerFile(String nick, String file, String status) {
//        allfiles.put(nick, file);
//        statusfiles.put(nick, status);
//    }
//
//    public void registerFile(String nick, String file) {
//        registerFile(nick, file, "0");
//    }
//
//    public String[] getFileRegistered(String nick) {
//        String to_return[] = new String[2];
//        to_return[0] = allfiles.get(nick);
//        to_return[1] = statusfiles.get(nick);
//        return to_return;
//    }
//
//    @Override
//    public void startActivity(Intent intent, Bundle bund) {
//        fragmentlist.add(new ArrayList<Integer>());
//        intentlist.add(intent);
//        super.startActivity(intent, bund);
//    }
//
//    @Override
//    public void startActivity(Intent i) {
//        fragmentlist.add(new ArrayList<Integer>());
//        intentlist.add(i);
//        super.startActivity(i);
//    }
//
//    public Intent lastIntent() {
//        return intentlist.get(intentlist.size() - 1);
//    }
//
//    public void addIntent(Context a, Class b) {
//        intentlist.add(new Intent(a, b));
//    }
//
//    public void addIntent(Class b) {
//        intentlist.add(new Intent(getApplicationContext(), b));
//    }
//
//    protected byte[] downloadData(String request, int buff, String... detail) {
//        request = nik + " " + pass + " " + request;
//        for (int i = 0; i < detail.length; i++) {
//            request += " " + detail[i];
//        }
//        byte[] to_return;
//        if (buff!=0) {
//            to_return = tools.send(request, buff);
//        }else{
//            to_return = tools.send(request).getBytes();
//        }
//        return to_return;
//    }
//    protected byte[] downloadData(String req, String ... detail)
//    {
//        return downloadData(req, 0, detail);
//    }
//    void setNoConnections(int alpha) {
//        setLayout(R.layout.disconnected);
//        Button yay = (Button) findViewById(R.id.try_again);
//        yay.setAlpha(alpha);
//    }
//
//    void setNoConnection(int again) {
//        this.addFragment(R.layout.disconnected);
//        Button yay = (Button) findViewById(R.id.try_again);
//        yay.setAlpha(0);
//    }
//
//    public void softRestart() {
//        Intent restart = new Intent(getBaseContext(), MainActivity.class);
//        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        restart.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        restart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(restart);
//    }
//
//    public void restart(Class cl) {
//        //System.exit(0);
//        Intent myAct = new Intent(getApplicationContext(), cl);
//        myAct.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        myAct.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        myAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        myAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        int x = 123456;
//        PendingIntent pending = PendingIntent.getActivity(getBaseContext(), x, myAct, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager) MainCore.this.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), pending);
//        //System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }
//    public void restart(){restart(MainActivity.class);
//    }
//
//    void AnotherError(String Error) {
//        setContentView(R.layout.another_error_portrait);
//        AppCompatTextView subheader = findViewById(R.id.error_subheader);
//        subheader.setText(subheader.getText() + Error);
//        Button yay = findViewById(R.id.try_again);
//        yay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                restart();
//            }
//        });
//    }
//
//    public void logout() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        final int before = lastFragment();
//        drawer.closeDrawer(GravityCompat.START);
//        setFragment(R.layout.confirmation);
//        ImageButton yes = findViewById(R.id.confirm_image_yes);
//        ImageButton no = findViewById(R.id.confirm_image_no);
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tools.delete(allfiles.get("loginfile0"));
//                tools.delete(allfiles.get("loginfile1"));
//                tools.delete(allfiles.get("profilepict"));
//                tools.writeFile(allfiles.get("loginfile0"), "Error");
//                tools.writeFile(allfiles.get("loginfile1"), "Error");
//                try {
//                    android.os.Process.killProcess(android.os.Process.myPid());
//                }catch(Exception err){android.os.Process.killProcess(android.os.Process.myPid());}
//            }
//        });
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(lastIntent());
//            }
//        });
//    }
//
//    void Debug(String Error) {
//        setFragment(R.layout.debugging);
//        AppCompatTextView subheader = findViewById(R.id.error_subheader);
//        subheader.setText(Error);
//        Button yay = findViewById(R.id.try_again);
//        yay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//    }
//
//    public void setFragment(int fragment, boolean anim) {
//        ViewGroup temp0 = findViewById(lastContent());
//        if (anim) {
//            Animation huff = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
//            huff.setDuration(750);
//            temp0.startAnimation(huff);
//        }
//        temp0.removeAllViews();
//        View now = getLayoutInflater().inflate(fragment, temp0, false);
//        temp0.addView(now);
//        layoutlist.add(lastLayout());
//        contentlist.add(lastContent());
//        plusFragment(fragment);
//    }
//
//    public void setFragment(int fragment) {setFragment(fragment, true);}
//    public void setNotAvailble() {
//        setFragment(R.layout.under_construction);
//        //setLayout(lastLayout(), lastContent(), R.layout.under_construction);
//    }
//
//    public void addFragment(int fragment) {
//        ViewGroup temp0 = findViewById(lastContent());
//        View now = getLayoutInflater().inflate(fragment, temp0, false);
//        temp0.addView(now);
//        layoutlist.add(lastLayout());
//        contentlist.add(lastContent());
//        plusFragment(fragment);
//    }
//
//    public void setLayout(int layout) {
//        setContentView(layout);
//        layoutlist.add(layout);
//        contentlist.add(this.lastContent());
//        plusFragment(0);
//    }
//    public void setLayout(int layout, int content, int fragment, int animationOn) {
//        Animation oke = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
//        oke.setDuration(500);
//        setContentView(layout);
//        ViewGroup temp0 = findViewById(content);
//        //temp0.setAlpha(0);
//        temp0.removeAllViews();
//        View now = getLayoutInflater().inflate(fragment, temp0, false);
//        temp0.addView(now);
//        if (animationOn == 1) {
//            temp0.startAnimation(oke);
//        }
//        //temp0.setAlpha(1);
//        layoutlist.add(layout);
//        contentlist.add(content);
//        plusFragment(fragment);
//    }
//
//    public void setLayout(int layout, int content, int fragment) {
//        setLayout(layout, content, fragment, 1);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle saved) {
//        saved.putAll(saved);
//        super.onSaveInstanceState(saved);
//    }
//
//    /*
//    @Override
//    protected void onRestoreInstanceState(Bundle saved) {
//        super.onRestoreInstanceState(saved);
//        //
//        setLayout(lastLayout(), lastContent(), lastFragment());
//    }
//    */
//
//    public void Exit() {
//        Intent keluar = new Intent(Intent.ACTION_MAIN);
//        keluar.addCategory(Intent.CATEGORY_HOME);
//        keluar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        super.startActivity(keluar);
//    }
//
//    @Override
//    public void onBackPressed() {
//        try {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            if (drawer != null) {
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                    return;
//                }
//            }
//            /*
//            if (fragmentlist.get(fragmentlist.size()-1).size()==0)
//            {
//                fragmentlist.remove(fragmentlist.size()-1);
//            }
//            else{
//                ArrayList<Integer> temp = fragmentlist.get(fragmentlist.size()-1);
//                temp.remove(temp.size()-1);
//                fragmentlist.set(fragmentlist.size()-1, temp);
//            }
//            */
//
//            //intentlist.remove(intentlist.size()-1);
//            //cant remove intent. so...
//
//            ArrayList<Intent> a = new ArrayList<>();
//            for (int i = 0; i < intentlist.size() - 1; i++) {
//                a.add(intentlist.get(i));
//            }
//            intentlist = a;
//
//            if (intentlist.size() <= 1) {
//                Exit();
//            } else {
//                //super.startActivity(intentlist.get(intentlist.size()-2));
//                finish();
//            }
//        } catch (Exception err) {
//            System.exit(0);
//        }
//    }
//
//    public void refresh() {
//        setFragment(lastFragment());
//    }
//
//    public boolean checkUpdate() {
//            switch (new String(downloadData("CheckUpdateActivity", version))) {
//                case "False": {
//                    return false;
//                }
//                default: {
//                    return true;
//                }
//            }
//    }
//    public void startCheckUpdate(){startCheckUpdate(Menu.class, 1);}
//    public void startCheckUpdate(final Class next, int tunggu)
//    {
//        CheckUpdateActivity.next = next;
//        CheckUpdateActivity.tunggu = tunggu;
//        Intent a = new Intent(getApplicationContext(), CheckUpdateActivity.class);
//        startActivity(a);
//    }
//
//    public native String stringFromJNI();
//
//    @Override
//    protected void onCreate(Bundle yay) {
//        super.onCreate(yay);
//        registerFile("loginfile0", "loginfile0.sys");
//        registerFile("loginfile1", "loginfile1.sys");
//        registerFile("info", "info.sys");
//        registerFile("profilepict", "pict.sys", "1");
//        this.context = getApplicationContext();
//        this.tools = new Tools(context);
//        this.login = new LoginActivity();
//        try_again = (Button) findViewById(R.id.try_again);
//        layoutlist.add(R.layout.activity_menu22);
//        contentlist.add(R.id.menu_contents);
//        fragmentlist.add(new ArrayList<Integer>());
//    }
//}