import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.*;

/**
 * The tile panel displays all the tiles (one per digit) of the game.
 *
 */
public class PanneauTuile extends JPanel {

	/**
	 * The tile panel object holds a reference to the game model to
	 * request information to display (view) and to modify its state (controller)
	 */
	private GameModel gameModelHandle;
	/**
	 * liste qui contient les cases sélectionnées
	 */
	private ArrayList<Tuile> selectedTuiles;
	private ArrayList<Tuile> tuilesAffichees;
	private String strChiffres;
	
	private String[] tCouleurs = {
			"0xff7f50","0xffe4e1","0x66cccc",
			"0xffc3a0","0xffd700","0xa0db8e",
			"0xffec6c","0xb0e0e6","0xc6e2ff",
			"0xffcedb","0x468499","0x8be9fd"
			};
	private String[] couleursFin = {"0x6af297", "0xff5e5e", "0xFFFFFF"};
	private int colorIndex = 0;
	
	
	/*-------------Constructeur-----------------*/
	public PanneauTuile(GameModel gameModel) {
		if (gameModel == null)
			throw new IllegalArgumentException("Should provide a valid instance of GameModel!");
		
		gameModelHandle = gameModel;
		selectedTuiles = new ArrayList<Tuile>();
		tuilesAffichees = new ArrayList<Tuile>();
		creerPanel();
	
	}
	/**
	 * crée la panneau de tuiles avec la String de chiffres générée par arrayToString()
	 * 
	 */
	private void creerPanel(){
		
		strChiffres = arrayToString(gameModelHandle.getTableau());	
		//aujoute les tuiles au panel
		for(int i = 0; i<strChiffres.length(); i++){
			Tuile newTuile = new Tuile(strChiffres.charAt(i));
			this.add(newTuile);
			tuilesAffichees.add(newTuile);
		}
		
	}
	/**
	 * Selon victoire ou non, met les tuiles au vert ou au rouge
	 * @param victoire
	 */
	public void couleurEndGame(boolean victoire){
		Color c;
		if(victoire){
			c = Color.decode(couleursFin[0]);
		}else{
			c = Color.decode(couleursFin[1]);
		}
		couleurAll(c);
	}
	/**
	 * Remettre les tuiles au blanc
	 */
	public void reinitCouleurs(){
		couleurAll(Color.decode(couleursFin[2]));
		colorIndex = 0;
	}
	public ArrayList<Tuile> getTuiles(){
		return tuilesAffichees;
	}
	/**
	 * Reçoit un tableau de int et le converti en string de chiffres
	 * 
	 * @param tab
	 * @return string
	 */
	private String arrayToString(int[] tab){
		
		String str = "";
		for(int i = 0; i<tab.length; i++){
			str += tab[i];
		}
		return str;		
	}
	/**
	 * recolorer toutes les tuiles
	 * @param c
	 */
	private void couleurAll(Color c){
		for(Tuile t: tuilesAffichees){
			t.changerCouleur(c);
		}
	}
	/**
	 * Colorer une tuile sélectionnée
	 * @param tuiles
	 */
	public void couleurSelect(Tuile[] tuiles){
		Color couleur = Color.decode(tCouleurs[colorIndex]);
		for(Tuile t: tuiles){
			t.changerCouleur(couleur);
		}
		colorIndex++;
	}
}
