
/**
 * 自定义的Panel类，实现带有背景的JPanel
 * */
package Utils;
import java.awt.*;
import javax.swing.*;

public class MyPanel extends JPanel {//继承JPanel类
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon icon = null;
	
 public MyPanel(ImageIcon icon) {
  this.icon = icon;
 }

 

  public void paintComponent(Graphics g) {
   int x = 0, y = 0;
   g.drawImage(icon.getImage(), x, y, getSize().width,getSize().height, this);// 图片会自动缩放

  }
 }


