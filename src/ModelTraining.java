import java.util.concurrent.ThreadLocalRandom;

public class ModelTraining extends GameModel{
	/**
	 * constructeur
	 * 
	 * g�n�re une nombre de groupes al�atoire puis un tableau de nombres dont la moyenne est enti�re
	 * 
	 */
	public ModelTraining(){
		super();
		
		nbGroupes = ThreadLocalRandom.current().nextInt(3,7);
		
		tabNombres = findRoundMean(genererTableau());		
		afficherReponses(tabNombres);
	}
	/**
	 * Cr�er un String comportant les informations n�cessaires � la sauvegarde d'une partie
	 * 
	 * @return
	 */
	public String genererSauvegarde(){
		String s = "";
		
		s+=temps+" ";
		s+=resets+" ";
		s+=isMoyenne+" ";
		s+=isHelp+" ";
		
		for(int nb:tabNombres){
			s+=nb+",";		
		}
		return s;
	}
}
