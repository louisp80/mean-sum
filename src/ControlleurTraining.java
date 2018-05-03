import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ControlleurTraining extends ControlleurUI{
	
	private String[] optionsTraining = {"Prochain", "Abandon", "Recommencer"};
	private String txtSauvegarderBtn = "Sauvegarder";
	private ModelTraining modelTraining;
	
	public ControlleurTraining(){
		super();
		modelTraining = new ModelTraining();
		fileUtil = new UtilitaireFichier();
		listeOptions = optionsTraining;
		
		panneauMenu.add(creerBoutons(listeOptions));
		super.add(panneauMenu, BorderLayout.SOUTH);	
		
		boutonsListeners();
		checkboxListeners();
	
		//initialiser la partie
		initPartie(modelTraining);
	}
	/**
	 * Création d'une nouvelle partie
	 */
	@Override
	protected void nouvellePartie(){
		modelTraining = new ModelTraining();
		modelTraining.setIsMoyenne(tabCheckboxes[0].isSelected());
		modelTraining.setIsHelp(tabCheckboxes[1].isSelected());
		
		super.remove(tilePanel);
		initPartie(modelTraining);		
		
		redimensionner();
		clearActiveGame();
		chronoStart();
		
	}
	/**
	 * 
	 */
	@Override
	protected void victoire(){
		//changer bouton recommencer pour sauvegarder
		for(JButton b : tabBoutons){
			if(b.getText().equals("Recommencer")){
				b.setText(txtSauvegarderBtn);
			}
		}	
	}
	/**
	 * Lorsque la partie est terminée, on ne peut plus cliquer sur Abandon
	 */
	@Override
	protected void disabledInterface(){
		for(JButton b:tabBoutons){
			if(b.getText().equals("Abandon")){
				b.setEnabled(false);
			}
		}
	}
	/**
	 * Créer un nouveau fichier pour la partie actuelle
	 * 
	 */
	private void sauvegarder(){
		fileUtil.creerFichier((ModelTraining)gameModel);
	}
	/**
	 * Assigner des listeners pour chaque checkbox. Aussitôt que l'on clique un checkbox, une nouvelle partie est démarrée
	 */
	private void checkboxListeners(){
		for(int i = 0; i<tabCheckboxes.length; i++){
			tabCheckboxes[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					nouvellePartie();
				}
			});	
		}
	}
	/**
	 * Listeners pour les boutons
	 */
	protected void boutonsListeners(){
		for(int i = 0; i<listeOptions.length; i++){
			tabBoutons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton boutonClique = (JButton) e.getSource();	
					switch(boutonClique.getText()){
						case "Prochain": 
							nouvellePartie();
							break;
						case "Abandon": 
							abandonner();
							break;	
						case "Recommencer": 
							recommencerPartie();
							break;
						case "Sauvegarder":
							sauvegarder();
							break;
					}
				}
			});
		}
	}
}
