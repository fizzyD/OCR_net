import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonPixel extends JButton implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int pixelx;
	int pixely;
	int xmax;
	int ymax;
	double[] screen;
	public ButtonPixel(String label, int x, int y, int xmax, int ymax, double[] pixelMap){
		//super(label);
		super();
		this.screen = pixelMap;
		this.xmax = xmax;
		this.ymax = ymax;
		this.setBackground(Color.white);
		this.setForeground(Color.white);
		pixelx = x;
		pixely = y;
		this.addMouseListener(this);
	}
	
    /////////////////////////////////////////////////////////////////
    //These are not used but are necessary for mouseListener
    public void mouseEntered (MouseEvent e)
    {
    	//System.out.println("MouseEntered");
    	// If the button is pressed and the mouse enters toggle the color and pixel
       	if ( SwingUtilities.isLeftMouseButton(e)){
        	if ( this.getBackground() == Color.black){
        		this.setBackground(Color.white);
        		this.screen[pixely*xmax+pixelx] = 0.0;
        		//System.out.println("mouseClicked set white");
        	} else {
        		this.setBackground(Color.black);
        		this.screen[pixely*xmax+pixelx] = 1.0;
            	//System.out.println("mouseClicked set black");
        	}
       	}
		//this.setForeground(Color.black);
		//this.setBackground(Color.black);
     }

    public void mouseClicked(MouseEvent e){
    	//System.out.println("HI BUTTON1 mouseClicked");
    }

    public void mouseExited (MouseEvent e)
    {
		//System.out.println("buttonpixel bye mouse");
    }
    

    public void mousePressed (MouseEvent e)
    {
    	//this.setBackground(Color.black);
    	//If the mouse is pressed on this pixel toggle the color and pixel
    	if ( this.getBackground() == Color.black){
    		this.setBackground(Color.white);
    		this.screen[pixely*xmax+pixelx] = 0.0;
        	//System.out.println("mouseClicked set white");
    	} else {
    		this.setBackground(Color.black);
        	//System.out.println("mouseClicked set black");
    		this.screen[pixely*xmax+pixelx] = 1.0;
    	}
    	//System.out.println("mousePress set black");
    }
    

    public void mouseReleased (MouseEvent e)
    {
    }
 
}