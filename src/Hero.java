public class Hero extends Tank {


    public Hero(int x, int y, int direct) {
        super(x, y, direct);
        setType(0);
        setBulletType(1);//使用激光武器
    }
}
