import java.util.concurrent.ThreadLocalRandom;

public class ModelArcade extends GameModel {
	
	private double niveau;
	private double moyenneProb;
	/**
	 * Constructeur
	 * 
	 * G�n�re les informations selon le niveau. 
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
		
		//Puisque la probabilit� de la moyenne et de l'aide sont identiques on appelle la m�me fonction
		isMoyenne = calculIsMoyenne();
		isHelp = calculIsMoyenne();
		
		afficherProbs();
		afficherReponses(tabNombres);
		
		
	}
	/**
	 * 
	 * D�termination de l'�tat de la checkbox Moyenne selon les probabilit�s impos�es
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
	 * Afficher les probabilit�s dans la console
	 */
	private void afficherProbs(){
		System.out.println("Nombre de groupes: "+nbGroupes+"\n"+"Probabilit� de nombre � deux chiffres: "+doubleDigitProb+"\nProbabilit� d'une case � cocher: "+moyenneProb);
	}
}
