public class ModelReplay extends GameModel {
	
	/**
	 * Constructeur
	 * 
	 * Re�oit une chaine de caract�res extraite d'un fichier de sauvegarde. Assigner chaque proprit� � sa valeur respective.
	 * 
	 * @param contenu
	 */
	public ModelReplay(String contenu){
		super();
		
		String[] tabContenu = contenu.split(" ");
		String[] tabNbsStr = tabContenu[tabContenu.length-1].split(",");
		int[] tabNbsInt = new int[tabNbsStr.length];
		
		for(int i = 0; i<tabNbsInt.length; i++){
			tabNbsInt[i] = Integer.parseInt(tabNbsStr[i]);
		}
		
		temps = Integer.parseInt(tabContenu[0]);
		resets = Integer.parseInt(tabContenu[1]);
		isMoyenne = Boolean.parseBoolean(tabContenu[2]);
		isHelp = Boolean.parseBoolean(tabContenu[3]);
		tabNombres = tabNbsInt;
		
		somme = genererSomme(tabNombres);
		moyenne = genererMoyenne(tabNombres);
		
		afficherReponses(tabNombres);
	}
	/**
	 * compter le temps
	 */
	@Override
	public void compter(){
		temps--;
	}
	/**
	 * d�cr�menter les resets
	 */
	@Override
	public void reset(){
		resets--;
	}
	/**
	 * V�rifier s'il reste encore des resets
	 * @return
	 */
	public boolean isResetsLeft(){
		boolean b = true;
		if(resets<0){
			resets = 0;
			b = false;
		}	
		return b;
	}
}
