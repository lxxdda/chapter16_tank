public class Shot extends Bullet implements Runnable {

    public Shot(int x, int y, int direct,int type) {//传入坦克位置,转化为子弹位置
        this.direct = direct;
        this.type=type;
        setLength(2);
        setHalfWidth(1);
        switch (direct) {
            case 0:
                this.x = x;
                this.y = y - 30;
                break;
            case 1:
                this.x = x + 30;
                this.y = y;
                break;
            case 2:
                this.x = x;
                this.y = y + 30;
                break;
            case 3:
                this.x = x - 30;
                this.y = y;
                break;
        }

    }
    @Override
    public boolean hitTank(Tank tank) {//击中坦克
        switch (tank.getDirect()) {
            case 0://上
            case 2://下,子弹坐标碰撞
                if (x + 1 > tank.getX() - 20 && x - 1 < tank.getX() + 20 && y + 1 > tank.getY() - 30 && y - 1 < tank.getY() + 30) {
                    if(type!=tank.getType()){//子弹类型和坦克类型不一样，坦克才掉血
                        tank.setHp(tank.getHp() - power);
                    }
                    hp--;
                    return true;
                }
                break;
            case 1://右
            case 3://左
                if (x + 1 > tank.getX() - 30 && x - 1 < tank.getX() + 30 && y + 1 > tank.getY() - 20 && y - 1 < tank.getY() + 20) {
                    if(type!=tank.getType()){//子弹类型和坦克类型不一样，坦克才掉血
                        tank.setHp(tank.getHp() - power);
                    }
                    hp--;
                    return true;
                }
                break;

        }
        return false;
    }

    @Override
    public void run() {
        while (hp>0) {
           // System.out.println("子弹的位置x" + x + "y" + y);
            switch (direct) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    x += speed;
                    break;
                case 2:
                    y += speed;
                    break;
                case 3:
                    x -= speed;
                    break;
                default:
                    break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750)) {
                hp = 0;
                break;
            }
        }
    }
}
