
/**
 * Classe utilitaire qui permet de sauvegarder dans un fichier binaire ou texte.
 * Elle petmer aussi de de r�cup�rer une boite.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

public class UtilitaireFichier {

	private Path dirSaves;
	private File fichier;
	private File[] listeFichiers;
	private int curseur = 0;
	/**
	 * S'il n'y a pas de partie � charger, c'est cette String qui sera utilis�e
	 */
	private String partieVierge = "1 0 false false 0,0,0,";
	
	/**
	 * Controlleur
	 * 
	 * Cr�er un dossier de sauvegarde si aucun n'existe
	 * 
	 */
	public UtilitaireFichier(){
		try {
			dirSaves = Paths.get("./sauvegardes/");
			//Cr�er un dossier dans le dossier du projet qui contiendra les fichiers de sauvegarde.
			if(!Files.exists(dirSaves)){
				Files.createDirectory(dirSaves);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateListeFichiers();
	}
	/**
	 * Cr�er un fichier au nom unique, puis g�n�rer une String � partir du gameModel. �crire la String dans le fichier
	 * @param model
	 */
	public void creerFichier(ModelTraining model) {
		try {
			int index = 1;
			//Cr�er nouveau fichier au nom unique
			do{
				fichier = new File(dirSaves.toString(), "sauvegarde"+index+".txt");
				index++;
			}while(fichier.exists());
			
			PrintWriter p = new PrintWriter(fichier, "UTF-8");
			//g�n�rer le contenu
			String contenu = model.genererSauvegarde();
			p.println(contenu);
			p.close();
			
			JOptionPane.showMessageDialog(null, "Sauvegarde r�ussie");			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	/**
	 * Charger une partie.
	 * Le curseur est incr�ment� et d�cr�ment� selon les boutons Pr�c�dent et Suivant. curseur%listeFichiers.length retourne
	 * un index qui se trouve toujours entre z�ro et le nombre de fichiers de sauvegardes. Ainsi, si l'utilisateur appuie
	 * sur "Suivant" ou "Pr�c�dent" plusieurs fois cons�cutives, on continue de naviguer � travers les fichiers sauvegard�s.
	 * 
	 * @return
	 */
	public ModelReplay recupererPartie(){
		String contenu = partieVierge;
		try {
			if(!isDirectoryEmpty()){
				int index = Math.abs(curseur%listeFichiers.length);
				Path filePath = Paths.get(dirSaves+"/"+listeFichiers[index].getName());
				BufferedReader reader = Files.newBufferedReader(filePath);
				contenu = reader.readLine();
			}
		} catch (IOException e) {	
			e.printStackTrace();
		}
		return new ModelReplay(contenu);
	}
	/**
	 * Cette proc�dure va chercher tous les fichiers de sauvegardes dans le dossier
	 */
	private void updateListeFichiers(){			
		//Ajouter dans un tableau toutes les sauvegardes contenues dans le dossier "sauvegardes"
		listeFichiers = null;
		listeFichiers = dirSaves.toFile().listFiles();
	}
	public ModelReplay partieSuivante(){
		curseur++;
		return recupererPartie();
	}
	public ModelReplay partiePrecedente(){
		curseur--;
		return recupererPartie();
	}
	/**
	 * V�rifier s'il existe des fichiers de sauvegarde
	 * @return
	 */
	public boolean isDirectoryEmpty(){
		updateListeFichiers();
		boolean b = true;
		if(listeFichiers.length>0){
			b = false;
		}
		return b;
	}
}