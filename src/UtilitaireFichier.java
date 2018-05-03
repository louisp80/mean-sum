
/**
 * Classe utilitaire qui permet de sauvegarder dans un fichier binaire ou texte.
 * Elle petmer aussi de de récupérer une boite.
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
	 * S'il n'y a pas de partie à charger, c'est cette String qui sera utilisée
	 */
	private String partieVierge = "1 0 false false 0,0,0,";
	
	/**
	 * Controlleur
	 * 
	 * Créer un dossier de sauvegarde si aucun n'existe
	 * 
	 */
	public UtilitaireFichier(){
		try {
			dirSaves = Paths.get("./sauvegardes/");
			//Créer un dossier dans le dossier du projet qui contiendra les fichiers de sauvegarde.
			if(!Files.exists(dirSaves)){
				Files.createDirectory(dirSaves);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateListeFichiers();
	}
	/**
	 * Créer un fichier au nom unique, puis générer une String à partir du gameModel. Écrire la String dans le fichier
	 * @param model
	 */
	public void creerFichier(ModelTraining model) {
		try {
			int index = 1;
			//Créer nouveau fichier au nom unique
			do{
				fichier = new File(dirSaves.toString(), "sauvegarde"+index+".txt");
				index++;
			}while(fichier.exists());
			
			PrintWriter p = new PrintWriter(fichier, "UTF-8");
			//générer le contenu
			String contenu = model.genererSauvegarde();
			p.println(contenu);
			p.close();
			
			JOptionPane.showMessageDialog(null, "Sauvegarde réussie");			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	/**
	 * Charger une partie.
	 * Le curseur est incrémenté et décrémenté selon les boutons Précédent et Suivant. curseur%listeFichiers.length retourne
	 * un index qui se trouve toujours entre zéro et le nombre de fichiers de sauvegardes. Ainsi, si l'utilisateur appuie
	 * sur "Suivant" ou "Précédent" plusieurs fois consécutives, on continue de naviguer à travers les fichiers sauvegardés.
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
	 * Cette procédure va chercher tous les fichiers de sauvegardes dans le dossier
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
	 * Vérifier s'il existe des fichiers de sauvegarde
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