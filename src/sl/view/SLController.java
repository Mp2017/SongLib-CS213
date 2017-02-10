//Maxim Torubarov, Mihir Patel

package sl.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import sl.application.Song;
import sl.application.SongNameComparator;

public class SLController {

	@FXML ListView<Song> ListView ; 
	@FXML Button AddB ; 
	@FXML Button DeleteB ; 
	@FXML Button EditB ; 
	@FXML Label SNL ; //Song Name Label in Details section 
	@FXML Label AL ; //Artist Name Label 
	@FXML Label ALL ;//Album name label 
	@FXML Label YL ; //Year Label 
	
	//Edit/Add TextFields
	@FXML TextField SNLEdit ; //Song Name Label in Details section 
	@FXML TextField ALEdit ; //Artist Name Label 
	@FXML TextField ALLEdit ;//Album name label 
	@FXML TextField YLEdit ; //Year Label 
	
	 Stage primaryStage;
	
	//list of song objects
	private ObservableList<Song> ObsList ;
	
	public void init (Stage PrimaryStage){
		primaryStage=PrimaryStage;
		
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
	    
	    //Displays the details of the 1st song
	    Song selectedSong=ListView.getSelectionModel().getSelectedItem();
	    if(selectedSong != (null)){
	    	DisplayDetails(selectedSong) ;
	    }else{
	    	SNL.setText("");
	    	AL.setText("");
	    	ALL.setText("");
	    	YL.setText("");
	    }
	    
	    //REgistering a Listener for the Selected Song name on the LsitView
	    ListView
	    	.getSelectionModel()
	    	.selectedItemProperty()
	    	.addListener(
	    			( obs, oldVal , newVal ) -> DisplayDetails(newVal) ) ; 
		
	}//init ends

	//displays the details of the song in the labels
	private void DisplayDetails(Song s) {
		//if try to delete song that doesnt exit, return
		if(s==null){
			return;
		}
		
		//set the song name
		SNL.setText(s.getName());
		
		//set the artist
		AL.setText(s.getArtist());
		
		//set the album
		if(s.getAlbum() == null|| s.getAlbum().equals("")) {
			ALL.setText("Album Not Available");
		}else { 
			ALL.setText(s.getAlbum());
		}
		
		//set the year
		if(s.getYear() == null||s.getYear().equals("") ){
			YL.setText("Year Not Available"); 
		}
		else { 
			YL.setText(s.getYear());
		}
	}

	//onAction for the delete button
   	public void DeleteSong(ActionEvent e){
   		//Get the selected song and its index
	    Song selectedSong=ListView.getSelectionModel().getSelectedItem();
	    int selectedIndex=ListView.getSelectionModel().getSelectedIndex();

	    //set up the popup
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    //alert.setHeaderText();
	    alert.setHeaderText("Are you sure you want to delete the selected song");
	    Optional<ButtonType> result=alert.showAndWait();
	    
	    //if the user presses ok, then go through with the action
	    if(result.get()==ButtonType.OK){
	    	//delete the selected song from the obsList
		    for(int i=0; i<ObsList.size();i++){
		    	if(ObsList.get(i).equals(selectedSong)){
		    		ObsList.remove(i);
		    		break;
		    	}
		    }
		    	
		    //if there is nothing left in the obsList, set all the display to nulls
		    if(ObsList.size() == 0 ){
		    	ObsList = FXCollections.observableArrayList() ; 
		    	SNL.setText("");
		    	AL.setText("");
		    	ALL.setText("");
		    	YL.setText("");
		    }
		    //ListView.setItems(ObsList); ? //Not Needed cz Updating Observable List automatically refreshes the ListView
		    //error when nothing in list
		 
		    //if the song we just deleted index is not 0, then we go on to the next song
		    if(selectedIndex!=0){
		    	ListView.getSelectionModel().select(selectedIndex);
		    }
		//otherwise, return
	    }else{
	    	return;
	    }
	    
	    
   	}	
	
   	//onAction for the edit button
   	public void EditSong(ActionEvent e){
   		//get the selected song
   		Song selectedSong=ListView.getSelectionModel().getSelectedItem();
   		
   		//first check if there are any songs to even edit
   		if(selectedSong == (null)){
	    	displayPopUp("There are no songs to edit.");
	    	return;
	    }
   		
   		//next check if anything is written at all and if it's not, display error
   		if(SNLEdit.getText().trim().equals("") || ALEdit.getText().trim().equals("")){
   			displayPopUp("Please input at least the name of the song and the artist in the text fields before clicking the Edit button.");
   			return;
   		}
   		
   		//now get the new parameters
   		String newName=SNLEdit.getText().trim();
   		String newArtist=ALEdit.getText().trim();
   		String newAlbum=ALLEdit.getText().trim();
   		String newYear=YLEdit.getText().trim();
   		
   		//check if artist/name is already in the list except for the song that is currently selected
   		for(int i=0; i<ObsList.size();i++){
   			Song song =ObsList.get(i);
   			if(song!=selectedSong){
   	   	   		if(song.getName().toLowerCase().equals(newName.toLowerCase()) && song.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
   					//this song exists so pop message and return;
   					displayPopUp("This song already exists in the list. Please enter another artist and/or name");
   		   			return;
   				}
   			}
   		}
   		
   		//set up the popup
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    //alert.setHeaderText();
	    alert.setHeaderText("Are you sure you want to edit the selected song by replacing its details with your input in the text fields");
	    Optional<ButtonType> result=alert.showAndWait();
	    
	    //if the user presses ok, then go through with the action
	    if(result.get()==ButtonType.OK){
	   		//change the name, artist and albums of the selected song
	   		selectedSong.setName(newName);
	   		selectedSong.setArtist(newArtist);
	   		if(newAlbum!=""){
	   			selectedSong.setAlbum(newAlbum);
	   		}else{
	   			selectedSong.setAlbum("");
	   		}
	   		
	   		//if year isnt number....
	   		if(newYear!=""){
	   			selectedSong.setYear(newYear);
	   		}else{
	   			selectedSong.setYear("");
	   		}
	   		
	   		FXCollections.sort(ObsList, new SongNameComparator() );
			ListView.setItems(ObsList); 
	   		DisplayDetails(selectedSong);
	   		
	   		
	   		//in end clear text fields
	   		SNLEdit.clear();
	   		ALLEdit.clear();
	   		ALEdit.clear();
	   		YLEdit.clear();
	    }else{
	    	return;
	    }
   	}	
	
