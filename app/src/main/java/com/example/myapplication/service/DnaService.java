package com.example.myapplication.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.retrofit.DNARetrofit;
import com.example.myapplication.retrofit.EposResult;
import com.example.myapplication.retrofit.ServiceGenerator;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.neptunelite.api.NeptuneLiteUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DnaService extends Service implements IDnaService, Runnable
{
    private static final String TAG = DnaService.class.getSimpleName();
    private static final int CHECK_INTERVAL = 10000;
    private static final int NOTIFICATION_ID = 3254;
    private NotificationManager _notificationManager;
    DNARetrofit _retroService = ServiceGenerator.createService(DNARetrofit.class);
    private Thread _thread;
    private boolean _stopThread;

    //printer stuff
    IPrinter _printer = null;

    public class DnaServiceBinder extends Binder
    {
        public IDnaService getService()
        {
            return DnaService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return new DnaServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(NOTIFICATION_ID, createNotification());
        //preparePrinter(); //I don't have a printer, so I commented this out
        startThread();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        return START_STICKY;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void someMethodFromService()
    {
        Toast.makeText(this, "someMethodFromService!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void killService() {
        stopForeground(true);
        stopThread();
        stopSelf();
    }

    private void startThread()
    {
        if (_thread != null) return;

        _stopThread = false;
        _thread = new Thread(this);
        _thread.setName(TAG + " - thread");
        _thread.start();
    }

    private synchronized void stopThread()
    {
        _stopThread = true;
        _thread = null;
        notify(); //notify releases wait
    }

    //thread func
    @Override
    public synchronized void run()
    {
        try
        {
            while (_stopThread != true)
            {
                doJob();
                wait(CHECK_INTERVAL);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Notification createNotification()
    {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new Notification.Builder(this, createNotificationChannel())
                        .setContentTitle("DNA Service")
                        .setContentText("DNA Service is running...")
                        .setSmallIcon(R.drawable.ic_stat_payment)
                        .setContentIntent(pendingIntent)
                        .setTicker("DNA")
                        .build();
    }

    private String createNotificationChannel()
    {
        String channelId = "DNA_SERVICE";
        String channelName = "DNA CHANNEL";

        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);

        chan.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        chan.setSound(null, null);
        chan.setShowBadge(true);

        _notificationManager.createNotificationChannel(chan);
        return channelId;
    }

    /*
        called every CHECK_INTERVAL ms
     */
    private void doJob()
    {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Log.d(TAG, "start doJob:" + time);

        Call<EposResult> call = _retroService.callEpos();

        try
        {
            Response<EposResult> response = call.execute();

            EposResult result = response.body();

            if (result != null)
            {
                sendToPrinter(result);
            }
            else
            {
                Log.d(TAG, "doJob response is null");
            }

            //http 400 error // request url is in ServiceGenerator.API_BASE_URL
            Log.d(TAG, "doJob response: " + response.toString());
        }
        catch (Exception e)
        {
            Log.d(TAG, "doJob exception: " + e.getMessage());
        }
    }

    private void sendToPrinter(EposResult result)
    {
        if (_printer != null)
        {
            try {
                _printer.printStr(result.foo1, null);
                _printer.printStr(result.foo2, null);
            }
            catch (Exception e)
            {
                Log.d(TAG, "sendToPrinter exception: " + e.getMessage());
            }
        }
    }

    private void preparePrinter()
    {
        try {
            _printer = NeptuneLiteUser.getInstance().getDal(getApplicationContext()).getPrinter();

            if (_printer == null)
            {
                Log.d(TAG, "preperePrinter printer is null");
                return;
            }

            _printer.init();
            EFontTypeAscii asciiFontType = EFontTypeAscii.FONT_12_24;
            EFontTypeExtCode fontTypeExtCode = EFontTypeExtCode.FONT_24_24;
            _printer.fontSet(asciiFontType, fontTypeExtCode);
        }
        catch (Exception e)
        {
            Log.d(TAG, "preperePrinter exception: " + e.getMessage());
            _printer = null;
        }
    }

    public Call<PedConnection> getPedConnection(String serialNumber) {
        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://localhost:44391/API/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the DnaService interface
        DnaService dnaService = retrofit.create(DnaService.class);

        // Call the method on the DnaService interface
        Call<PedConnection> call = dnaService.getPedConnection(serialNumber);

        // Execute the request asynchronously
        call.enqueue(new Callback<PedConnection>() {
            @Override
            public void onResponse(Call<PedConnection> call, Response<PedConnection> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    PedConnection pedConnection = response.body();
                    // Do something with the PedConnection object
                } else {
                    // Handle error response
                    // You can use response.errorBody() to get the error message
                }
            }

            @Override
            public void onFailure(Call<PedConnection> call, Throwable t) {
                // Handle network error
            }
        });
        return call;
    }



}
