import java.io.Serializable;
import java.util.Vector;

public class Tank implements Serializable {
    private int x;
    private int y;
    private int bulletType = 0;//武器类型,0为Shot，1为laser
    private int type = 1;//0为自己1为敌人
    private int clock = 0;
    private int maxShotNum = 5;//最大子弹数量
    private int hp = 1;
    private int direct;//坦克方向 0 上1 右 2下 3左
    private int speed = 10;
    private Vector<Bullet> bullets = new Vector<>();

    public Tank(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;

    }

    public Rectangle getFrame() {
        if (direct == 0 || direct == 2) {
            return new Rectangle(x - 20, y - 30, x + 20, y + 30);
        } else {
            return new Rectangle(x - 30, y - 20, x + 30, y + 20);
        }

    }

    public boolean fire() {
        if (bullets.size() < maxShotNum) {
            switch (bulletType) {
                case 0:
                    Shot shot = new Shot(x, y, direct, type);
                    bullets.add(shot);
                    new Thread(shot).start();
                    break;
                case 1:
                    Laser laser = new Laser(x, y, direct, type);
                    bullets.add(laser);
                    new Thread(laser).start();
                    break;
            }
            return true;
        }
        return false;
    }

    public boolean moveUp(int step) {
        clock++;
        if(type==1){//敌人每300步开火
            if(clock%300==1)fire();
        }
        Rectangle thisFrame = getFrame().offsetY(-1);//移动后的矩形框
        for (int i = 0; i < GameMap.allTanks.size(); i++) {
            //判断是否与其他坦克碰撞
            Tank aTank = GameMap.allTanks.get(i);
            if (this != aTank) {
                if (Tools_.squareCollide(thisFrame, aTank.getFrame())) return false;
            }
        }
        if (y - 30 > 0) {//判断边界
            y -= step;
            return true;
        }
        return false;
    }

    public boolean moveDown(int step) {
        clock++;
        if(type==1){//敌人每300步开火
            if(clock%300==1)fire();
        }
        Rectangle thisFrame = getFrame().offsetY(1);//移动后的矩形框
        for (int i = 0; i < GameMap.allTanks.size(); i++) {
            //判断是否与其他坦克碰撞
            Tank aTank = GameMap.allTanks.get(i);
            if (this != aTank) {
                if (Tools_.squareCollide(thisFrame, aTank.getFrame())) return false;
            }
        }
        if (y + 30 < 750) {
            y += step;
            return true;
        }
        return false;
    }

    public boolean moveRight(int step) {
        clock++;
        if(type==1){//敌人每300步开火
            if(clock%300==1)fire();
        }
        Rectangle thisFrame = getFrame().offsetX(1);//移动后的矩形框
        for (int i = 0; i < GameMap.allTanks.size(); i++) {
            //判断是否与其他坦克碰撞
            Tank aTank = GameMap.allTanks.get(i);
            if (this != aTank) {
                if (Tools_.squareCollide(thisFrame, aTank.getFrame())) return false;
            }
        }
        if (x + 30 < 1000) {
            x += step;
            return true;
        }
        return false;
    }

    public boolean moveLeft(int step) {
        clock++;
        if(type==1){//敌人每300步开火
            if(clock%300==1)fire();
        }
        Rectangle thisFrame = getFrame().offsetX(-1);//移动后的矩形框
        for (int i = 0; i < GameMap.allTanks.size(); i++) {
            //判断是否与其他坦克碰撞
            Tank aTank = GameMap.allTanks.get(i);
            if (this != aTank) {
                if (Tools_.squareCollide(thisFrame, aTank.getFrame())) return false;
            }
        }
        if (x - 30 > 0) {
            x -= step;
            return true;
        }
        return false;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getMaxShotNum() {
        return maxShotNum;
    }

    public void setMaxShotNum(int maxShotNum) {
        this.maxShotNum = maxShotNum;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(Vector<Bullet> bullets) {
        this.bullets = bullets;
    }


}
