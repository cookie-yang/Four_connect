package com.example.neo.connect_four;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by neo on 19/10/2016.
 */

public class Game extends AppCompatActivity {

    LinearLayout col[]=new LinearLayout[7];
    ImageView chess[][]=new ImageView[6][7];
    int [][]boardArray = new int[6][7];

    boolean isFirst = true;
    boolean isWin = false;

    int colIndex[]=new int[7];

    int stateStack[]=new int[42];
    int stackIndex = 0;

    int winNumFirst = 0;
    int winNUmSecend = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initChessBoard();
        dropChess();
        backStep();
        endGame();
    }

    public void initChessBoard(){
       //set col's layout as defined in the activity_game.xml
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ScreenHight = dm.heightPixels;
        int ScreenWidth = dm.widthPixels;

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.cheeseboard);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,ScreenHight/8*6));
        relativeLayout.setY(ScreenHight/16*4);

        RelativeLayout topPanel=(RelativeLayout) findViewById(R.id.toppanel);
        topPanel.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,ScreenHight/4));

        TextView winNumone = (TextView)findViewById(R.id.winNum1);
        winNumone.setText(winNumFirst+"");
        TextView winNumtwo = (TextView)findViewById(R.id.winNum2);
        winNumtwo.setText(winNUmSecend+"");

        for (int i=0;i<7;i++){
            LinearLayout linearlayout = new LinearLayout(this);
            linearlayout.setId(i);
            linearlayout.setOrientation(LinearLayout.VERTICAL);
            linearlayout.setX(ScreenWidth/7*i);
            linearlayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenWidth/7,ScreenWidth/7*6+ScreenWidth/28));
            linearlayout.setBackgroundResource(R.drawable.linearlayout);
            col[i]=linearlayout;
            relativeLayout.addView(col[i]);

        }

       //set emptychess's layout
        for(int j =0;j<7;j++)
        {
            for(int i =0;i<6;i++)
            {
                boardArray[i][j]=0;
                ImageView imageview=new ImageView(this);
                imageview.setY(ScreenWidth/7/20*i);
                imageview.setLayoutParams(new ViewGroup.LayoutParams(ScreenWidth/7,ScreenWidth/7));
                chess[i][j]=imageview;
            }
        }

       //put each emptychess in the column
       for(int j=0;j<7;j++)
       {   colIndex[j]=5;
           for(int i =0;i<6;i++)
           {
               col[j].addView(chess[i][j]);
           }
       }

   }


    public void reset()
    {

        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ScreenHight = dm.heightPixels;
        int ScreenWidth = dm.widthPixels;

        isFirst = true;
        isWin = false;

        int stateStack[]=new int[42];
        stackIndex = 0;

        ImageView turnView1=(ImageView)findViewById(R.id.turnview1);
        ImageView turnView2=(ImageView)findViewById(R.id.turnview2);

        turnView1.setImageResource(R.drawable.hordchess_win);
        turnView2.setImageResource(R.drawable.alliancechess);


        for(int j=0;j<7;j++)
        {
            colIndex[j]=5;
            for(int i=0;i<6;i++)
            {
                stateStack[i*7+j]=0;
                boardArray[i][j]=0;
                chess[i][j].setVisibility(View.INVISIBLE);
            }
        }




    }

    public void dropChess(){
        for(int i=0;i<7;i++){
            final int j=i;

            col[j].setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    dropChess(j);
                }
            });
        }
    }

    public void dropChess(int col){

        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ScreenHight = dm.heightPixels;
        int ScreenWidth = dm.widthPixels;

        if(isWin==false) {
            ImageView turnView1=(ImageView)findViewById(R.id.turnview1);
            ImageView turnView2=(ImageView)findViewById(R.id.turnview2);
            if (colIndex[col]>=0&&colIndex[col]<6) {
                if (isFirst == true) {
                    chess[colIndex[col]][col].setImageResource(R.drawable.hordchess);
                    chess[colIndex[col]][col].setVisibility(View.VISIBLE);
                    boardArray[colIndex[col]][col] = 1;
                    Judge(colIndex[col], col, boardArray);
                    saveState(col,boardArray[colIndex[col]][col]);
                    isFirst = false;
                    turnView1.setImageResource(R.drawable.hordchess);
                    turnView2.setImageResource(R.drawable.alliancechess_win);

                    colIndex[col]--;

                } else {
                    chess[colIndex[col]][col].setImageResource(R.drawable.alliancechess);
                    chess[colIndex[col]][col].setVisibility(View.VISIBLE);

                    boardArray[colIndex[col]][col] = 2;
                    Judge(colIndex[col], col, boardArray);
                    saveState(col,boardArray[colIndex[col]][col]);
                    isFirst = true;
                    turnView1.setImageResource(R.drawable.hordchess_win);
                    turnView2.setImageResource(R.drawable.alliancechess);
                    colIndex[col]--;
                }

            }
        }
    }

    public int winner(int i) {
        int winnerID;
        winnerID=i;
        isWin=true;
        setAlert(winnerID);
        return  winnerID;
    }

    public void Judge(int row,int col,int boardArray[][]) {
        int rowChange=0,colChange=0;
        String judgeDirection;
        judgeDirection="horizontal";
        rowChange=0;
        colChange=1;
        Judge(row,col,rowChange,colChange,boardArray);
        judgeDirection="vertical";
        rowChange=1;
        colChange=0;
        Judge(row,col,rowChange,colChange,boardArray);
        judgeDirection="obliquerightdown";
        rowChange=1;
        colChange=1;
        Judge(row,col,rowChange,colChange,boardArray);
        judgeDirection="obliquerightup";
        rowChange=1;
        colChange=-1;
        Judge(row,col,rowChange,colChange,boardArray);
        }

        public void Judge(int row, int col,int rowChange,int colChange,int boardArray[][]){

            TextView winNumone = (TextView)findViewById(R.id.winNum1);
            TextView winNumtwo = (TextView)findViewById(R.id.winNum2);

            DisplayMetrics dm =new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int ScreenHight = dm.heightPixels;
            int ScreenWidth = dm.widthPixels;

            int potentialWinchessNum =0;
            ImageView potentialWinChess[] = new ImageView[10];
            int pWCIndex = 0;
            for(int i =row+rowChange,j=col+colChange;i<6&&j<7&i>=0&j>=0;i=i+rowChange,j=j+colChange)
            {

                if(boardArray[i][j]==boardArray[row][col])
                {
                    potentialWinChess[pWCIndex++]=chess[i][j];
                    potentialWinchessNum++;
                }
                else break;
            }
            for(int i =row,j=col;i<6&&j<7&i>=0&j>=0;i=i-rowChange,j=j-colChange)
            {
                if(boardArray[i][j]==boardArray[row][col])
                {
                    potentialWinChess[pWCIndex++]=chess[i][j];
                    potentialWinchessNum++;
                }
                else break;
            }
            if (potentialWinchessNum >=4)
            {

                if (boardArray[row][col] == 1) {

                    winNumFirst++;
                    winNumone.setText(winNumFirst+"");

                    for (int i = 0; i < potentialWinchessNum; i++) {
                        potentialWinChess[i].setImageResource(R.drawable.hordchess_win);
                        potentialWinChess[i].setVisibility(View.VISIBLE);
                        Toast toast=Toast.makeText(getApplicationContext(),"TRIBE has Win !!!!!!!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if(boardArray[row][col]==2){

                    winNUmSecend++;
                    winNumtwo.setText(winNUmSecend+"");

                    for (int i = 0; i < potentialWinchessNum; i++) {
                        potentialWinChess[i].setImageResource(R.drawable.alliancechess_win);
                        potentialWinChess[i].setVisibility(View.VISIBLE);
                        Toast toast=Toast.makeText(getApplicationContext(),"ALLIANCE has Win !!!!!!!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    System.out.printf("drop wrong chess");
                }
                winner(boardArray[row][col]);

            }
        }

    public void saveState(int j,int chessState) {
        if (stackIndex <= 41) {
            stateStack[stackIndex++]= chessState*10+j;
        }
        if(stackIndex==42){
            Toast toast=Toast.makeText(getApplicationContext(),"Draw!!!!!!!!!!!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void backStep(){



        Button backButton=(Button)findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                DisplayMetrics dm =new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int ScreenHight = dm.heightPixels;
                int ScreenWidth = dm.widthPixels;

                if(isWin==false) {
                    if (stackIndex > 0) {
                        ImageView turnView1=(ImageView)findViewById(R.id.turnview1);
                        ImageView turnView2=(ImageView)findViewById(R.id.turnview2);

                        int laststate = stateStack[--stackIndex];
                        stateStack[stackIndex] = 0;
                        int chessState = laststate / 10;
                        int j = laststate % 10;
                        colIndex[j] = colIndex[j] + 1;
                        chess[colIndex[j]][j].setVisibility(View.INVISIBLE);
                        boardArray[colIndex[j]][j] = 0;

                        if (chessState - 1 == 0) {
                            isFirst = true;
                            turnView1.setImageResource(R.drawable.hordchess_win);
                            turnView2.setImageResource(R.drawable.alliancechess);
                        } else {
                            isFirst = false;
                            turnView1.setImageResource(R.drawable.hordchess);
                            turnView2.setImageResource(R.drawable.alliancechess_win);
                        }
                    }
                }
            }
        });
    }
    public void setAlert(int winner){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(winner+"wins!!!")
               .setMessage("Do you want play again?");
    }
    public void endGame(){
        Button endButton = (Button)findViewById(R.id.gameover);
        endButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                reset();
//                Intent gameover = new Intent();
//                setResult(RESULT_OK,gameover);
//                finish();
            }
        });

    }
}
