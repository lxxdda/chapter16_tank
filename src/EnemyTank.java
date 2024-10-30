public class EnemyTank extends Tank implements Runnable {

    public EnemyTank(int x, int y, int direct,int clock) {
        super(x, y, direct);
        setClock(clock);
        //初始化敌方坦克
        setType(1);
        setBulletType(0);//子弹类型
        //初始化一颗子弹
        //fire();
        //setSpeed(0);
    }

    public void randomChangeDirect() {//随机的改变坦克方向 0-3,为0的概率只有一半
        int tmp = (int) (Math.random() * 7 + 1);//[1,8)
        tmp = tmp / 2;
        setDirect(tmp);
    }

    @Override
    public void run() {
        //敌方坦克移动逻辑
        while (getHp() > 0) {
            //根据坦克的方向来继续移动
            switch (getDirect()) {
                case 0:  //向上
                    //让坦克保持一个方向，走10-30步
                    for (int i = 0; i < (int) (Math.random() * 20 + 10); i++) {
                        boolean moveSuccess=true;
                        for (int j = 0; j < getSpeed(); j++) {//按速度一步一步移动
                            moveSuccess=moveUp(1);
                            if(!moveSuccess)break;
                        }

                        if (!moveSuccess) {//碰到边界变换方向
                            randomChangeDirect();
                            break;
                        }
                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:  //向右
                    for (int i = 0; i < (int) (Math.random() * 20 + 10); i++) {
                        boolean moveSuccess=true;
                        for (int j = 0; j < getSpeed(); j++) {//按速度一步一步移动
                            moveSuccess=moveRight(1);
                            if(!moveSuccess)break;
                        }

                        if (!moveSuccess) {//碰到边界变换方向
                            randomChangeDirect();
                            break;
                        }

                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:  //向下
                    for (int i = 0; i < (int) (Math.random() * 20 + 10); i++) {
                        boolean moveSuccess=true;
                        for (int j = 0; j < getSpeed(); j++) {//按速度一步一步移动
                            moveSuccess=moveDown(1);
                            if(!moveSuccess)break;
                        }

                        if (!moveSuccess) {//碰到边界变换方向
                            randomChangeDirect();
                            break;
                        }


                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:  //向左
                    for (int i = 0; i < (int) (Math.random() * 20 + 10); i++) {
                        boolean moveSuccess=true;
                        for (int j = 0; j < getSpeed(); j++) {//按速度一步一步移动
                            moveSuccess=moveLeft(1);
                            if(!moveSuccess)break;
                        }

                        if (!moveSuccess) {//碰到边界变换方向
                            randomChangeDirect();
                            break;
                        }

                        //休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

            //然后随机的改变坦克方向
            randomChangeDirect();
        }
    }
}
