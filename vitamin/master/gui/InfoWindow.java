package master.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
* @author	Martin Pelzer
 * 			Fraunhofer FOKUS
 * 			martin.pelzer@fokus.fraunhofer.de
 */
public class InfoWindow extends JFrame {
		
	// the vitamin logo
	private Image image = null;
	

	public InfoWindow() {
		super("about Vitamin");
		this.setSize(470, 330);
		
		File file = new File("images/logo_info.png");
		try {
			this.image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.repaint();
	}
	
	
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		int x = 130;
				
		Font vorher = graphics.getFont();
		Font fett = graphics.getFont().deriveFont(Font.BOLD);
		graphics.setFont(fett);
		graphics.drawString("Vitamin", x, 50);
		graphics.setFont(vorher);
		graphics.drawString("Visualisation of network traffic in IP based networks", x, 70);
		graphics.drawString("vitamin.berlios.de", x, 90);
		
		graphics.drawString("written 2004 - 2005 by Martin Pelzer", x, 130);
		graphics.drawString("Fraunhofer Institute for Open Communication Systems", x, 150);
		graphics.drawString("competence center SatCom", x, 170);
		graphics.drawString("www.fokus.fraunhofer.de/satcom", x, 190);
		graphics.drawString("martin.pelzer@fokus.fraunhofer.de", x, 210);
		
		graphics.drawString("This software uses the library JPcap (jpcap.sf.net).", x, 250);
				
		graphics.drawString(" This software is published under the GPL", x, 290);
		graphics.drawString(" (except JPcap which is published under MPL 1.1).", x, 310);
		
		graphics.drawImage(this.image, 10, 50, null);
	}

}
