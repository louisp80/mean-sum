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
	 * Liste des groupes de un ou deux chiffres form�s par l'utilisateur
	 * Utilis�e pour calculer la somme et la comparer � la somme g�n�r�e par GameModel
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
	 * Incr�menter le temps
	 */
	public void compter(){
		temps++;
	}
	/**
	 * incr�menter les resets
	 */
	public void reset(){
		resets++;
	}
	/**
	 * G�n�re un tableau de nombres selon les probabilit�s. Ne g�re pas la moyenne enti�re.
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
	 *Cette m�thode transforme le tableau qu'elle re�oit en tableau ayant une moyenne enti�re
	 *sans changer la longueur des entr�es.
	 *
	 *Le but est de d�cr�menter une des entr�es jusqu'� ce que la moyenne soit enti�re. On sait qu'on n'aura jamais
	 *besoin de d�cr�menter de plus que le nombre d'entr�es.
	 *
	 *Pour ce faire, on cherche d'abord une entr�e dont la valeur est sup�rieure au nombre d'entr�es (pour ne pas se
	 *retrouver avec une valeur n�gative), ou encore qui ne se trouve pas entre 10 et 10+nbGroupes(car on risquerait
	 *de passer de 10 � 9, ce qui changerait la longueur du nombre).
	 *
	 *(On ne veut pas que la longueur d'une entr�e passe de 2 � 1 puisque cela modifierait le r�sultat de la g�n�ration
	 *de tableau qui prend en compte des probabilit�s. Les probabilit�s risqueraient de ne plus �tre respect�es)
	 *
	 * Si aucune entr�e ne r�pond aux crit�res, on incr�mente sur la derni�re entr�e du tableau, � la place de d�cr�menter.
	 *(Pas besoin de v�rifier si le nombre est pr�s de 10 ou de 100 puisque si un tel nombre �tait dans le tableau, il aurait
	 *r�pondu aux crit�res de d�cr�mentation)
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
	 * G�n�ration de la moyenne 
	 * 
	 * @param tableau
	 * @return
	 */
	protected double genererMoyenne(int[] tab){	
		return (double)somme/((double)(tab.length));
	}
	/**
	 * g�n�ration de la somme
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
		System.out.println("R�ponses: "+r);
	}
	/**
	 * V�rifier si les tuiles clique�s sont d�j� s�lectionn�es
	 * Comparer les tuiles cliqu�es avec celles dans la liste de tuiles s�lectionn�es
	 * 
	 * @param tuiles tableau de Tuile
	 * @return boolean si les cases �taient d�j� s�lectionn�es
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
	 * V�rifie si les tuiles choisies sont d�j� s�lectionn�es. Si non, les ajouter � la liste selectedTuiles
	 * 
	 * @param tuiles tableau de Tuile cliqu�es. Peut en contenir 1 ou 2
	 * @return bool pour notifier le controlleur si la s�l�ction a fonctionn�
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
	 * NEttoie les listes de groupes form�s par l'utilisateur et des tuiles s�lectionn�es
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
	 * inverser la valeur de isMoyenne selon l'�tat de la case � cocher
	 */
	public void setIsMoyenne(boolean b){
		isMoyenne = b;
	}
	/**
	 * inverser la valeur de isHelp selon l'�tat de la case � cocher
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
	 * Calcul de la r�ponse de l'utilisateur
	 * La somme est divis�e par sois 1, sois le nombre de groupes, selon l'�tat de isMoyenne. Ceci permet de faire
	 * un seul calcul pour la somme et la moyenne.
	 * 
	 * @return
	 */
	public int getReponse(){	
		int reponse = 0;
		int divMoyenne = 1;
		int nbGroupes = getNbGroupes();
		if(nbGroupes!=0){
			//d�termination du d�nominateur
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
	 * @return les tuiles s�lectionn�es
	 */
	public ArrayList<Tuile> getSelected(){
		return selectedTuiles;
	}
	/**
	 * 
	 * @return le nombre de groupes form�s par l'utilisateur
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
