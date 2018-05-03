import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame{

	private String[] listeOngletsTexte = {"Training", "Replay", "Arcade"};
	private ControlleurUI[] listeOnglets;
	
	public static void main(String args[]){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MainFrame(){
		initUI();
	}
	/**
	 * Initialisation de l'interface. On Crée trois onglets. Chacun instantie un type de controlleur différent. Un controlleur
	 * existe pour chaque mode de jeu et étend la classe abstraite ControlleurUI. 
	 * 
	 */
	private void initUI(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Mean Sum");
		
		JTabbedPane conteneur = new JTabbedPane();
		setContentPane(conteneur);
		
		//Créer les onglets
		listeOnglets = new ControlleurUI[listeOngletsTexte.length];
		for(int i = 0; i<listeOngletsTexte.length; i++){
			
			ControlleurUI tab = null;
			switch(listeOngletsTexte[i]){
			case "Training":
				tab = new ControlleurTraining();
				break;
			case "Replay":
				tab = new ControlleurReplay();
				break;
			case "Arcade":
				tab = new ControlleurArcade();
				break;
			}
			listeOnglets[i] = tab;
			conteneur.addTab(listeOngletsTexte[i], tab);
		}
		//Démarrer le chrono de l'onglet actif au départ
		ControlleurUI activeTab = (ControlleurUI)conteneur.getSelectedComponent();
		activeTab.chronoStart();
		
		pack();		
		setLocationRelativeTo(null);
		
		//Lorsqu'on change d'onglet, arrêter les chronos puis démarrer le chrono de l'onglet actif
		conteneur.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				for(int i = 0; i<listeOnglets.length; i++){
					listeOnglets[i].chronoStop();
				}
				JTabbedPane controlleur = (JTabbedPane)e.getSource();
				ControlleurUI activeTab = (ControlleurUI)controlleur.getSelectedComponent();
				activeTab.chronoStart();
			}
		});
		
	}
}
