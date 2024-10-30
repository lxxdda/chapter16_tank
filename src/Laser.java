import javax.tools.Tool;

public class Laser extends Bullet implements Runnable {

    public Laser(int x, int y, int direct, int type) {
        setPower(10);
        setHalfWidth(10);
        setLength(600);
        this.direct = direct;
        this.type = type;
        switch (direct) {
            case 0:
                this.x = x;
                this.y = y - 35;
                break;
            case 1:
                this.x = x + 35;
                this.y = y;
                break;
            case 2:
                this.x = x;
                this.y = y + 35;
                break;
            case 3:
                this.x = x - 35;
                this.y = y;
                break;
        }
    }

    @Override
    public boolean hitTank(Tank tank) {
        Rectangle laserFrame=null;
        switch(getDirect()){
            //激光的方向
            case 0:
                laserFrame=new Rectangle(x-halfWidth,y - length,x + halfWidth,y);
                break;
            case 1:
                laserFrame=new Rectangle(x,y - halfWidth,x + length,y+halfWidth);
                break;
            case 2:
                laserFrame=new Rectangle(x-halfWidth,y,x + halfWidth,y+length);
                break;
            case 3:
                laserFrame=new Rectangle(x-length,y - halfWidth,x ,y+halfWidth);
                break;
        }

        if (Tools_.squareCollide(laserFrame,tank.getFrame())) {
            System.out.println(laserFrame);
            tank.setHp(tank.getHp() - power);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while(halfWidth>0){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            halfWidth--;
            if(halfWidth<=0){
                setHp(0);
            }
        }
    }


}
