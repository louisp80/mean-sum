import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Tuile extends JPanel {
	
	String text;
	float tailleText = 45f;
	
	public Tuile(char chiffre){
		text = ""+chiffre;
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
	}
	protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawString(text, (this.getWidth()/2) - (int)(tailleText*0.28), (this.getHeight()/2) + (int)(tailleText*0.36));
        setFont(getFont().deriveFont(tailleText));
    }
	public void changerCouleur(Color c){
		setBackground(c);
	}
	public String getText(){
		return text;
	}

}
