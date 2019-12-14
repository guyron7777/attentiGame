package com.GuyRon.attentigame.mainActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.GuyRon.attentigame.models.ColorsObj;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivityViewModel extends AndroidViewModel {
    private int[][] matrix = null;
    private MutableLiveData<String> islandNumber;
    private int getColor = 0;
    private ArrayList<ColorsObj> colorsArray = new ArrayList<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<Boolean> isUseClick = new MutableLiveData<>(false);
    MutableLiveData<Boolean> shouldNotify = new MutableLiveData<>(false);
    MutableLiveData<ArrayList<ColorsObj>> getColorsObj = new MutableLiveData<>();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[i][j] = new Random().nextInt(2);
                }
            }
            try {
                shouldNotify.setValue(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * init the Matrix by size (and if it random by values also)
     */
    void startAdapter(final int[][] matrix, boolean isRandomize) {
        this.matrix = matrix;
        if (isRandomize) new Thread(runnable).start();
        else isUseClick.setValue(true);
    }

    /**
     * @return number of islands in the matrix
     */
    public int islandsNumber() {
        //initialize all params
        int ROW = matrix.length;
        if (ROW == 0)
            return 0;
        int COL = matrix[0].length;
        int result = 0;
        Stack<stackObj> stack = new Stack<>();
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];
        //initialize all squares to false (not island)
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                visited[i][j] = false;
            }
        }
        //add all islands to stack -
        //if it is a new one we add the result and check for neighbors
        //else we move to the next squer
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (!visited[i][j] && matrix[i][j] == 1) {
                    stack.push(new stackObj(i, j));
                    findIslandNeighbors(stack, matrix, visited);
                    getColor++;
                    result++;
                }
            }
        }
        getColorsObj.postValue(colorsArray);
        return result;
    }

    /**
     * run the search of all the neighbors in the iland
     * its not the fast way, but it is the best way because the usage of the memory in the recursive way
     * (we need 1000*1000 and this is bigger then 1000 mb in the recursive way)
     */
    private void findIslandNeighbors(Stack<stackObj> stack, int[][] matrix, boolean[][] visited) {
        //init all params
        final int rowsNum = matrix.length;
        int colsNum = matrix[0].length;
        //we check all neighbors, and than to the other neghibors and go on.
        while (!stack.empty()) {
            //all the neighbors in the stack are the same islands
            //they can be from the first island squer in the beginning
            //and they can be from the below code - the neighbors of neighbors
            stackObj stackObj = stack.pop();
            final int row = stackObj.row;
            final int col = stackObj.col;
            //check if we are in the matrix and if we didnt visit and if this is island else we get out of the loop
            if (row < 0 || col < 0 || row >= rowsNum || col >= colsNum || visited[row][col] || matrix[row][col] != 1)
                continue;
            //if we have got so far the corrent squer is an island so we check it as one
            //and than check all of his neighbors
            visited[row][col] = true;
            //add to array all the colors obj and in the end of the array we notify the adapter and change the color as needed
            int COLORS_NUMBER = 7;
            ColorsObj colorsObj = new ColorsObj(row, col, getColor % COLORS_NUMBER);
            colorsArray.add(colorsObj);
            //add to stack all the neighbors and check every one of them if it good to add to array
            stack.push(new stackObj(row, col - 1));
            stack.push(new stackObj(row, col + 1));
            stack.push(new stackObj(row - 1, col));
            stack.push(new stackObj(row + 1, col));
            stack.push(new stackObj(row + 1, col - 1));
            stack.push(new stackObj(row + 1, col + 1));
            stack.push(new stackObj(row - 1, col - 1));
            stack.push(new stackObj(row - 1, col + 1));
        }


    }

    /**
     * the usage of livedata send later the final number of islands to the view
     */
    MutableLiveData<String> setIslandsNumber() {
        if (islandNumber == null) {
            islandNumber = new MutableLiveData<>();
        }
        return islandNumber;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * we did this new class just to make the code clear
     */
    class stackObj {
        int row;
        int col;

        public stackObj(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
