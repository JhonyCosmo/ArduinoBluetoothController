package br.arduinobluetoothcontroller.Views.Controls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import br.arduinobluetoothcontroller.BluetoothConnection.ConnectionWrapper;
import br.arduinobluetoothcontroller.R;

public class CarControlActivity extends AppCompatActivity {

    private ImageButton btnLeft;
    private ImageButton btnRight;
    private ImageButton btnUp;
    private ImageButton btnDown;
    private boolean btnLeftIsPressed;
    private boolean btnRightIsPressed;
    private boolean btnUpIsPressed;
    private boolean btnDownIsPressed;
    private Button btnBuz;
    private Button btnEnviarTexto;
    private EditText edEnviarTexto;
    //region Eventos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_controller);
        btnLeft=(ImageButton)  findViewById(R.id.btnleft);
        btnRight=(ImageButton)  findViewById(R.id.btnright);
        btnUp=(ImageButton)  findViewById(R.id.btnup);
        btnDown=(ImageButton)  findViewById(R.id.btndown);
        btnBuz=(Button)  findViewById(R.id.btnbuz);
        btnEnviarTexto=(Button)  findViewById(R.id.btnEnviartexto);
        edEnviarTexto=(EditText) findViewById(R.id.editText);


        btnEnviarTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeCommand("{" + edEnviarTexto.getText().toString() + "}");
                edEnviarTexto.setText("");
            }
        });

        btnBuz.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        writeCommand("{bz1}");
                        writeCommand("{sai do mei!!!}");
                        return true;

                    case MotionEvent.ACTION_UP:
                        writeCommand("{bz0}");
                        return true;
                }
                return false;
            }
        });

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnLeftIsPressed=true;
                        writeCommand("{mlo}");
                        writeCommand("{mra}");
                        return true;

                    case MotionEvent.ACTION_UP:
                        btnLeftIsPressed=false;
                        stopMotors();
                        return true;
                }
                return false;
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnRightIsPressed=true;
                        writeCommand("{mla}");
                        writeCommand("{mro}");
                        return true;

                    case MotionEvent.ACTION_UP:
                        btnRightIsPressed=false;
                        stopMotors();
                        return true;
                }
                return false;
            }
        });

        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnUpIsPressed=true;
                        writeCommand("{mlo}");
                        writeCommand("{mro}");
                        return true;

                    case MotionEvent.ACTION_UP:
                        btnUpIsPressed=false;
                        stopMotors();
                        return true;
                }
                return false;
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnDownIsPressed=true;
                        writeCommand("{mla}");
                        writeCommand("{mra}");
                        return true;

                    case MotionEvent.ACTION_UP:
                        btnDownIsPressed=false;
                        stopMotors();
                        return true;
                }
                return false;
            }
        });

    }
    //endregion
    //region Metodos

    private void stopMotors(){

        if(btnLeftIsPressed) return;
        if(btnRightIsPressed) return;
        if(btnUpIsPressed) return;
        if(btnDownIsPressed) return;

        writeCommand("{mls}");
        writeCommand("{mrs}");

    }
    private void writeCommand(String strCommand){

        byte[] data =  strCommand.getBytes();

        ConnectionWrapper.getConnection().write(data);

    }
    //endregion
}
