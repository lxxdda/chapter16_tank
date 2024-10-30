import jdk.nashorn.internal.ir.CallNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;

    //物体实际在GameMap里
    Vector<Bullet> noMasterBullets = null;//没有主人的Shot子弹
    Vector<Tank> allTanks = null;
    int enemyTankSize = 0;//初始敌人数量
    Vector<Bomb> bombs = new Vector<>();//需要绘制的炸弹
    //初始化爆炸效果的三张图片
    /*
    Image image1 = Toolkit.getDefaultToolkit().getImage("E:\\ProgrammingApp\\Java\\JavaCode\\studyProject\\chapter16_tank\\src\\bomb_1.gif");
    Image image2 = Toolkit.getDefaultToolkit().getImage("E:\\ProgrammingApp\\Java\\JavaCode\\studyProject\\chapter16_tank\\src\\bomb_2.gif");
    Image image3 = Toolkit.getDefaultToolkit().getImage("E:\\ProgrammingApp\\Java\\JavaCode\\studyProject\\chapter16_tank\\src\\bomb_3.gif");
     */
    File f1 = new File("src\\bomb_1.gif");
    File f2 = new File("src\\bomb_2.gif");
    File f3 = new File("src\\bomb_3.gif");
    BufferedImage image1 = ImageIO.read(f1);
    BufferedImage image2 = ImageIO.read(f2);
    BufferedImage image3 = ImageIO.read(f3);


    public MyPanel(String key) throws IOException, ClassNotFoundException {
        //初始化
        hero=GameMap.hero;
        noMasterBullets = GameMap.noMasterBullets;//没有主人的Shot子弹
        allTanks = GameMap.allTanks;
        enemyTankSize = GameMap.enemyTankSize;//初始敌人数量
        switch(key){
            case "0"://新游戏
                allTanks.add(hero);
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank(500, i * 100 + 100, 1,0);
                    new Thread(enemyTank).start();
                    allTanks.add(enemyTank);
                }
                break;
            case "1"://继续上局游戏
                GameMap.getRecord();
                hero=GameMap.hero;
                System.out.println(GameMap.killedEnemyTankNum);
                break;
            default:
                System.out.println("错误的输入");
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, GameMap.mapWidth, GameMap.mapHeight);
        showInfo(g);
        //画所有坦克
        for (int i = allTanks.size() - 1; i >= 0; i--) {
            Tank aTank = allTanks.get(i);
            //画一个坦克的所有子弹
            for (int j = aTank.getBullets().size() - 1; j >= 0; j--) {
                //取出一颗子弹
                Bullet bullet = aTank.getBullets().get(j);
                if (bullet.getHp() > 0) {
                    drawBullet(bullet, g);
                    //判断子弹是否击中任意坦克
                    for (int k = 0; k < allTanks.size(); k++) {
                        Tank otherTank = allTanks.get(k);
                        if (bullet.hitTank(otherTank)) {//子弹与一个坦克碰撞

                        }
                    }
                } else aTank.getBullets().remove(j);//移除死亡的子弹
            }

            if (aTank.getHp() > 0) {
                drawTank(aTank.getX(), aTank.getY(), g, aTank.getDirect(), aTank.getType());
            } else {
                //移除死亡的坦克
                Bomb bomb = new Bomb(aTank.getX(), aTank.getY());
                bombs.add(bomb);//加入bombs等待绘制爆炸
                if (aTank.getBullets().size() > 0) {
                    //死亡的坦克有Shot子弹存活，创建无主人Shot子弹代替
                    for (int j = 0; j < aTank.getBullets().size(); j++) {
                        if (aTank.getBulletType() == 0) {//只替代Shot子弹
                            Bullet shot = aTank.getBullets().get(j);
                            //shot=(Shot)shot;
                            Shot copyShot = new Shot(shot.getX(), shot.getY(), shot.getDirect(), shot.getType());
                            noMasterBullets.add(copyShot);
                            new Thread(copyShot).start();
                        }

                    }
                }
                if(aTank instanceof EnemyTank){//移除敌人时击杀数加1
                    GameMap.killedEnemyTankNum++;
                }
                allTanks.remove(aTank);
                System.out.println(GameMap.killedEnemyTankNum);
            }
        }

        //画无主人子弹
        for (int i = noMasterBullets.size() - 1; i >= 0; i--) {
            //取出一颗子弹
            Bullet bullet = noMasterBullets.get(i);
            if (bullet.getHp() > 0) {
                drawBullet(bullet, g);
                //判断子弹是否击中任意坦克
                for (int k = 0; k < allTanks.size(); k++) {
                    Tank otherTank = allTanks.get(k);
                    if (bullet.hitTank(otherTank)) {//子弹与一个坦克碰撞

                    }
                }
            } else noMasterBullets.remove(i);//移除死亡的子弹
        }


        //显示爆炸效果
        for (int i = bombs.size() - 1; i >= 0; i--) {
            //取出炸弹
            Bomb bomb = bombs.get(i);
            //根据当前这个bomb对象的life值去画出对应的图片
            if (bomb.getLife() > 6) {
                g.drawImage(image1, bomb.getX() - 20, bomb.getY() - 20, 40, 40, this);
            } else if (bomb.getLife() > 3) {
                g.drawImage(image2, bomb.getX() - 20, bomb.getY() - 20, 40, 40, this);
            } else {
                g.drawImage(image3, bomb.getX() - 20, bomb.getY() - 20, 40, 40, this);
            }
            //让这个炸弹的生命值减少
            bomb.lifeDown();
            //如果bomb life 为0, 就等待从bombs中删除
            if (bomb.getLife() <= 0) {
                bombs.remove(bomb);
            }
        }

        //复活
        if (hero.getHp() <= 0) {
            System.out.println(hero.hashCode());
            GameMap.reLive();
            hero=GameMap.hero;
        }

    }

    /**
     * @param x      坦克的左上角x坐标
     * @param y      坦克的左上角y坐标
     * @param g      画笔
     * @param direct 坦克方向（上下左右）
     * @param type   坦克类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        if (type == 0)//0为自己
            g.setColor(Color.cyan);
        else g.setColor(Color.yellow);

        switch (direct) {
            case 0: //表示向上
                g.fill3DRect(x - 20, y - 30, 10, 60, false);//画出坦克左边轮子
                g.fill3DRect(x + 10, y - 30, 10, 60, false);//画出坦克右边轮子
                g.fill3DRect(x - 10, y - 20, 20, 40, false);//画出坦克盖子
                g.fillOval(x - 10, y - 10, 20, 20);//画出圆形盖子
                g.drawLine(x, y, x, y - 30);//画出炮筒
                g.drawLine(x + 1, y, x + 1, y - 30);//画出炮筒
                g.drawLine(x - 1, y, x - 1, y - 30);//画出炮筒
                break;
            case 1: //表示向右
                g.fill3DRect(x - 30, y - 20, 60, 10, false);//画出坦克上边轮子
                g.fill3DRect(x - 30, y + 10, 60, 10, false);//画出坦克下边轮子
                g.fill3DRect(x - 20, y - 10, 40, 20, false);//画出坦克盖子
                g.fillOval(x - 10, y - 10, 20, 20);//画出圆形盖子
                g.drawLine(x, y, x + 30, y);//画出炮筒
                g.drawLine(x, y + 1, x + 30, y + 1);//画出炮筒
                g.drawLine(x, y - 1, x + 30, y - 1);//画出炮筒
                break;
            case 2: //表示向下
                g.fill3DRect(x - 20, y - 30, 10, 60, false);//画出坦克左边轮子
                g.fill3DRect(x + 10, y - 30, 10, 60, false);//画出坦克右边轮子
                g.fill3DRect(x - 10, y - 20, 20, 40, false);//画出坦克盖子
                g.fillOval(x - 10, y - 10, 20, 20);//画出圆形盖子
                g.drawLine(x, y, x, y + 30);//画出炮筒
                g.drawLine(x + 1, y, x + 1, y + 30);//画出炮筒
                g.drawLine(x - 1, y, x - 1, y + 30);//画出炮筒
                break;
            case 3: //表示向左
                g.fill3DRect(x - 30, y - 20, 60, 10, false);//画出坦克上边轮子
                g.fill3DRect(x - 30, y + 10, 60, 10, false);//画出坦克下边轮子
                g.fill3DRect(x - 20, y - 10, 40, 20, false);//画出坦克盖子
                g.fillOval(x - 10, y - 10, 20, 20);//画出圆形盖子
                g.drawLine(x, y, x - 30, y);//画出炮筒
                g.drawLine(x, y + 1, x - 30, y + 1);//画出炮筒
                g.drawLine(x, y - 1, x - 30, y - 1);//画出炮筒
                break;
            default:
                System.out.println("暂时没有处理");
        }

    }

    public void drawBullet(Bullet bullet, Graphics g) {
        //System.out.println("绘制子弹");
        g.setColor(Color.RED);
        if (bullet.getClass().getSimpleName() == "Shot") {
            g.fillOval(bullet.getX() - 1, bullet.getY() - 1, 3, 3);
        } else if (bullet.getClass().getSimpleName() == "Laser") {

            int halfWidth = ((Laser) bullet).getHalfWidth(), length = ((Laser) bullet).getLength();
            switch (bullet.getDirect()) {
                case 0:
                    if (bullet.getY() - length < 0) length = bullet.getY();//不画边界外的子弹
                    g.fillRect(bullet.getX() - halfWidth, bullet.getY() - length, halfWidth * 2, length);
                    break;
                case 1:
                    if (bullet.getX() + length > 1000) length = 1000 - bullet.getX();
                    g.fillRect(bullet.getX(), bullet.getY() - halfWidth, length, halfWidth * 2);
                    break;
                case 2:
                    if (bullet.getY() + length > 750) length = 750 - bullet.getY();
                    g.fillRect(bullet.getX() - halfWidth, bullet.getY(), halfWidth * 2, length);
                    break;
                case 3:
                    if (bullet.getX() - length < 0) length = bullet.getX();
                    g.fillRect(bullet.getX() - length, bullet.getY() - halfWidth, length, halfWidth * 2);
            }

        }


    }

    //编写方法，显示我方击毁敌方坦克的信息
    public void showInfo(Graphics g) {

        //画出玩家的总成绩
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);

        g.drawString("您累积击毁敌方坦克", 1020, 30);
        drawTank(1040, 90, g, 0, 1);//画出一个敌方坦克
        g.setColor(Color.BLACK);//这里需要重新设置成黑色
        g.drawString(GameMap.killedEnemyTankNum + "", 1080, 100);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            //System.out.println("W");
            hero.setDirect(0);
            for (int i = 0; i < hero.getSpeed(); i++) {
                hero.moveUp(1);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            //System.out.println("A");
            hero.setDirect(3);
            for (int i = 0; i < hero.getSpeed(); i++) {
                hero.moveLeft(1);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            //System.out.println("S");
            hero.setDirect(2);
            for (int i = 0; i < hero.getSpeed(); i++) {
                hero.moveDown(1);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            //System.out.println("D");
            hero.setDirect(1);
            for (int i = 0; i < hero.getSpeed(); i++) {
                hero.moveRight(1);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_J) {
            hero.fire();
        }

        //让面板重绘
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {//每隔一段时间绘制区域，实现屏幕刷新
        while (true) {
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GameMap.clock++;
            if (GameMap.clock % 100==0) {//后续增加敌人
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank(500, i * 100 + 100, 1,0);
                    new Thread(enemyTank).start();
                    allTanks.add(enemyTank);
                }
            }


            this.repaint();
        }
    }
}
