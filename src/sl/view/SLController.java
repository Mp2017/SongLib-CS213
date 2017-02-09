package sl.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListView.EditEvent;
import javafx.stage.Stage;

import sl.application.Song;
import sl.application.SongNameComparator;

public class SLController {

	@FXML ListView<Song> ListView ; 
	@FXML Button AddB ; 
	@FXML Button DeleteB ; 
	@FXML Button EditB ; 
	@FXML Label SNL ; //Song Name LAbel in Details section 
	@FXML Label AL ; //Artist Name Label 
	@FXML Label ALL ;//Album name label 
	@FXML Label YL ; //Year Label 
	
	//list of song objects
	private ObservableList<Song> ObsList ;
	
	public void init (Stage PrimaryStage){
		//Create an Observable List from an Array List 
		try {
			ObsList = FXCollections.observableArrayList(readStoredFile("saved.txt")) ;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		// SORTing the OBsList based on the Song NAme field before setting the Listview items
		FXCollections.sort(ObsList, new SongNameComparator() );
		ListView.setItems(ObsList); 
		
		
		//By default Select the 1st song in the list
	    ListView.getSelectionModel().select(0);
		
		//now display the information for the first song
	  //first, get the song
	    Song selectedSong=ListView.getSelectionModel().getSelectedItem();
	    SNL.setText(selectedSong.getName());
	    AL.setText(selectedSong.getArtist());
	    ALL.setText(selectedSong.getAlbum());
	    YL.setText(""+selectedSong.getYear());
	    
	    ListView.getSelectionModel().selectedItemProperty().addListener(
	    		new ChangeListener<Song>(){
	    	    	//@Override
	    	    	public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newVale){
	    	    		Song selectedSong=ListView.getSelectionModel().getSelectedItem();
	    	    	    SNL.setText(selectedSong.getName());
	    	    	    AL.setText(selectedSong.getArtist());
	    	    	    ALL.setText(selectedSong.getAlbum());
	    	    	    YL.setText(""+selectedSong.getYear());
	    	    	}
	    	    	
	    	    }
	    
	    		
	    		
	    );
	    
	    DeleteB.setOnAction(new EventHandler<ActionEvent>(){
	    	public void handle(ActionEvent e){
	    		Song selectedSong=ListView.getSelectionModel().getSelectedItem();
	    		//delete the song that is selected from the list
	    		//first find the song in observablelist
	    		for(int i=0; i<ObsList.size();i++){
	    			if(ObsList.get(i)==selectedSong){
	    				ObsList.remove(i);
	    				break;
	    			}
	    		}
	    		
	    		//ListView.setItems(ObsList); ?
	    		
	    		//error when nothing in list
	    	}
	    	
	    	
	    	
	    });
	}
	
	
	
	//When the USer quits the app, this method should be called in stop() of SongLibApp, and should iterate over the ArrayList of songs and overwrite the "Saved.txt"  
	public void quit(){
		BufferedWriter writer = null ; 
		try{
			File fnew = new File("saved.txt") ; 
			writer = new BufferedWriter (new FileWriter(fnew)) ;
			
			String temp = null ; 
			System.out.println("Checking the ObsList before writing to File");
			
			System.out.println(ObsList==null);
			for(Song s : ObsList){
				System.out.println(s.toString());
			}
			
			//Iterate over the entire ArrayList forms a String and writes it out to the file  
			for(Song s : ObsList){
				temp = s.getName() + ";" + s.getArtist() + ";" ;
				if(s.getYear() == 0 ){
					temp = temp + ";" ; 
				}else{
					temp = temp + s.getYear() +";" ; 
				}
				temp = temp + s.getAlbum() + ";" ; 
				writer.write(temp);
				writer.newLine(); 
				temp = null ; 
			}	
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try{
				writer.close(); 
			}catch(Exception e){
			
			}
		}
	}
	
	//DEPRECATED METHOD 
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
				
		while((line = br.readLine()) != null ){
			String temp[] = null; //Array of Strings
			temp = line.split(";") ; //temp is a String Array st temp[0] = Song name, temp[1] = Artist name, temp[2] and temp [3] are optional 
			int arrLen = temp.length ; 
			
			if(arrLen < 2 ) 
				System.err.println("Error in the file format, Needs to have Song Name and Artist name at least");
			
			switch(arrLen){
				case 2:
					Song s = new Song(temp[0], temp[1] ) ;
					SL.add(s) ; 
					break ; 
				case 3:		
					Song t = new Song(temp[0], temp[1] , Integer.parseInt(temp[2]) ) ;
					SL.add(t) ; 
					break;
				case 4: 
					if(temp[2].equals("") || temp[2] == null ){
						Song u = new Song(temp[0], temp[1] ,temp[3]) ;
						SL.add(u) ; 
					}else{
						Song v = new Song(temp[0] , temp[1] , Integer.parseInt(temp[2]) , temp[3] ) ; 
						SL.add(v) ; 
					}
					break ; 
			}	
			
		}//While loop ends 
		br.close();
		return SL;
	}
	
}
