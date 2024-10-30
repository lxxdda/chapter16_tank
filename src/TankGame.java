import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Scanner;

public class TankGame extends JFrame {
    MyPanel mp = null;
    Scanner myScanner=new Scanner(System.in);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TankGame tankGame = new TankGame();

    }

    public TankGame() throws IOException, ClassNotFoundException {
        System.out.println("请输入选择 0: 新游戏 1: 继续上局");
        String key=myScanner.next();
        mp = new MyPanel(key);
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);
        this.setSize(1300, 800);
        this.addKeyListener(mp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    GameMap.keepRecord();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("监听到窗口关闭");
                System.exit(0);
            }
        });
    }
}
