import java.awt.*;

public class GoldPic extends Object{
    GoldPic(){
        this.x=(int) (Math.random()*700);
        this.y=(int)(Math.random()*550+300);
        this.width=52;
        this.height=52;
        this.img= Toolkit.getDefaultToolkit().getImage("imgs/gold1.gif");
    }
}
