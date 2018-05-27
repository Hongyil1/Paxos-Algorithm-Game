package com.example.hylxmu.myapplication;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class GameBoardActivity extends AppCompatActivity {
    Handler h = null;//Handle UI update
    private Button updateButton;
    private EditText editText;
    private TextView result;
    private String userInput;
    private int userNumber;
    private int ramNumber;
    private int numToServer;
    public final int MIN = 0;
    public final int MAX = 10;
    String server_ip ="10.13.76.131";
    int server_port = 7777;
    DataInputStream input;
    DataOutputStream output;
    TextView textview;
    private boolean flag = false;
    Socket socket;


    @SuppressLint("HandlerLeak")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        updateButton = (Button) findViewById(R.id.updateButton);
        editText = (EditText) findViewById(R.id.editText);
        result= (TextView) findViewById(R.id.winLose);
        textview=(TextView) findViewById(R.id.scoreText);
        new Thread(runnable).start();
        new Thread(runnable1).start();
        h = new Handler(){
            public void handleMessage(Message msg){
                textview.setText(msg.getData().getString("value"));
            }
        };
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput = editText.getText().toString();
                if (validation(userInput)) {

                    if(compareNum()) {
                        result.setText("You Win!"+ "Number is: " +ramNumber);
                        numToServer = 1;
                        flag = true;

                    }else{
                        result.setText("You Lose..." + "Number is: " +ramNumber);
                        numToServer = -1;
                        flag = true;
                    }

                }else{
                    result.setText("Please enter the correct integer!");
                }

            }
        });
    }

    // this method is used to validate the user input
    public boolean validation(String userInput){
        if(intValidation(userInput)){
            userNumber = Integer.parseInt(userInput);
            if(rangeValidation(userNumber)){
                return true;
            }
        }
        return false;
    }
    // this method is used to check whether the user input is an int
    public boolean intValidation(String userInput) {
        try {
            Integer.parseInt(userInput);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // this method is used to check whether the user input int is with in the given range
    public boolean rangeValidation(int userNumber){
        if(userNumber>=MIN && userNumber<=MAX){
            return true;
        }else{
            return false;
        }
    }
    // this method is used to compare the user input with system ramdon number
    public boolean compareNum(){
        ramNumber = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
        if(userNumber==ramNumber){
            return true;
        }
        return false;
    }

    Runnable runnable1 = new Runnable()//New Thread to output to Server
    {
        @Override
        public void run() {
            while(true){
                // TODO: http request.
                if (flag==true)//If Client have new value to output
                {
                    try {
                        output.writeUTF(String.valueOf(numToServer));//Output to server
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                }
            }
        }
    };
    Runnable runnable = new Runnable() //New Thread to establish NetworkConnection and listening for input
    {
        @Override
        public void run() {
            try {
                socket = new Socket(server_ip, server_port);

                // Input and ouput stream
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());

                while(true){

                    if(input.available() > 0){
                        String message_rec= input.readUTF();
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("value",message_rec);//Get input from Server
                        msg.setData(data);
                        h.sendMessage(msg);//Send result to UI

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
