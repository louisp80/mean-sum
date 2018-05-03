import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public abstract class GameModel {
	
	protected int temps = 0;
	protected int resets = 0;
	protected boolean isMoyenne = false;
	protected boolean isHelp = false;
	protected int[] tabNombres;

	protected int somme;
	protected double moyenne;
	

	protected double nbGroupes;
	protected double doubleDigitProb = 0.3;
	/**
	 * Liste des groupes de un ou deux chiffres formés par l'utilisateur
	 * Utilisée pour calculer la somme et la comparer à la somme générée par GameModel
	*/
	private ArrayList<String> groupesChiffres;
	private ArrayList<Tuile> selectedTuiles;
	/**
	 * constructeur
	 * 
	 * Initialisation des listes
	 */
	public GameModel(){
		
		selectedTuiles = new ArrayList<Tuile>();
		groupesChiffres = new ArrayList<String>();
	}
	/**
	 * Incrémenter le temps
	 */
	public void compter(){
		temps++;
	}
	/**
	 * incrémenter les resets
	 */
	public void reset(){
		resets++;
	}
	/**
	 * Génère un tableau de nombres selon les probabilités. Ne gère pas la moyenne entière.
	 * 
	 * @return tab
	 */
	protected int[] genererTableau(){
		
		int[] tab = new int[(int)nbGroupes];
		
		for(int i = 0; i<tab.length; i++){	
			double rand = Math.random()*1;	
			if(rand >= doubleDigitProb){	
				tab[i] = ThreadLocalRandom.current().nextInt(1,10);
			}else{
				tab[i] = ThreadLocalRandom.current().nextInt(10,100);				
			}		
		}
		return tab;
	}
	/**
	 *Cette méthode transforme le tableau qu'elle reçoit en tableau ayant une moyenne entière
	 *sans changer la longueur des entrées.
	 *
	 *Le but est de décrémenter une des entrées jusqu'à ce que la moyenne soit entière. On sait qu'on n'aura jamais
	 *besoin de décrémenter de plus que le nombre d'entrées.
	 *
	 *Pour ce faire, on cherche d'abord une entrée dont la valeur est supérieure au nombre d'entrées (pour ne pas se
	 *retrouver avec une valeur négative), ou encore qui ne se trouve pas entre 10 et 10+nbGroupes(car on risquerait
	 *de passer de 10 à 9, ce qui changerait la longueur du nombre).
	 *
	 *(On ne veut pas que la longueur d'une entrée passe de 2 à 1 puisque cela modifierait le résultat de la génération
	 *de tableau qui prend en compte des probabilités. Les probabilités risqueraient de ne plus être respectées)
	 *
	 * Si aucune entrée ne répond aux critères, on incrémente sur la dernière entrée du tableau, à la place de décrémenter.
	 *(Pas besoin de vérifier si le nombre est près de 10 ou de 100 puisque si un tel nombre était dans le tableau, il aurait
	 *répondu aux critères de décrémentation)
	 *
	 * 
	 * @param tab
	 * @return
	 */
	protected int[] findRoundMean(int[] tab){
		
		int i = 0;
		boolean b = false;
		while(!b&&(tab[i]<nbGroupes||((tab[i]>10)&&(tab[i]<(10+nbGroupes))))){
			i++;
			if(i==tab.length){
				b=true;
			}
		}
		
		somme = genererSomme(tab);
		moyenne = genererMoyenne(tab);
		
		while(moyenne != Math.floor(moyenne)){
			
			if(b){
				tab[i-1]++;
			}else{
				tab[i]--;
			}
			
			somme = genererSomme(tab);
			moyenne = genererMoyenne(tab);
			
		}
		return tab;
	}
	/**
	 * Génération de la moyenne 
	 * 
	 * @param tableau
	 * @return
	 */
	protected double genererMoyenne(int[] tab){	
		return (double)somme/((double)(tab.length));
	}
	/**
	 * génération de la somme
	 * 
	 * @param tableau
	 * @return
	 */
	protected int genererSomme(int[] tab){
		
		int s=0;
		for(int i = 0; i<tab.length; i++){
			s += tab[i];
		}	
		return s;
	}
	/**
	 * afficher les groupes du tableau dans la console
	 * @param tab
	 */
	protected void afficherReponses(int[] tab){
		String r ="";
		for(int i = 0; i<tab.length; i++){
			r+=(tab[i]+", ");
		}
		System.out.println("Réponses: "+r);
	}
	/**
	 * Vérifier si les tuiles cliqueés sont déjà sélectionnées
	 * Comparer les tuiles cliquées avec celles dans la liste de tuiles sélectionnées
	 * 
	 * @param tuiles tableau de Tuile
	 * @return boolean si les cases étaient déjà sélectionnées
	 */
	private boolean isSelected(Tuile[] tuiles){
		
		boolean b = false;
		if(!selectedTuiles.isEmpty()){
			for(Tuile sT: selectedTuiles){
				for(Tuile nT: tuiles){
					if(sT.equals(nT)){
						b = true;
					}
				}
			}
		}
		return b;
	}
	/**
	 * Vérifie si les tuiles choisies sont déjà sélectionnées. Si non, les ajouter à la liste selectedTuiles
	 * 
	 * @param tuiles tableau de Tuile cliquées. Peut en contenir 1 ou 2
	 * @return bool pour notifier le controlleur si la séléction a fonctionné
	 */
	public boolean select(Tuile[] tuiles){
	
		boolean b = false;
		String s = "";
		if(!isSelected(tuiles)){
			for(Tuile t: tuiles){
				selectedTuiles.add(t);
				s+=t.getText();
			}
			groupesChiffres.add(s);
			b = true;
		}
		return b;
	}
	/**
	 * NEttoie les listes de groupes formés par l'utilisateur et des tuiles sélectionnées
	 * 
	 */
	public void viderListes(){
		while(!selectedTuiles.isEmpty()){
			selectedTuiles.remove(0);
		}
		while(!groupesChiffres.isEmpty()){
			groupesChiffres.remove(0);
		}
	}
	/**
	 * inverser la valeur de isMoyenne selon l'état de la case à cocher
	 */
	public void setIsMoyenne(boolean b){
		isMoyenne = b;
	}
	/**
	 * inverser la valeur de isHelp selon l'état de la case à cocher
	 */
	public void setIsHelp(boolean b){
		isHelp = b;
	}
	/**
	 * 
	 */
	public boolean getIsMoyenne(){
		return isMoyenne;
	}
	/**
	 * 
	 */
	public boolean getIsHelp(){
		return isHelp;
	}
	/**
	 * 
	 * @return
	 */
	public int getObjectif(){
		
		return (isMoyenne) ? (int)moyenne : somme;
	}
	/**
	 * Calcul de la réponse de l'utilisateur
	 * La somme est divisée par sois 1, sois le nombre de groupes, selon l'état de isMoyenne. Ceci permet de faire
	 * un seul calcul pour la somme et la moyenne.
	 * 
	 * @return
	 */
	public int getReponse(){	
		int reponse = 0;
		int divMoyenne = 1;
		int nbGroupes = getNbGroupes();
		if(nbGroupes!=0){
			//détermination du dénominateur
			divMoyenne = (isMoyenne) ? nbGroupes : 1;
			//calcul de la somme
			for(String nb: groupesChiffres){
				reponse+=Integer.parseInt(nb);
			}
		}
		return (reponse/divMoyenne);
	}
	/**
	 * 
	 * @return
	 */
	public int getTemps(){
		return temps;			
	}
	/**
	 * 
	 * @return
	 */
	public int[] getTableau(){
		return tabNombres;
	}
	/**
	 * 
	 * @return les tuiles sélectionnées
	 */
	public ArrayList<Tuile> getSelected(){
		return selectedTuiles;
	}
	/**
	 * 
	 * @return le nombre de groupes formés par l'utilisateur
	 */
	public int getNbGroupes(){
		int nbGroupes = 0;
		if(groupesChiffres.isEmpty()){
			nbGroupes = 0;
		}else{
			nbGroupes = groupesChiffres.size();
		}
		return nbGroupes;
	}
}
