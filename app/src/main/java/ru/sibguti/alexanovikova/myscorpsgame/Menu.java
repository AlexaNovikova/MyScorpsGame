package ru.sibguti.alexanovikova.myscorpsgame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Menu extends Activity implements View.OnClickListener {
    ImageButton imageButtonPlay;
    ImageButton editNickButton;
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    TextView helloView;
    private String nick;
    private Spinner spinner;
    public SharedPreferences sharedPreferences;
    public static final String APP_PREFERENCES = "apppref";
    private static final String fileName = "score.txt";
    private ArrayList<String> scoreList;
    private HashMap<String, Integer> scoresMap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        sharedPreferences= getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        imageButtonPlay = (ImageButton) findViewById(R.id.playButton);
        imageButtonPlay.setOnClickListener(this);
        editNickButton = (ImageButton) findViewById(R.id.editButton);
        editNickButton.setOnClickListener(this);
        editNickButton.setVisibility(View.INVISIBLE);
        helloView = (TextView) findViewById(R.id.hello);
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        textInputEditText = findViewById(R.id.userName);
        ListView listView = findViewById(R.id.scores);
        scoreList = new ArrayList<>();
        fillScoresList();
        if(this.getIntent().getStringExtra("nick")!=null){
            nick = this.getIntent().getStringExtra("nick");
            textInputEditText.setVisibility(View.INVISIBLE);
            textInputLayout.setVisibility(View.INVISIBLE);
            helloView.setText("Hello, "+ nick+ "!");
            editNickButton.setVisibility(View.VISIBLE);
        }
        spinner = findViewById(R.id.spinner);

        textInputEditText.setOnKeyListener(new View.OnKeyListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                      nick = textInputEditText.getText().toString();
                        System.out.println(nick);
                        editNickButton.setVisibility(View.VISIBLE);
                        helloView.setText("Hello, "+ nick+ "!");
                        helloView.setVisibility(View.VISIBLE);
                        textInputLayout.setVisibility(View.INVISIBLE);
                        textInputEditText.setVisibility(View.INVISIBLE);
                        return true;
                    }
                return false;
            }
        });

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        final ArrayAdapter<String> listAdapter;
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,scoreList);
        // Привяжем массив через адаптер к ListView
        listView.setAdapter(listAdapter);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.amount,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Вызываем адаптер
        spinner.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillScoresList() {
        readFile();
        String nick;
        int score;
        for(String line: scoreList){
            nick = line.split(":")[0];
            score = Integer.parseInt(line.split(": ")[1]);
            scoresMap.put(nick, score);
            System.out.println(nick + ":"+ score);
        }
        Map<String, Integer> sortedMap = scoresMap.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));
        scoreList.clear();
        for(Map.Entry<String, Integer> entry: sortedMap.entrySet()){
            scoreList.add(entry.getKey()+ ": " + entry.getValue());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                Intent intent = new Intent(Menu.this, MainActivity.class);
                intent.putExtra("scorpsAmount", spinner.getSelectedItem().toString());
                intent.putExtra("nick", nick);
                startActivity(intent);
                break;
            case R.id.editButton:
                if(editNickButton.getVisibility()==View.VISIBLE) {
                    nick = "";
                    textInputEditText.setVisibility(View.VISIBLE);
                    textInputLayout.setVisibility(View.VISIBLE);
                    editNickButton.setVisibility(View.INVISIBLE);
                    helloView.setVisibility(View.INVISIBLE);
                }

        }
    }

    private void readFile() {
        File myFile = new File( Environment.getExternalStorageDirectory()+"/" + fileName);
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            /*
             * Буфферезируем данные из выходного потока файла
             */
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            /*
             * Класс для создания строк из последовательностей символов
             */
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                /*
                 * Производим построчное считывание данных из файла в конструктор строки,
                 * После того, как данные закончились, производим вывод текста в ListView
                 */
                while ((line = bufferedReader.readLine()) != null){
                    scoreList.add(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
