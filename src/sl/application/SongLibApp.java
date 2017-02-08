package sl.application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import sl.view.SLController;


public class SongLibApp extends Application {
	
	SLController slc ; 
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		
			FXMLLoader loader = new FXMLLoader() ; 
			loader.setLocation(getClass().getResource("/sl/view/SL.fxml")) ; //Loads the fxml file to create the GUI objects from 
			
			AnchorPane root = (AnchorPane)loader.load(); //Top Level Container 
			
			slc = loader.getController();
		    slc.init(primaryStage); //Calling the init method of the SLController 
		    
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("SongApp");
			primaryStage.setResizable(false);
			primaryStage.show();
	}
	
	@Override
	public void stop(){
		 slc.quit(); 
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
