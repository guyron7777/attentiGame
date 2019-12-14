package com.GuyRon.attentigame.homeActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.GuyRon.attentigame.R;
import com.GuyRon.attentigame.mainActivity.MainActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText colsNum;
    private EditText rowsNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTopBar();
        setContentView(R.layout.activity_home);
        Button randomize = findViewById(R.id.randomize);
        Button choose = findViewById(R.id.choose);
        ConstraintLayout mainScreen = findViewById(R.id.mainScreen);
        mainScreen.setOnClickListener(this);
        randomize.setOnClickListener(this);
        choose.setOnClickListener(this);
        colsNum = findViewById(R.id.etColsNum);
        rowsNum = findViewById(R.id.etRowsNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.randomize:
                nextPage(true);
                break;
            case R.id.choose:
                nextPage(false);
                break;
        }
    }

    public void nextPage(boolean randomize) {
        if (rowsNum.getText().toString().isEmpty()) {
            rowsNum.setError("must have more than 1 row");
            rowsNum.requestFocus();
        } else if (colsNum.getText().toString().isEmpty()) {
            colsNum.setError("must have more than 1 col");
            colsNum.requestFocus();
        } else {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.putExtra("randomize", randomize);
            intent.putExtra("col", Integer.parseInt(colsNum.getText().toString()));
            intent.putExtra("row", Integer.parseInt((rowsNum.getText().toString())));
            startActivity(intent);
        }
    }

    private void hideTopBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).hide();
        } else {
            try {
                getSupportActionBar().hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
