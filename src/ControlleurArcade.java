import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControlleurArcade  extends ControlleurUI{
	
	private ModelArcade modelArcade;
	private int niveauFinal = 20;
	private String[] optionsArcade = {"Prochain", "Abanon", "Reset", "Nouvelle Partie"};
	
	public ControlleurArcade(){
		super();
		niveau = 1;
		modelArcade = new ModelArcade(niveau);
		
		listeOptions = optionsArcade;
		panneauMenu.add(creerBoutons(listeOptions));
		super.add(panneauMenu, BorderLayout.SOUTH);	
		
		boutonsListeners();
		
		initPartieArcade(modelArcade);
	}
	/**
	 * 
	 * @param m
	 */
	private void initPartieArcade(GameModel m){
		initPartie(m);
		setCheckboxes();
		disabledInterface();		
	}
	/**
	 * Incrémenter le niveau et créer une nouvelle partie
	 */
	private void prochainNiveau(){
		niveau++;
		nouvellePartie();
	}
	/**
	 * 
	 */
	@Override
	protected void nouvellePartie(){
		modelArcade = new ModelArcade(niveau);
		super.remove(tilePanel);
		initPartieArcade(modelArcade);		
		
		redimensionner();
		clearActiveGame();
		chronoStart();
	}
	/**
	 * Appellée lorsque chaque niveau est gagné. S'il s'agit du dernier niveau, un message de victoire s'affiche
	 */
	@Override
	protected void victoire(){
		partieTerminee = false;
		if(niveau == niveauFinal){
			JOptionPane.showMessageDialog(null, "Vous avez battu tous les niveaux!");
			partieTerminee = true;
		}
	}
	/**
	 * Désactiver les boutons selon l'état de fin de partie. Plusieurs possibilités
	 */
	@Override
	protected void disabledInterface(){
		enableCheckboxes(false);
		
		for(JButton b:tabBoutons){
			
			if(partieTerminee){
				if(b.getText().equals("Nouvelle Partie")){
					b.setEnabled(true);
				}else{
					b.setEnabled(false);
				}
			}else if(isGameWon){
				if(b.getText().equals("Prochain")||b.getText().equals("Nouvelle Partie")){
					b.setEnabled(true);
				}else{	
					b.setEnabled(false);
				}
			}else{
				if(b.getText().equals("Prochain")){
					b.setEnabled(false);
				}else{
					b.setEnabled(true);
				}
			}			
		}
	}
	/**
	 * Listeners pour les boutons
	 */
	@Override
	protected void boutonsListeners(){
		for(int i = 0; i<listeOptions.length; i++){
			tabBoutons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton boutonClique = (JButton) e.getSource();	
					switch(boutonClique.getText()){
						case "Prochain": 
							prochainNiveau();
							break;
						case "Abandon": 
							abandonner();
							break;	
						case "Reset":
							recommencerPartie();
							break;
						case "Nouvelle Partie":
							niveau=1;
							nouvellePartie();
							break;
					}						
				}
			});
		}
	}
}
