# **Connect Four Game**

This is an android four chess game developed on android studio 2.2.2 platform. And the emulator
is Nexus 5 with API 22.

## **How to run**

Input the whole project, choose MainActivity.java file and click run button. After choosing the emlator
(no limited to Nexus 5, the screen will automatically adjust to fit the screen size), the stratgame page 
should show as below.

![Start Page](./drawable/startgamepage.png)

>`LOK-TAR OGAR` : Start Game Button(if you are a big world of warcraft fan, you will definitely know its meaning)

>`student name` : name of developer for identification

>`student number` : number of developer

After you click the `LOK_TAR OGAR` button it will change to the game page as below.

![Game Page](./drawable/gamepage.png)

In this page, you can begin your game by click each column to place the chess.

>`chess on the bottom` : show the player next turn

>`back` : button to go back one previous step

>`restart` : button to restart the game at any time

## **Details of function**

This part will explain main module and function of this game

### **Layout**

The **static layout** is mainly defined in two xml files : [_`activity_game`_](./layout/activity_game.xml) and [_`activity_main.xml`_](./layout/activity_main.xml)

_`activity_main`_ uses _RelativeLayout_ , and uses _TextView_ to show `student name` and `student number`, _Button_ to start game.

_`activity_game`_ uses _RelativeLayout_ as main layout, _RelativeLayout_ to construct outline of chessboard ,  _ImageView_ to store `chess at the bottom` , _Button_ to create `back` and `restart` button.

The **dynamic layout** is mainly defined in [_`Game.java`_](./Game.java)


>for (int i=0;i<7;i++)  
>{    
>LinearLayout linearlayout = new LinearLayout(this);  
>linearlayout.setId(i);  
>linearlayout.setOrientation(LinearLayout.VERTICAL);  
>linearlayout.setX(ScreenWidth/7*i);  
>linearlayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenWidth/7,ScreenWidth/7*6+ScreenWidth/28));  
>linearlayout.setBackgroundResource(R.drawable.linearlayout);  
>col[i]=linearlayout;  
>relativeLayout.addView(col[i]);  
>}

Uses _for_ loop and _LinearLayout_ to construct 7 column of chessboard.

>for(int j =0;j<7;j++)  
>{  
>for(int i =0;i<6;i++)  
>{  
>boardArray[i][j]=0;  
>ImageView imageview=new ImageView(this);  
>imageview.setY(ScreenWidth/7/20*i);  
>imageview.setLayoutParams(new ViewGroup.LayoutParams(ScreenWidth/7,ScreenWidth/7));  
>chess[i][j]=imageview;  
>}  
>}  

Uses _for_ loop and _ImageView_ to set 42 chess and uses _boardArray_ to store each chess's state.

### **MainActivity.java**

#### LOK_TAR OGAR

This button defined in [`MainActivity.java`][MainActivity path] is used to change to game page by using _OnClick_ and _Intent_ Function.
>Button startButton = (Button) findViewById(R.id.start);  
>startButton.setOnClickListener(new View.OnClickListener() 
>{  
>public void onClick(View view) {  
>Intent myIntent = new Intent(view.getContext(), Game.class);  
>startActivityForResult(myIntent, 0);  
>}  
>});  

[MainActivity path]:./MainActivity.java

### **Game.java**

#### DEFAULT STATIC SETTING

>LinearLayout col[]=new LinearLayout[7];  
>ImageView chess[][]=new ImageView[6][7];  
>int [][]boardArray = new int[6][7];  

`col[]` is used to store column of chessboard, `chess[][]` is used to store chess, its _i_ represents the number of line, and _j_ represents the number of
column, `boardArray[][]` represents each chess's state. 

>boolean isFirst = true;  
>boolean isWin = false;

`isFirst` is a flag to show if current turn is the firstplayer, `isWin` is a flag to show the state of the game.

>int colIndex[]=new int[7];

Since in this game, each chess will appear from the bottom of each column, `colIndex[]` is used to store the current position of chess 
in each column. Its initial value is 0, and after each droppin, it will increase 1.

>int stateStack[]=new int[42];  
>int stackIndex = 0;

`stateStack[]` is a stack to store each dropped chess in order, and `stackIndex` is the index of this stack. This two variables is used to 
achieve back function.

#### initChessBoard

>public void initChessBoard(){  
>DisplayMetrics dm =new DisplayMetrics();  
>getWindowManager().getDefaultDisplay().getMetrics(dm);  
>int ScreenHight = dm.heightPixels;  
>int ScreenWidth = dm.widthPixels;  

>RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.cheeseboard);  
>relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,ScreenHight/8*6));  
>relativeLayout.setY(ScreenHight/16*4);  
>}  

In this part, using DisplayMetrics to get the size of phone's screen size. `ScreenHight` is the height of the screen and `ScreenWidth` is the width of the screen.
`relativeLayout` is the outline of the chessboard.

#### drop chess

This part realizes dropchess function.

>for(int i=0;i<7;i++)  
>final int j=i;  
>col[j].setOnClickListener(new View.OnClickListener(){  
>public void onClick(View view){  
>dropChess(j);  
>}});}}  

`dropChess` function sets _OnClickListener_ for each column and when click column,calls `dropChess(j)` fuction.
  
>if(isWin==false)  
>if (colIndex[col]>=0&&colIndex[col]<6)  
>if (isFirst == true)  
>chess[colIndex[col]][col].setImageResource(R.drawable.hordchess);  
>chess[colIndex[col]][col].setVisibility(View.VISIBLE);  
>boardArray[colIndex[col]][col] = 1; 
>colIndex[col]--;   
>saveState(col,boardArray[colIndex[col]][col]);  
>Judge(colIndex[col], col, boardArray);  
>isFirst = false;    


