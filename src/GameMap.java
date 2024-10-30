import java.io.*;
import java.util.Vector;

public class GameMap {
    public static Hero hero=new Hero(500, 500, 0);
    public static Vector<Tank> allTanks=new Vector<>();//所有坦克
    public static Vector<Bullet> noMasterBullets = new Vector<>();//没有主人的Shot子弹
    public static Vector<Tank> recordAllTanks;//接收读记录
    public static Vector<Bullet> recordNoMasterBullets ;//
    public static int mapHeight=750;
    public static int mapWidth=1000;
    public static int killedEnemyTankNum=0;//击杀敌人数量
    public static int enemyTankSize = 3;//初始敌人数量
    public static int clock=0;
    public static String filePath = "src\\myRecord.dat";

    //保存数据
    public static void keepRecord() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
        oos.writeInt(clock);
        oos.writeInt(killedEnemyTankNum);
        oos.writeObject(allTanks);
        oos.writeObject(noMasterBullets);
    }

    //加载数据
    public static void getRecord() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
        clock=ois.readInt();
        killedEnemyTankNum=ois.readInt();
        recordAllTanks=(Vector<Tank>) ois.readObject();
        recordNoMasterBullets=(Vector<Bullet>) ois.readObject();
        //根据得到的数据恢复场景
        for (int i = 0; i < recordAllTanks.size(); i++) {
            Tank aTank=recordAllTanks.get(i);
            if(aTank instanceof EnemyTank){
                EnemyTank enemyTank = new EnemyTank(aTank.getX(), aTank.getY(),aTank.getDirect(),aTank.getClock());
                new Thread(enemyTank).start();
                allTanks.add(enemyTank);
                for (int j = 0; j < aTank.getBullets().size(); j++) {
                    Bullet aBullet=aTank.getBullets().get(j);
                    if(aBullet instanceof Shot){
                        Shot newShot=new Shot(aBullet.getX(),aBullet.getY(),aBullet.getDirect(),aBullet.getType());
                        new Thread(newShot).start();
                        noMasterBullets.add(newShot);
                    }
                }
            }
            else {
                hero=new Hero(aTank.getX(),aTank.getY(),aTank.getDirect());
                allTanks.add(hero);
                for (int j = 0; j < aTank.getBullets().size(); j++) {
                    Bullet aBullet=aTank.getBullets().get(j);
                    if(aBullet instanceof Shot){
                        noMasterBullets.add(aBullet);
                    }
                }
            }
        }
    }

    public static void reLive(){//复活
        System.out.println("复活");
        hero=new Hero(500, 500, 0);
        allTanks.add(hero);
        System.out.println(allTanks.size());
    }

}
