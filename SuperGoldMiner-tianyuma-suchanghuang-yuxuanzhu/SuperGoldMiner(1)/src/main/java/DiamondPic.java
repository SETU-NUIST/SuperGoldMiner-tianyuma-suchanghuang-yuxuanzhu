import java.awt.*;
import javax.swing.ImageIcon;

public class DiamondPic extends Object{
    DiamondPic(){
        this.x=(int) (Math.random()*700);
        this.y=(int)(Math.random()*550+300);
        this.width=71;
        this.height=71;
        this.img= new ImageIcon(getClass().getResource("/imgs/gold2.gif")).getImage();
    }
}
