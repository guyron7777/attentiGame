package com.GuyRon.attentigame.mainActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.GuyRon.attentigame.R;
import com.GuyRon.attentigame.adapters.IslandsAdapter;
import com.GuyRon.attentigame.models.ColorsObj;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IslandsAdapter.ItemClickListener {
    private MainActivityViewModel viewModel;
    private IslandsAdapter gridviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTopBar();
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        Intent intent = getIntent();

        int[][] m = new int
                [intent.getIntExtra("row", 5)]
                [intent.getIntExtra("col", 5)];

        RecyclerView gridview = findViewById(R.id.recycler);
        final Button solve = findViewById(R.id.solve);
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (solve.getText().toString().contains("RESTART")) {
                    onBackPressed();
                } else {
                    getIslandNumber GetMyCoupons = new getIslandNumber();
                    GetMyCoupons.execute();
                }
            }
        });

        gridviewAdapter = new IslandsAdapter(MainActivity.this, m);
        gridview.setLayoutManager(new GridLayoutManager(this, m[0].length));
        gridview.setAdapter(gridviewAdapter);
        viewModel.startAdapter(m, intent.getBooleanExtra("randomize", true));


        final Observer<String> islandsObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                solve.setText(newName + " islands\n" + "RESTART");
            }
        };
        final Observer<Boolean> notifyObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean doNotify) {
                if (doNotify != null && doNotify) {
                    gridviewAdapter.notifyDataSetChanged();
                    viewModel.shouldNotify.setValue(false);
                }
            }
        };
        final Observer<Boolean> useClickObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isClick) {
                if (isClick != null && isClick) {
                    gridviewAdapter.setClickListener(MainActivity.this);
                    viewModel.isUseClick.setValue(false);
                }
            }
        };
        final Observer<ArrayList<ColorsObj>> changeColorsObserver = new Observer<ArrayList<ColorsObj>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<ColorsObj> color) {
                if (color != null && !color.isEmpty())
                    for (int i = 0; i < color.size(); i++) {
                        gridviewAdapter.changeColor(color.get(i));
                    }
            }
        };
        viewModel.setIslandsNumber().observe(this, islandsObserver);
        viewModel.shouldNotify.observe(this, notifyObserver);
        viewModel.isUseClick.observe(this, useClickObserver);
        viewModel.getColorsObj.observe(this, changeColorsObserver);
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

    /**
     * run the search in the bacground thread and post the answer via rx
     */
    private class getIslandNumber extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            //enable new clicks after we started the search
            gridviewAdapter.setClickListener(null);
            //start the search
            return viewModel.islandsNumber() + "";
        }

        @Override
        protected void onPostExecute(String numberOfIslands) {
            super.onPostExecute(numberOfIslands);
            //call liveData
            viewModel.setIslandsNumber().setValue(numberOfIslands + "");
            gridviewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * if we in the bonus game we can get here in the click:
     * if it an islan click remove it
     * else click make it an island
     */
    @Override
    public void onItemClick(View view, int position) {
        int[][] matrix = viewModel.getMatrix();

        if (matrix[position / matrix[0].length][position % matrix[0].length] == 0)
            matrix[position / matrix[0].length][position % matrix[0].length] = 1;
        else matrix[position / matrix[0].length][position % matrix[0].length] = 0;
        gridviewAdapter.notifyItemChanged(position);
    }
}
