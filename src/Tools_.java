public class Tools_ {
    public static void main(String[] args) {
        Rectangle rectangle1 = new Rectangle(10, 20, 50, 50);
        Rectangle rectangle2 = new Rectangle(50, 60, 110, 110);
        System.out.println(squareCollide(null, rectangle2));
    }

    public static boolean squareCollide(Rectangle rectangle1, Rectangle rectangle2) {
        //检测两个矩形是否碰撞,true为碰撞
        int x1 = rectangle1.getX1(), y1 = rectangle1.getY1(), x2 = rectangle1.getX2(), y2 = rectangle1.getY2();
        int x3 = rectangle2.getX1(), y3 = rectangle2.getY1(), x4 = rectangle2.getX2(), y4 = rectangle2.getY2();
        int subX1 = Math.abs(x1 - x2), subX2 = Math.abs(x3 - x4), subY1 = Math.abs(y1 - y2), subY2 = Math.abs(y3 - y4);
        int maxX = Math.max(x1, Math.max(x2, Math.max(x3, x4)));
        int minX = Math.min(x1, Math.min(x2, Math.min(x3, x4)));
        int maxY = Math.max(y1, Math.max(y2, Math.max(y3, y4)));
        int minY = Math.min(y1, Math.min(y2, Math.min(y3, y4)));
        if (subX1 + subX2 > maxX - minX && subY1 + subY2 > maxY - minY) {
            //System.out.println(true);
            return true;
        }
        //System.out.println(false);
        return false;
    }
}
