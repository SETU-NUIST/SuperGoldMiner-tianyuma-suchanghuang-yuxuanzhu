import java.awt.*;

public class Object {
    int x;
    int y;
    int width;
    int height;
    Image img;
    void paintSelf(Graphics g) {
        g.drawImage(img,x,y,null);
    }
}