   	//displays popups for errors given a string to show
   	public void displayPopUp(String s){
   		Stage dialog = new Stage();
   		dialog.setTitle("ERROR");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        HBox dialogHbox = new HBox(20);
        Text t=new Text(s);
        t.setX(50);
        t.setY(50);
        dialogHbox.setAlignment(Pos.CENTER);
        dialogHbox.getChildren().add(t);
        
        Scene dialogScene = new Scene(dialogHbox, t.maxWidth(0)+50, t.maxHeight(0)+50);
        
       dialog.setResizable(false);
        dialog.setScene(dialogScene);
        dialog.show();
   	}

   	public void AddSong(ActionEvent e){
   		//first check if anything is written at all and if it's not, display error that 
   		//"Please input at least the name of the song and the artist below before clicking this button" 
   		//System.out.println("We in edit:"+SNLEdit.getText().trim()+":");
   		if(SNLEdit.getText().trim().equals("") || ALEdit.getText().trim().equals("")){
   			displayPopUp("Please input at least the name of the song and the artist in the text fields before clicking the Add button.");
   			return;
   		}
   		
   		
   		//now add the song
   		String newName=SNLEdit.getText().trim();
   		String newArtist=ALEdit.getText().trim();
   		String newAlbum=ALLEdit.getText().trim();
   		String newYear=YLEdit.getText().trim();
   		
   		//check if artist/name is already in the list except for the song that is currently selected
   		for(int i=0; i<ObsList.size();i++){
   			Song song =ObsList.get(i);
			if(song.getName().toLowerCase().equals(newName.toLowerCase()) && song.getArtist().toLowerCase().equals(newArtist.toLowerCase())){
				//this song exists so pop message and return;
				displayPopUp("This song already exists in the list. Please enter another artist and/or name");
	   			return;
			}
   			
   		}
   		
   	//set up the popup
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirmation");
	    //alert.setHeaderText();
	    alert.setHeaderText("Are you sure you want to add a song with the details specified by the text fields");
	    Optional<ButtonType> result=alert.showAndWait();
	    
	    //if the user presses ok, then go through with the action
	    if(result.get()==ButtonType.OK){
	   		
	   		Song song = new Song(newName, newArtist);
	  
	   		song.setAlbum(newAlbum);
	
	   		
	   		//if year isnt number....
	   		song.setYear(newYear);
	   		
	   		ObsList.add(song);
	   		FXCollections.sort(ObsList, new SongNameComparator() );
			ListView.setItems(ObsList); 
	   		DisplayDetails(song);
	   		
	   		//returns -1 if no song found otherwise returns the index in the Obslist
	   		int index= findSongArtist(song.getName(), song.getArtist());
	   		ListView.getSelectionModel().select(index);
	   		
	   		//in end clear text fields
	   		SNLEdit.clear();
	   		ALLEdit.clear();
	   		ALEdit.clear();
	   		YLEdit.clear();
	    }else{
	    	return;
	    }
   	}	
	
	public int findSongArtist(String name, String artist){
		for(int i=0; i<ObsList.size();i++){
   			Song song =ObsList.get(i);
			if(song.getName().toLowerCase().equals(name.toLowerCase()) && song.getArtist().toLowerCase().equals(artist.toLowerCase())){
				//this song exists so pop message and return;
				return i;
			}
   			
   		}
		return -1;
	}
   	
   	
	//When the USer quits the app, this method should be called in stop() of SongLibApp, and should iterate over the ArrayList of songs and overwrite the "Saved.txt"  
	public void quit(){
		BufferedWriter writer = null ; 
		try{
			File fnew = new File("saved.txt") ; 
			writer = new BufferedWriter (new FileWriter(fnew)) ;
			
			String temp = null ; 
			
			//Iterate over the entire ArrayList forms a String and writes it out to the file  
			for(Song s : ObsList){
				temp = s.getName() + ";" + s.getArtist() + ";" ;
				if(s.getYear() == "" ){
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
					Song t = new Song(temp[0], temp[1] , temp[2], "" ) ;
					SL.add(t) ; 
					break;
				case 4: 
					if(temp[2].equals("") || temp[2] == null ){
						Song u = new Song(temp[0], temp[1] ,"",temp[3]) ;
						SL.add(u) ; 
					}else{
						Song v = new Song(temp[0] , temp[1] , temp[2] , temp[3] ) ; 
						SL.add(v) ; 
					}
					break ; 
			}	
			
		}//While loop ends 
		br.close();
		return SL;
	}
	
}