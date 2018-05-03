import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * The view-controller class handles the display (tiles, buttons, etc.)
 * and the user input (actions from selections, clicks, etc.).
 *
 */
public abstract class ControlleurUI extends JPanel {

	/**
	 * Instance of the game (logic, state, etc.)
	 */
	protected GameModel gameModel;
	protected UtilitaireFichier fileUtil;
	protected PanneauTuile tilePanel;
	protected JPanel panneauMenu;
	protected String[] listeOptions;
	protected JButton[] tabBoutons;
	protected Timer chrono;
	protected JCheckBox[] tabCheckboxes;
	protected boolean isGameWon = false;
	protected boolean partieTerminee = false;
	protected int niveau = 0;
	
	private JFrame mainFrame;
	private String[] listeCheckbox = {"Moyenne", "Sans Aide"};	
	private String[] listeLabels = {"Level", "Objectif", "Réponse", "Resets", "Temps"};
	private JLabel[] tabLabels;



	
	public ControlleurUI() {
		setLayout(new BorderLayout());
		
		//Panneau qui contient les boutons et les checkbox
		panneauMenu = new JPanel();
		panneauMenu.setLayout(new BoxLayout(panneauMenu, BoxLayout.PAGE_AXIS));
		panneauMenu.add(creerCheckboxes());	
		
		//Panneau qui contient la barre d'affichage
		JPanel panneauLabels = new JPanel();
		panneauLabels.add(creerBarre());
		panneauLabels.setLayout(new BoxLayout(panneauLabels, BoxLayout.LINE_AXIS));
		this.add(panneauLabels, BorderLayout.CENTER);
		
		//Timer
		chrono = new Timer(100, new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	chronoCompte();
		    }    
		});		
	}
	/**
	 * procédures implémentées dans chacun des controlleurs
	 */
	protected abstract void victoire();
	protected abstract void nouvellePartie();
	protected abstract void disabledInterface();
	protected abstract void boutonsListeners();
	/**
	 * Initialisation d'une partie. Assigner le GameModel, créer le tilePanel, réinitialiser l'interface et assigner les listeners
	 */
	protected void initPartie(GameModel g){
		gameModel = g;
		tilePanel = new PanneauTuile(gameModel);
		tilePanel.setBackground(Color.decode("0xe0dcd7"));
		this.add(tilePanel, BorderLayout.NORTH);
		
		isGameWon = false;
		partieTerminee = false;
		
		//Réinitialiser le contenu de la barre d'affichage
		refreshBarre();		
		//Réinitialiser l'état des checkbox
		enableCheckboxes(true);
		initBoutonsTxt();

		tuilesListeners();
	}
	/**
	 * reçoit une ou deux tuiles. Si la partie n'est pas en cours, ne pas permettre la sélection. 
	 * Sélectionner les tuiles puis changer leurs couleurs. Vérifier si la partie est gagnée puis rafraichir l'interface
	 * 
	 * @param tuiles
	 */
	private void select(Tuile...tuiles){
		if(!partieTerminee){
			if(gameModel.select(tuiles)){
				tilePanel.couleurSelect(tuiles);
				checkWin();
			}
			refreshBarre();
		}	
	}
	/**
	 * Vérifier si la partie est gagnée. D'abord, s'assurer que toutes les tuiles ont été sélectionnées. Ensuite, 
	 * vérifier si la réponse de l'utilisateur correspond à l'objectif de la partie (si la somme ou moyenne est correcte).
	 * Si oui, fin de la partie. Si non, mettre les tuiles au rouge.
	 * 
	 */
	private void checkWin(){
		
		if(gameModel.getSelected().size() == tilePanel.getTuiles().size()){
			if(gameModel.getReponse() == gameModel.getObjectif()){
				isGameWon = true;
				finPartie();
			}else{
				isGameWon = false;
				tilePanel.couleurEndGame(isGameWon);
			}
		}	
	}
	/**
	 * Lorsqu'une partie n'Est pas en cours. Le timer n'est pas en marche, les checkbox sont désactivées.
	 * Certains boutons sont désactivés selon le mode de jeu (disabledInterface)
	 */
	protected void finPartie(){
		chrono.stop();
		refreshBarre();
		partieTerminee = true;
		enableCheckboxes(false);
		tilePanel.couleurEndGame(isGameWon);
		if(isGameWon){
			victoire();
		}
		disabledInterface();
	}
	/**
	 * Réinitialisation de la partie actuelle. Itérer le nombre de resets
	 * 
	 */
	protected void recommencerPartie(){
		gameModel.reset();
		clearActiveGame();
	}
	/**
	 * Compter et afficher le temps 
	 */
	protected void chronoCompte(){
		gameModel.compter();
    	ecrireTemps(gameModel.getTemps());
	}	
	/**
	 * démarrer le chronomètre
	 */
	public void chronoStart(){
		if(!partieTerminee){
			chrono.start();
		}
	}
	public void chronoStop(){
		chrono.stop();
	}
	/**
	 * Réinitialisation de la partie. Vider les listes des tuiles séléctionnées, remettre les tuiles au blacn
	 * et rafraîchir l'interface
	 */
	protected void clearActiveGame(){
		gameModel.viderListes();
		tilePanel.reinitCouleurs();
		refreshBarre();
	}
	/**
	 * Boucle qui sélectionne les cases lorsque l'utilisateur abandonne la partie.
	 */
	protected void abandonner(){
		clearActiveGame();
		finPartie();
		int[] tab = gameModel.getTableau();
		int nbChiffres = 0;
		int indexTuile = 0;
		
		//pour chaque nombre formé par le game model
		for(int nombre: tab){
			//obtenir la taille du nombre (1 ou 2 chiffres)
			nbChiffres = String.valueOf(nombre).length();
			
			//Pour chaque chiffre, ajouter dans un tableau la tuile correspondante
			Tuile[] tabTuilesTmp = new Tuile[nbChiffres];
			for(int i = 0; i<tabTuilesTmp.length;i++){
				tabTuilesTmp[i] = (Tuile)tilePanel.getComponent(indexTuile + i);
			}
			//séléctionner les tuiles dans PanneauTuile
			gameModel.select(tabTuilesTmp);
			tilePanel.couleurSelect(tabTuilesTmp);
			//actualiser à quelle tuile du tile pannel nous sommes rendus
			indexTuile += nbChiffres;
		}
	}
	/**
	 * recoit le nombre de dixièmes de secondes puis ordonne en minutes et en secondes. retourne un chaine
	 * qui affiche le temps comme un chronomètre
	 * 
	 * @param t
	 * @return string
	 */
	protected void ecrireTemps(int t){
		int minutes = t/600;
		int secondes = (t%600)/10;
		int dixiemes = (t%600)%10;
		
		tabLabels[tabLabels.length-1].setText("Temps: "+minutes+" : "+secondes+" : "+dixiemes);
	}
	/**
	 * permet de changer l'état (activées ou désactivées) des checkbox. Reçoit l'état désiré en paramètre
	 * 
	 * @param b
	 */
	protected void enableCheckboxes(boolean b){
		for(JCheckBox c:tabCheckboxes){
			c.setEnabled(b);
		}
	}	
	/**
	 * Change l'état des checkbox (sélectionnées ou non) selon le gameModel
	 */
	protected void setCheckboxes(){
		tabCheckboxes[0].setSelected(gameModel.getIsMoyenne());
		tabCheckboxes[1].setSelected(gameModel.getIsHelp());
	}
	/**
	 * Remet les boutons à leur état original. Remet le texte original et active les boutons
	 */
	private void initBoutonsTxt(){
		//Réinitialiser les boutons
		for(int i = 0; i<tabBoutons.length; i++){
			JButton b = tabBoutons[i];
			b.setEnabled(true);
			b.setText(listeOptions[i]);
		}
	}
	/**
	 * Rafraichir la barre d'affichage. On modifie le contenu des labels selon les valeurs du gameModel.
	 * 
	 * @param JPanel
	 */
	private void refreshBarre(){

		String info = "";
		String sLevel = "N/A";
		if(niveau!=0){
			sLevel = ""+niveau;
		}
		String sObjectif = ""+gameModel.getObjectif();
		String sReponse = gameModel.getReponse()+"("+gameModel.getNbGroupes()+")";
		String sResets = ""+gameModel.resets;
		
		//Si noHelp est sélectionné, on n'affiche pas la somme partielle
		if(gameModel.getIsHelp()){
			sReponse = "N/A";		
		}
		
		for(int i = 0; i<listeLabels.length-1; i++){	
			switch(listeLabels[i]){
				case "Level":
					info = sLevel;
					break;
				case "Objectif":
					info = sObjectif;
					break;
				case "Réponse":
					info = sReponse;
					break;
				case "Resets":
					info = sResets;
					break;
			}
			String texteLabel = listeLabels[i]+": "+info;
			tabLabels[i].setText(texteLabel);
		}
		//Le temps est affiché dans une autre procédure
		ecrireTemps(gameModel.getTemps());
	}
	/**
	 * Créer la barre d'affichage. On crée un jpanel global qui contient un JPanel pour chaque élément de la barre
	 * Chaque JPanel contient un JLabel. Le texte du label est établi dans refreshBarre()
	 * 
	 * @return
	 */
	private JPanel creerBarre(){
		JPanel contenant = new JPanel();
		contenant.setLayout(new BoxLayout(contenant, BoxLayout.LINE_AXIS));
		tabLabels = new JLabel[listeLabels.length];
		
		for(int i = 0; i<listeLabels.length; i++){
			JPanel panel = new JPanel();
			tabLabels[i] = new JLabel(listeLabels[i] + ": ");
			
			panel.setBackground(Color.WHITE);
			panel.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 15, 5, 15)));
			panel.add(tabLabels[i]);
			contenant.add(panel);
		}
		return contenant;
	}
	/**
	 * Création des checkbox. 
	 * @return panel qui contient les checkbox
	 */
	private JPanel creerCheckboxes(){
		
		JPanel panel = new JPanel();
		tabCheckboxes = new JCheckBox[listeCheckbox.length];
		
		for(int i = 0; i<listeCheckbox.length; i++){
			tabCheckboxes[i] = new JCheckBox(listeCheckbox[i]);
			panel.add(tabCheckboxes[i]);
		}	
		return panel;		
	}
	/**
	 * Création des boutons
	 * @param nomsBoutons liste qui contient les noms des boutons
	 * @return panel qui contient les boutons
	 */
	protected JPanel creerBoutons(String[] nomsBoutons){
		
		JPanel panel = new JPanel();
		tabBoutons = new JButton[nomsBoutons.length];
		
		for(int i = 0; i<nomsBoutons.length; i++){
			tabBoutons[i] = new JButton(nomsBoutons[i]);
			panel.add(tabBoutons[i]);
		}
		return panel;
	}
	/**
	 * Redimensionner la fenêtre selon la taille du panneauTuiles et repositionner au centre de l'écran
	 */
	protected void redimensionner(){
		mainFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
	}
	/**
	 * Assigner des listeners aux tuiles. On obtien la tuile cliquée et la tuile sous la souris lors du mouse released.
	 * On s'assure que les tuiles sont voisines puis on appelle la fonction de sélction.
	 */
	private void tuilesListeners() {		
		
		for(int i = 0; i<tilePanel.getComponentCount(); i++){
			
			Tuile laTuile = (Tuile)tilePanel.getComponent(i);
			
			laTuile.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					
					Tuile tuileMouseDown = (Tuile)e.getSource();
					
					//Obtenir la position du curseur relative au contentPane
					int mousePosX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
					int mousePosY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
					
					//Vérifier si l'élément sur lequel l'utilisateur fait mouseReleased est bel et bien une tuile, et non le panneauTuile lui même.	
					if(tilePanel.isAncestorOf(tilePanel.findComponentAt(mousePosX,mousePosY))){
						
						//On assigne la tuile sous la souris à une variable, puis on vérifie s'il s'agit de la même tuile que celle
						//qui a été cliquée.
						Tuile tuileMouseUp = (Tuile)tilePanel.findComponentAt(mousePosX,mousePosY);
						if(tuileMouseDown == tuileMouseUp){
							//on séléctionne la tuile
							select(tuileMouseUp);				
						//S'il s'agit de deux tuiles,s'assurer qu'elles sont voisines en calculant la différence de leurs indexes
						}else if(Math.abs(tilePanel.getComponentZOrder(tuileMouseUp) - tilePanel.getComponentZOrder(tuileMouseDown)) == 1){
							
							//Pour s'assurer que peu importe l'ordre dans lequel les tuiles 
							//sont sélectionnées, le chiffre de gauche est toujours le premier
							if(tilePanel.getComponentZOrder(tuileMouseUp)<tilePanel.getComponentZOrder(tuileMouseDown)){
								select(tuileMouseUp, tuileMouseDown);
							}else{
								select(tuileMouseDown, tuileMouseUp);
							}
						}
					}					
				}
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {}
			});
		}
	}
}
