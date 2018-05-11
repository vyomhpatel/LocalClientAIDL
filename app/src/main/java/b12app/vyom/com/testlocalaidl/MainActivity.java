package b12app.vyom.com.testlocalaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import b12app.vyom.com.aidlremotepractice.IRemote;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultText;
    private EditText firstValue, secondValue;
    private Button add;
    protected IRemote mService;
    private ServiceConnection mServiceConnection;
    private  String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        resultText = findViewById(R.id.resultText);
        firstValue = findViewById(R.id.firstValue);
        secondValue = findViewById(R.id.secondValue);
        add.setOnClickListener(this);

    }

    private void initConnection(final int a, final int b) {



        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IRemote.Stub.asInterface((IBinder) service);

//                try{
//                    mService.add(2,3);
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
                try {
                    result    = String.valueOf(mService.add(a, b));
                    resultText.setText(result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.i("IRemote", "onServiceConnected: ");


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                mService = null;
                Log.i("IRemote", "onService Disconnected: ");
            }
        };

        if (mService == null) {

            Intent intent = new Intent();

            intent.setAction("b12app.vyom.com.aidlremotepractice.IRemote");

            intent.setPackage("b12app.vyom.com.aidlremotepractice");

            startService(intent);

            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:

                int a = Integer.parseInt(firstValue.getText().toString());
                int b = Integer.parseInt(secondValue.getText().toString());
                Log.i("a&b", "onClick: " + a + " " + b);

                initConnection(a, b);




                break;
        }
    }
}