package sl.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import sl.application.Song;

public class SLController {

	@FXML ListView<String> ListView ; 
	@FXML Button AddB ; 
	@FXML Button DeleteB ; 
	@FXML Button EditB ; 
	
	private ObservableList<String> ObsList ;
	
	public void init (Stage PrimaryStage){
		//Create an Observable List from an Array List 
		try {
			ObsList = FXCollections.observableArrayList(SongNameListFromSongsList(readStoredFile("saved.txt"))) ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//ObsList.s
		ListView.setItems(ObsList); 
		
		//By default Select the 1st song in the list
	    ListView.getSelectionModel().select(0);
		
		
	}
	
	//Makes a List of Strings SongNames from the List of Song objects returned by readStoredFile method 
	public List<String> SongNameListFromSongsList (List<Song> sl){
		List<String> songnames = new ArrayList<String>() ;
		for(int i = 0 ; i < sl.size() ; i++){
			Song temp = sl.get(i) ; 
			songnames.add(temp.getName()) ; 
		}
		return songnames ;
	}
	
	//Returns a List containing songs from the stored File
	private List<Song> readStoredFile(String FileLoc) throws Exception{
		String line = null;
		
		List<Song> SL = new ArrayList<Song>() ; 
		BufferedReader br = new BufferedReader(new FileReader(FileLoc)) ;
		String temp[] = null; 
		
		while((line = br.readLine()) != null ){
			temp = line.split(";") ; //temp is a String Array st temp[0] = Song name, temp[1] = Artist name, temp[2] and temp [3] are optional 
			if(temp.length == 2){
				Song s = new Song(temp[0], temp[1]) ; 
				SL.add(s) ; 
			}
		}
		br.close();
		return SL;
	}
	
	
	
}
