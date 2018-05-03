import java.util.concurrent.ThreadLocalRandom;

public class ModelArcade extends GameModel {
	
	private double niveau;
	private double moyenneProb;
	/**
	 * Constructeur
	 * 
	 * Génère les informations selon le niveau. 
	 * 
	 * @param n
	 */
	public ModelArcade(int n){
		super();
		
		niveau = n;
		
		nbGroupes = (3 + Math.round(3.0 * niveau / 20));
		doubleDigitProb =  0.3 + (0.3 * niveau / 20);
		moyenneProb =  0.1 + (0.9 * niveau / 20);
		
		tabNombres = findRoundMean(genererTableau());
		
		//Puisque la probabilité de la moyenne et de l'aide sont identiques on appelle la même fonction
		isMoyenne = calculIsMoyenne();
		isHelp = calculIsMoyenne();
		
		afficherProbs();
		afficherReponses(tabNombres);
		
		
	}
	/**
	 * 
	 * Détermination de l'état de la checkbox Moyenne selon les probabilités imposées
	 * 
	 * @return
	 */
	private boolean calculIsMoyenne(){
		boolean moyenne= false;
		double rand = Math.random();
		if(rand<moyenneProb){
			moyenne = true;			
		}
		return moyenne;
	}
	/**
	 * Afficher les probabilités dans la console
	 */
	private void afficherProbs(){
		System.out.println("Nombre de groupes: "+nbGroupes+"\n"+"Probabilité de nombre à deux chiffres: "+doubleDigitProb+"\nProbabilité d'une case à cocher: "+moyenneProb);
	}
}
