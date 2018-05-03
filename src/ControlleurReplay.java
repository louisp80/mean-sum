import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControlleurReplay extends ControlleurUI{
	
	private String[] optionsReplay = {"Pr�c�dent", "Suivant", "Recommencer"};
	private ModelReplay modelReplay;
	
	/**
	 * Constructeur
	 * 
	 * Cr�ation d'un gameModel � partir d'un fichier dans le dossier du projet
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
	 * Cr�er une partie avec le GameModel charg�. Set l'�tat des checkbox selon la partie charg�e. D�sactive les checkbox.
	 * @param m
	 */
	private void initPartieReplay(GameModel m){
		initPartie(m);
		setCheckboxes();
		enableCheckboxes(false);
	}
	/**
	 * Enlever le tilePannel puis cr�er une nouvelle partie.
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
	 * Compter et �crire le temps. V�rifier si le temps est �coul�
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
		JOptionPane.showMessageDialog(null, "F�licitations");
	}
	/**
	 * V�rifier si les resets ou le temps sont �coul�s. Si oui, fin de la partie
	 */
	private void checkDefaite(){
		if(!modelReplay.isResetsLeft()||(modelReplay.getTemps() <= 0)){
			finPartie();
		}
	}
	/**
	 * Changer de partie � l'aide des boutons Pr�c�dent et Suivant. Cr�er un nouveau modelReplay avec le fichier suivant dans la
	 * liste puis cr�er une nouvelle partie
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
	 * Lorsque la partie est termin�e, d�sactiver le bouton Recommencer
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
	 * S'il n'y a pas de partie � charger, les boutons ne font rien
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
							case "Pr�c�dent": 
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
