package com.Quadranet.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.Quadranet.myapplication.retrofit.DNAPedAPI;
import com.Quadranet.myapplication.retrofit.DNAPedResult;
import com.Quadranet.myapplication.retrofit.RetroFitClient;
import com.Quadranet.myapplication.service.DnaService;
import com.Quadranet.myapplication.service.IDnaService;
//import com.example.myapplication.R;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.quadranetepos.R;
//import com.quadranet.dbx.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private IDnaService _dnaService; //service to call epos
    private DNAPedAPI  _dnaAPI;
    private static IDAL dal;
    private String clientGuid;
    private WebView mywebView;
    private String serialNumber;
    private String ipAddress;
    private boolean _dbxloaded;

    protected void onCreate(Bundle savedInstanceState) {
        _dbxloaded=false;
        serialNumber = getSerialNumber();
        ipAddress =getIPAddress();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tryPrinter();

        if(clientGuid== null){
            GetPedDetails(serialNumber);
        }
    }
    @Override
    public void onBackPressed() {
        if (mywebView.canGoBack()) {
            mywebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void loadWEbViewer(){
        try{

            if(_dbxloaded){
                return;
            }

            mywebView=(WebView) findViewById(R.id.webview);

            WebSettings webSettings=mywebView.getSettings();
            Log.d("WebViewer","GotIN");
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setSupportZoom(true);

            webSettings.setDefaultTextEncodingName("utf-8");
            Log.d("WebViewer","Finihs Adding Settings");

            mywebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d("WebViewer","Not created ");
                    return false;
                }


            });
            Log.d("WebViewer","loading dbx ");

            //mywebView.loadUrl("https://dbxlive.quadranet.co.uk/Login/"+clientGuid+"/P/"+ipAddress+"/"+serialNumber);
            mywebView.loadUrl("https://dbxdev.quadranet.co.uk/Login/"+clientGuid+"/P/"+ipAddress+"/"+serialNumber);
            if(clientGuid!=null){
                _dbxloaded=true;
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();

        }
    }

    private void GetPedDetails(String serialNumber) {
        try
        {
            if(clientGuid!=null) {
                return;
            }
            Call<DNAPedResult> call = RetroFitClient.getInstance().getMyApi().getPedURL(serialNumber,ipAddress);
            //if(call==null) showError();
            call.enqueue(new Callback<DNAPedResult>() {
            @Override
            public void onResponse(Call<DNAPedResult> call, Response<DNAPedResult> response) {
                DNAPedResult PedDetails = response.body();

                if(PedDetails==null) {
                    showError();
                    return;
                }


                if(PedDetails.success){
                    clientGuid=PedDetails.url;
                }
                else{
                    showError();
                }

                if(PedDetails.use_webpos==2){
                    loadWEbViewer();
                }
                else if(PedDetails.success && PedDetails.use_webpos!=2)
                {
                    showClosePopUp(PedDetails.use_webpos);
                }
                else
                {
                 showError();
                }

            }

            @Override
            public void onFailure(Call<DNAPedResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Fatal calling API", Toast.LENGTH_LONG).show();

        }
    }

    private void showClosePopUp(int choice)
    {
        ImageView image = new ImageView(this);
        //image.setImageResource(R.drawable.hellodiogo);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.hellodiogo);

        builder.setTitle("Terminal Config Complete");

        if(choice==1){

            builder.setNegativeButton("Load POS", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dlg, int sumthin) {
                    loadWEbViewer();
                }
            });
        }

        builder.setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                // Close the current activity
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    private void showError()
    {
        ImageView image = new ImageView(this);
        //image.setImageResource(R.drawable.hellodiogo);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.hellodiogo);

        builder.setTitle("Please contact Quadranet Systems");

        SpannableStringBuilder sbuilder = new SpannableStringBuilder();
        SpannableString phoneSpan = new SpannableString("Your device requires to be set up\n\n");
        phoneSpan.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, phoneSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.append(phoneSpan);
        SpannableString aSpan = new SpannableString("Phone No: 01494 473 337 OPT 1\n");
        aSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, aSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.append(aSpan);

        SpannableString bSpan = new SpannableString("Email : support@quadranet.co.uk\n");
        bSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, bSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.append(bSpan);

        SpannableString cSpan = new SpannableString("PED Serial Number: "+serialNumber+" \n");
        cSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, cSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.append(cSpan);

        SpannableString eSpan = new SpannableString("IpAddress: "+ ipAddress +" \n");
        eSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, eSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.append(eSpan);
        //sbuilder.append(message.substring("Your device requires to be set u".length()));

        sbuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, phoneSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.setSpan(new StyleSpan(Typeface.NORMAL), 0, aSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.setSpan(new StyleSpan(Typeface.NORMAL), 0, bSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.setSpan(new StyleSpan(Typeface.NORMAL), 0, cSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbuilder.setSpan(new StyleSpan(Typeface.NORMAL), 0, eSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(sbuilder);
        builder.setView(image);
        builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                GetPedDetails(serialNumber);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    public IDAL getDal() {
        if(dal == null){ //dal is a private static IDAL variable of the application
            try {
                dal = NeptuneLiteUser.getInstance().getDal(this);
            } catch (Exception e) {}
        }
        return dal;
    }
    private void tryPrinter()
    {
        try {
            IPrinter printer= getDal().getPrinter();

            printer.init();
            EFontTypeAscii asciiFontType = EFontTypeAscii.FONT_16_32;
            EFontTypeExtCode fontTypeExtCode = EFontTypeExtCode.FONT_24_24;
            printer.fontSet(asciiFontType, fontTypeExtCode);



            printer.start();
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //connect to service
        Intent serviceIntent = new Intent(this, DnaService.class);
        startService(serviceIntent);
        bindService(serviceIntent, _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {

        if (_serviceConnection != null)
        {
            unbindService(_serviceConnection);
        }

        super.onStop();
    }

    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _dnaService = ((DnaService.DnaServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    // Get the device's serial number
    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    // Get the device's IP address
    public static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (intf.getName().equalsIgnoreCase("wlan0") || intf.getName().equalsIgnoreCase("wlan1")) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                            return addr.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}