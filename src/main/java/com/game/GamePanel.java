package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    int length; //蛇的长度
    int[] snakeX = new int[600];  //蛇的坐标x
    int[] snakeY = new int[500];  //蛇的坐标y
    String fx; //蛇的方向 ： R:右  L:左  U:上  D:下
    boolean isStart = false; //游戏是否开始
    Timer timer = new Timer(100, this); //定时器：第一个参数，就是定时执行时间
    //食物
    int food_x;
    int food_y;
    Random random = new Random();
    boolean isFail = false; //游戏是否结束
    int score; //游戏分数！

    //构造方法
    public GamePanel(){
        init();//初始化
        this.setFocusable(true); //获取焦点事件
        this.addKeyListener(this); //键盘监听事件
        timer.start();
    }

    //初始化方法
    public void init(){
        length = 3;//初始小蛇有三节,包括小脑袋
        //初始化开始的蛇,给蛇定位,
        snakeX[0] = 100; snakeY[0] = 100;
        snakeX[1] = 75; snakeY[1] = 100;
        snakeX[2] = 50; snakeY[2] = 100;

        //初始化食物数据
        food_x = 25 + 25* random.nextInt(34);
        food_y = 75 + 25* random.nextInt(24);

        fx = "R";
        score = 0; //初始化游戏分数
    }

    //画组件
    public void paintComponent(Graphics g){
        super.paintComponent(g);//清屏
        this.setBackground(Color.WHITE); //设置面板的背景色
        Data.header.paintIcon(this,g,25,11); //绘制头部信息区域
        g.fillRect(25,75,850,600); //绘制游戏区域

        //把小蛇画上去
        switch (fx) {
            case "R":  //蛇的头通过方向变量来判断
                Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "L":
                Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "U":
                Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "D":
                Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
        }

        for (int i = 1; i < length; i++) {
            Data.body.paintIcon(this,g,snakeX[i],snakeY[i]); //蛇的身体长度根据length来控制
        }

        //画食物
        Data.food.paintIcon(this,g,food_x,food_y);

        g.setColor(Color.white);
        g.setFont(new Font("微软雅黑",Font.BOLD,18));
        g.drawString("长度 " + length,750,35);
        g.drawString("分数 " + score,750,50);

        //游戏提示
        if (!isStart){
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑",Font.BOLD,40));
            g.drawString("按下空格开始游戏!",300,300);
        }
        //失败判断
        if (isFail){
            g.setColor(Color.RED);
            g.setFont(new Font("微软雅黑",Font.BOLD,40));
            g.drawString("失败, 按下空格重新开始",200,300);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //键盘监听事件
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); //获取按下的键盘

        if (keyCode==KeyEvent.VK_SPACE){ //如果是空格
            if (isFail){ //如果游戏失败,从头再来！
                isFail = false;
                init(); //重新初始化
            }else { //否则，暂停游戏
                isStart = !isStart;
            }
            repaint();
        }

        //键盘控制走向
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
            fx = "L";
        }else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
            fx = "R";
        }else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            fx = "U";
        }else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            fx = "D";
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //定时执行的操作
    @Override
    public void actionPerformed(ActionEvent e) {
        //如果游戏处于开始状态，并且没有结束，则小蛇可以移动
        if (isStart && !isFail){
            //右移:即让后一个移到前一个的位置即可 !
            for (int i = length-1; i > 0; i--) { //除了脑袋都往前移：身体移动
                snakeX[i] = snakeX[i-1]; //即第i节(后一节)的位置变为(i-1：前一节)节的位置！
                snakeY[i] = snakeY[i-1];
            }
            //通过方向控制，头部移动
            switch (fx) {
                case "R":
                    snakeX[0] = snakeX[0] + 25;
                    if (snakeX[0] == snakeX[2]) {
                        fx = "L";
                        snakeX[0] = snakeX[0] - 50;
                        if (snakeX[0] < 25) snakeX[0] = 850;
                    }
                    if (snakeX[0] > 850) snakeX[0] = 25;
                    break;
                case "L":
                    snakeX[0] = snakeX[0] - 25;
                    if (snakeX[0] == snakeX[2]) {
                        fx = "R";
                        snakeX[0] = snakeX[0] + 50;
                        if (snakeX[0] > 850) snakeX[0] = 25;
                    }
                    if (snakeX[0] < 25) snakeX[0] = 850;
                    break;
                case "U":
                    snakeY[0] = snakeY[0] - 25;
                    if (snakeY[0] == snakeY[2]) {
                        fx = "D";
                        snakeY[0] = snakeY[0] + 50;
                        if (snakeY[0] > 650) snakeY[0] = 75;
                    }
                    if (snakeY[0] < 75) snakeY[0] = 650;
                    break;
                case "D":
                    snakeY[0] = snakeY[0] + 25;
                    if (snakeY[0] == snakeY[2]) {
                        fx = "U";
                        snakeY[0] = snakeY[0] - 50;
                        if (snakeY[0] < 75) snakeY[0] = 650;
                    }
                    if (snakeY[0] > 650) snakeY[0] = 75;
                    break;
            }

            //吃食物:当蛇的头和食物一样时,算吃到食物!
            if (snakeX[0]==food_x && snakeY[0]==food_y){
                //1.长度加一
                length++;
                //每吃一个食物，增加积分
                score = score + 10;
                //2.重新生成食物
                food_x = 25 + 25* random.nextInt(34);
                food_y = 75 + 25* random.nextInt(24);
            }

            //结束判断，头和身体撞到了
            for (int i = 1; i < length; i++) {
                //如果头和身体碰撞，那就说明游戏失败
                if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                    isFail = true;
                    break;
                }
            }

            repaint(); //需要不断的更新页面实现动画
        }
        timer.start();//让时间动起来!

    }

}