In `dropChess(j)` function, `isWin` is used to judge the game state, then use `colIndex` to judge the number of chess in each column, if the column 
is not full, then drop the chess. `isFirst` is used to judge the current turn, _setImageResource_ and _setVisibility_is used to show chess picture. 
And after drop chess, change the value of boardArray to change state of chess and this column decreases number, save the dropped chess state for `back`. Each time after dropping chess, 
we need to judge in this state, can one of player wins. And after judging, change drop turn.

#### Judge

This part realizes judge fuction after each dropping. In this game, we need to judge the vertical, horizontal, and lean. 
The difference between vertical judge, horizontal judege, lean judge is the distance in each search step. In vertical, line number increases/decreases 1 each time and column number keeps unchange.
In horizontal, line number keeps unchange and column number increases/decreases 1 each time. In lean, both line number and column number increase/decrease 1 or line number increase/decrease and column 
number decreases/increases 1 each time.

>for(int i =row+rowChange,j=col+colChange;i<6&&j<7&i>=0&j>=0;i=i+rowChange,j=j+colChange)  
>if(boardArray[i][j]==boardArray[row][col]){}  
>potentialWinChess[pWCIndex++]=chess[i][j];  
>potentialWinchessNum++;}  
>else break;  
>for(int i =row,j=col;i<6&&j<7&i>=0&j>=0;i=i-rowChange,j=j-colChange){  
>if(boardArray[i][j]==boardArray[row][col])  
>potentialWinChess[pWCIndex++]=chess[i][j];  
>potentialWinchessNum++;}  
>else break;  

`potentialWinChess` is used to store potential Win Chess, when it over 4, it means someone wins. 

>if (potentialWinchessNum >=4){  
>if (boardArray[row][col] == 1) {  
>for (int i = 0; i < potentialWinchessNum; i++) {  
>potentialWinChess[i].setImageResource(R.drawable.hordchess_win);  
>potentialWinChess[i].setVisibility(View.VISIBLE);  
>Toast toast=Toast.makeText(getApplicationContext(),"TRIBE has Win !!!!!!!",Toast.LENGTH_SHORT);  
>toast.show();}}   
>else if(boardArray[row][col]==2){  
>for (int i = 0; i < potentialWinchessNum; i++) {  
>potentialWinChess[i].setImageResource(R.drawable.alliancechess_win);  
>potentialWinChess[i].setVisibility(View.VISIBLE);  
>Toast toast=Toast.makeText(getApplicationContext(),"ALLIANCE has Win !!!!!!!",Toast.LENGTH_SHORT);  
>toast.show();}}  

#### BackStep

When one player drops a wrong chess, he can click back button to go back previous state. 
Before going back, it needs to save state first.

>if (stackIndex <= 42) {  
>stateStack[stackIndex++]= chessState*10+j;}

Using `stateStack` to save each chess state, in the stack, each chess's column and the value of boardArray will 
be recorded. **_stateStack = boardArray value * 100 + column number_** and then each value will be stored in stateStack.

And when the stackIndex number goes over 42, it means all chesses have been put into stack, which means chessboard is filled.  

>else{  
>Toast toast=Toast.makeText(getApplicationContext(),"Draw!!!!!!",Toast.LENGTH_SHORT);  
>toast.show();}   

In this situation, no one wins.  

After saving each dropped chess, when _`back`_ button is clicked, pop out one chess and go back to previous state.

>backButton.setOnClickListener(new View.OnClickListener(){  
>public void onClick(View view){  
>if(isWin==false) {  
>if (stackIndex > 0){    
>int laststate = stateStack[--stackIndex];  
>stateStack[stackIndex] = 0;  
>int chessState = laststate / 10;  
>int j = laststate % 10;  
>colIndex[j] = colIndex[j] + 1;  
>chess[colIndex[j]][j].setVisibility(View.INVISIBLE);  
>boardArray[colIndex[j]][j] = 0;  
>if (chessState - 1 == 0) {  
>isFirst = true;    
>else {  
>isFirst = false;  
>}}}}});}  

#### restart

When _`restart game`_ button is clicked, it will call `reset` function.
In this function, the state of the whole chess will be initialized again.

>isFirst = true;  
>isWin = false;  
>stackIndex = 0;  
>for(int j=0;j<7;j++){  
>colIndex[j]=5;  
>for(int i=0;i<6;i++){  
>stateStack[i*7+j]=0;  
>boardArray[i][j]=0;  
>chess[i][j].setVisibility(View.INVISIBLE);}}  

## **Forward**

This game's UI can be improved more, such as add some animation and some music.

## **Reference**
[Android offcial API guidence](https://developer.android.com/guide/index.html)  
[Hua Ban](http://huaban.com/)  
[Startgamepage picture](http://wow.17173.com/content/2013-05-17/20130517144519165.shtml)  
[chessboard piture](https://www.walldevil.com/17603-horde-world-of-warcraft.html)  
[chess one piture](http://i1.download.fd.pchome.net/t_600x1024/g1/M00/0C/05/ooYBAFROFiKIDlm-AAaDcyu6uIkAACCTgJq990ABoOL403.jpg)  
[chess two picture](https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSG8U1z98xeIMD6qasDPzPTfwVPG6GrNlXoAaPvDs4XToXY84vN, )  
[top panel picture](http://i2.17173.itc.cn/2013/wow/2013/06/03/the_alliance___1080p_edition__free_download__by_kobashihd-d66ka2y.png)  