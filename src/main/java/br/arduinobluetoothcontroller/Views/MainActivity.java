package br.arduinobluetoothcontroller.Views;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.arduinobluetoothcontroller.BluetoothConnection.ConnectionThread;
import br.arduinobluetoothcontroller.BluetoothConnection.ConnectionWrapper;
import br.arduinobluetoothcontroller.BluetoothConnection.Constants;
import br.arduinobluetoothcontroller.Views.Controls.CarControl2ButtonsActivity;
import br.arduinobluetoothcontroller.Views.Controls.CarControlActivity;
import br.arduinobluetoothcontroller.R;

public class MainActivity extends AppCompatActivity {

    private static TextView txtStatusMessage;
    private Button btnSeachPairedDevices;
    private Button btnDiscoveredDevices;
    private Button btnwaitConnection;
    private Button btnSendMessage;
    private Button btnSendCommand;
    private static TextView txtSpace;


    //region Eventos
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtStatusMessage=(TextView) findViewById(R.id.statusMessage);

        btnSeachPairedDevices=(Button)  findViewById(R.id.button_PairedDevices);

        btnDiscoveredDevices=(Button)  findViewById(R.id.button_DiscoveredDevices);

        ///btnEnableVisibility=(Button)  findViewById(R.id.button_Visibility);

        btnwaitConnection=(Button)  findViewById(R.id.button_WaitConnection);

        btnSendCommand=(Button)  findViewById(R.id.button_Send);

        btnSendMessage=(Button)  findViewById(R.id.button_SendText);

        txtSpace = (TextView) findViewById(R.id.textSpace);

        BluetoothAdapter mBTAdapter= BluetoothAdapter.getDefaultAdapter();

        if(mBTAdapter==null){

            setMensagem("Não foi encontrato nehum adaptador bluetooth.");

            return;

        }else{

            setMensagem("Adapdador bluetooth encotrado e funcionando.");

        }

        if(!mBTAdapter.enable()){

            Intent mEnableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(mEnableBtIntent, Constants.ENABLE_BLUETOOTH);

            setMensagem("Solicitando ativação do Adaptador...");

        }else{

            setMensagem("Bluetooth ativado");

        }

        btnSeachPairedDevices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchPairedDevices(v);
            }
        });

        btnDiscoveredDevices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                discoverDevices(v);
            }
        });

        btnwaitConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                waitConnection(v);
            }
        });

        btnSendCommand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendCommand(v);
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_carcontrol:

                if( ConnectionWrapper.getConnection()!=null){

                    try{

                        Intent intent=new Intent(this,CarControlActivity.class);

                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        setMensagem(e.getMessage());
                    }



                }
                else{

                    setMensagem("Conecte o dispositivo primeiro.");

                }

                return true;

            case R.id.menu_carcontrol2:

                if( ConnectionWrapper.getConnection()!=null){

                    try{

                        Intent intent=new Intent(this,CarControl2ButtonsActivity.class);

                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        setMensagem(e.getMessage());
                    }



                }
                else{

                    setMensagem("Conecte o dispositivo primeiro.");

                }

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == Constants.ENABLE_BLUETOOTH) {

            if(resultCode == RESULT_OK) {

                setMensagem("Bluetooth ativado :D");

            }
            else {

                setMensagem("Bluetooth não ativado :(");

            }
        }else if(requestCode == Constants.SELECT_PAIRED_DEVICE || requestCode == Constants.SELECT_DISCOVERED_DEVICE) {

            if(resultCode == RESULT_OK) {

                setMensagem("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));

                ConnectionWrapper.setConnection(new ConnectionThread(data.getStringExtra("btDevAddress")));

                ConnectionWrapper.getConnection().start();

            }
            else {

                setMensagem("Nenhum dispositivo selecionado :(");

            }
        }
    }
    //endregion

    //region Metodos

    private static void setMensagem(String mtxt){

        txtStatusMessage.setText(mtxt);

    }

    private void searchPairedDevices(View view){

        Intent searchPairedDevicesIntent = new Intent(this, PairedDevicesListActivity.class);

        startActivityForResult(searchPairedDevicesIntent, Constants.SELECT_PAIRED_DEVICE);

    }

    public void discoverDevices(View view) {

        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevicesListActivity.class);

        startActivityForResult(searchPairedDevicesIntent, Constants.SELECT_DISCOVERED_DEVICE);
    }

    public void enableVisibility(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);

        startActivity(discoverableIntent);
    }

    public void waitConnection(View view) {

        ConnectionWrapper.setConnection(new ConnectionThread());

        ConnectionWrapper.getConnection().start();

    }

    public void sendCommand(View view) {

        EditText messageBox = (EditText) findViewById(R.id.editText_MessageBox);
        String messageBoxString = messageBox.getText().toString();
        byte[] data =  messageBoxString.getBytes();
        ConnectionWrapper.getConnection().write(data);
        messageBox.setText("");
    }

    public void sendMessage(View view) {

        EditText messageBox = (EditText) findViewById(R.id.editText_MessageBox);
        String messageBoxString = String.format("{" + messageBox.getText().toString() + "}");
        byte[] data =  messageBoxString.getBytes();
        ConnectionWrapper.getConnection().write(data);
        messageBox.setText("");

    }
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);

            if (dataString.equals("---N"))
                setMensagem("Ocorreu um erro durante a conexão D:");
            else if (dataString.equals("---S"))
                setMensagem("Conectado :D");
            else {

                txtSpace.setText(new String(data));
            }
        }
    };
//endregion
}

