import java.awt.*;
import javax.swing.ImageIcon;

public class Rock extends Object {
    Rock(){
        this.x=(int) (Math.random()*700);
        this.y=(int)(Math.random()*550+300);
        this.width=71;
        this.height=71;
        this.img= new ImageIcon(getClass().getResource("/imgs/rock1.png")).getImage();
    }
}
