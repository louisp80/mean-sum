import java.util.concurrent.ThreadLocalRandom;

public class ModelTraining extends GameModel{
	/**
	 * constructeur
	 * 
	 * génère une nombre de groupes aléatoire puis un tableau de nombres dont la moyenne est entière
	 * 
	 */
	public ModelTraining(){
		super();
		
		nbGroupes = ThreadLocalRandom.current().nextInt(3,7);
		
		tabNombres = findRoundMean(genererTableau());		
		afficherReponses(tabNombres);
	}
	/**
	 * Créer un String comportant les informations nécessaires à la sauvegarde d'une partie
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
