import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControlleurReplay extends ControlleurUI{
	
	private String[] optionsReplay = {"Précédent", "Suivant", "Recommencer"};
	private ModelReplay modelReplay;
	
	/**
	 * Constructeur
	 * 
	 * Création d'un gameModel à partir d'un fichier dans le dossier du projet
	 * 
	 */
	public ControlleurReplay(){
		super();
		fileUtil = new UtilitaireFichier();
		modelReplay = fileUtil.recupererPartie();
		
		listeOptions = optionsReplay;
		panneauMenu.add(creerBoutons(listeOptions));
		super.add(panneauMenu, BorderLayout.SOUTH);	
		
		boutonsListeners();		
		initPartieReplay(modelReplay);
	}
	/**
	 * Créer une partie avec le GameModel chargé. Set l'état des checkbox selon la partie chargée. Désactive les checkbox.
	 * @param m
	 */
	private void initPartieReplay(GameModel m){
		initPartie(m);
		setCheckboxes();
		enableCheckboxes(false);
	}
	/**
	 * Enlever le tilePannel puis créer une nouvelle partie.
	 */
	@Override
	protected void nouvellePartie(){
		
		super.remove(tilePanel);
		initPartieReplay(modelReplay);		
		
		redimensionner();
		clearActiveGame();
		chronoStart();
	}
	/**
	 * Compter et écrire le temps. Vérifier si le temps est écoulé
	 */
	@Override
	protected void chronoCompte(){
		modelReplay.compter();
		checkDefaite();
    	ecrireTemps(modelReplay.getTemps());
	}
	/**
	 * 
	 */
	@Override
	protected void victoire(){
		JOptionPane.showMessageDialog(null, "Félicitations");
	}
	/**
	 * Vérifier si les resets ou le temps sont écoulés. Si oui, fin de la partie
	 */
	private void checkDefaite(){
		if(!modelReplay.isResetsLeft()||(modelReplay.getTemps() <= 0)){
			finPartie();
		}
	}
	/**
	 * Changer de partie à l'aide des boutons Précédent et Suivant. Créer un nouveau modelReplay avec le fichier suivant dans la
	 * liste puis créer une nouvelle partie
	 * 
	 * @param b
	 */
	private void naviguer(boolean b){
		if(!b){
			modelReplay = fileUtil.partiePrecedente();
		}else{
			modelReplay = fileUtil.partieSuivante();
		}
		nouvellePartie();
	}
	/**
	 * Lorsque la partie est terminée, désactiver le bouton Recommencer
	 */
	@Override
	protected void disabledInterface(){
		for(JButton b:tabBoutons){
			if(b.getText().equals("Recommencer")){
				b.setEnabled(false);
			}
		}
	}
	/**
	 * Listeners pour les boutons.
	 * S'il n'y a pas de partie à charger, les boutons ne font rien
	 */
	protected void boutonsListeners(){
		for(int i = 0; i<listeOptions.length; i++){
			tabBoutons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fileUtil.isDirectoryEmpty()){
						JOptionPane.showMessageDialog(null, "There are no saved games to load.");
					}else{
						JButton boutonClique = (JButton) e.getSource();	
						switch(boutonClique.getText()){
							case "Précédent": 
								naviguer(false);
								break;
							case "Suivant": 
								naviguer(true);
								break;	
							case "Recommencer": 
								recommencerPartie();
								checkDefaite();
								break;
						}						
					}
					
				}
			});
		}
	}
}
