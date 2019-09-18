package com.example.project1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class gameScreen extends AppCompatActivity implements View.OnClickListener{

    ////// TIMER STUFF

    int score = 0;
//    Timer timer = new Timer();
//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//
//            //switchOptions();
//            System.out.println("hello");
//            toast();
//        }
//    };

    //int currentButton = 2131165251; // make this update with the real button value. then set to 0
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<Button> buttons = new ArrayList<Button>();

    TextView tv1;
    TextView timer;
    TextView time_left;
    Random rand = new Random();
    String currentName;
    Button currentButton;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button end_game_button;
    Context context;

    ImageView imageView;
    CountDownTimer currentTimer;
    long millisecondsLeft = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        tv1 = (TextView)findViewById(R.id.textView);
        time_left = (TextView)findViewById(R.id.time_left);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        Button end_game_button = (Button) findViewById(R.id.end_game_button);
        end_game_button.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);

        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);

        getNames();
        switchOptions();
        countDownTimer(6000);
    }

    public void getNames() {

        try{
            DataInputStream textFileStream = new DataInputStream(getAssets().open(String.format("names.txt")));
            Scanner sc = new Scanner(textFileStream);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            nameArray.add(line);
            }
        sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        currentTimer.cancel();

        switch (v.getId()) {
            case R.id.button1:
                System.out.println(v.getId());
                System.out.println(buttons.get(0));
                checkResponse(buttons.get(0).getId(), currentButton.getId());
                break;
            case R.id.button2:
                checkResponse(buttons.get(1).getId(), currentButton.getId());
                break;
            case R.id.button3:
                checkResponse(buttons.get(2).getId(), currentButton.getId());
                break;
            case R.id.button4:
                checkResponse(buttons.get(3).getId(), currentButton.getId());
                break;
            case R.id.end_game_button:
                endGame();
            case R.id.imageView:
                pauseTimer();
                addtoContacts();
                resumeTimer();
        }
    }

    public void checkResponse(int clicked, int real){
        int x = 1;
        while(x > 0){ // parameter is time being less than the 5 secs
            //timer.purge();
            if(clicked == real){
                addToScore();
            }
            else{

                Toast.makeText(gameScreen.this, "Incorrect", Toast.LENGTH_SHORT).show();
            }
            switchOptions();
            countDownTimer(6000);
            x--;
        }
    }

    public void random(){
        int randomNumber = rand.nextInt(nameArray.size());
        currentName = nameArray.get(randomNumber);

        int randomButton = rand.nextInt(buttons.size());
        currentButton = buttons.get(randomButton);
        System.out.println("current button " + currentButton.getId());
        System.out.println("button1: " + buttons.get(0).getId());
        currentButton.setText(currentName);
        System.out.println("hello");
    }

    public void switchOptions(){ // change button info and image
        random();
        String name1 = nameArray.get(rand.nextInt(nameArray.size()));
        String name2 = nameArray.get(rand.nextInt(nameArray.size()));
        String name3 = nameArray.get(rand.nextInt(nameArray.size()));

        if (currentButton.getId() == R.id.button1) {
            System.out.println("FIRST SECTION");
            buttons.get(1).setText(name1);
            buttons.get(2).setText(name2);
            buttons.get(3).setText(name3);
        }
        else if(currentButton.getId() == R.id.button2){
            System.out.println("SECOND SECTION");
            buttons.get(0).setText(name1);
            buttons.get(2).setText(name2);
            buttons.get(3).setText(name3);
        }
        else if(currentButton.getId() == R.id.button3){
            System.out.println("THIRD SECTION");
            buttons.get(0).setText(name1);
            buttons.get(1).setText(name2);
            buttons.get(3).setText(name3);

        }
        else if(currentButton.getId() == R.id.button4){
            System.out.println("FOURTH SECTION");
            buttons.get(0).setText(name1);
            buttons.get(1).setText(name2);
            buttons.get(2).setText(name3);
        }

        Resources resources = getResources();
        imageView = findViewById(R.id.imageView);
        System.out.println("imageView: " + imageView);

        String image = nameToImage(currentName);

        final int resourcesId = resources.getIdentifier(image, "drawable", getPackageName());
        System.out.println("RESOURCES ID" + resourcesId);

        imageView.setImageResource(resourcesId);
        context = imageView.getContext();
    }

    public void endGame() {
        // method frame found on stack overflow: https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        pauseTimer();
        new AlertDialog.Builder(gameScreen.this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit the game?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // exit out of game
                        Intent intent = new Intent(gameScreen.this, MainActivity.class);
                        intent.putExtra("Score", score);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resumeTimer();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void pauseTimer() {
        currentTimer.cancel();
    }

    public void resumeTimer() {
        countDownTimer(millisecondsLeft);
    }

      public String nameToImage(String name) {
        return name.replaceAll("\\s+","").toLowerCase();
      }

      public void addToScore(){
        score += 1;
        String scoreString = Integer.toString(score);
        tv1.setText("Score:" + scoreString);
    }

    public void addtoContacts() {
        // method frame found on stack overflow: https://stackoverflow.com/questions/14278587/insert-a-new-contact-intent

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // Names
        ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            currentName).build());

        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(context, "Contact " + currentName + " added", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Contact not added", Toast.LENGTH_SHORT).show();
        }

    }

    public void countDownTimer(long timeLeft){
        currentTimer = new CountDownTimer(timeLeft, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisecondsLeft = millisUntilFinished;
                time_left.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(gameScreen.this, "You snooze you lose! Choose faster next time", Toast.LENGTH_SHORT).show();
                switchOptions();
                countDownTimer(6000);
            }
        }.start();

    }


}


