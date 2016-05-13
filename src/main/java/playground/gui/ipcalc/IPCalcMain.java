package playground.gui.ipcalc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IPCalcMain extends Application {
	
	public static void main(String... args) { 
		launch(args);
	}
	
	@Override
	public void start(final Stage stage) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
//			FXMLLoader loader = new FXMLLoader(
//					getClass().getResource("C:/workspace/playground/src/main/resources/playground/gui/ipcacl/view.fxml"));
	        final Parent root = loader.load();
	        final Controller controller = loader.getController();
	        
//	        final Scene scene = new Scene(root, 500, 500);
	        final Scene scene = new Scene(root);
//	        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
//	        scene.getStylesheets().addAll(Main.class.getResource("application.css").toExternalForm());
//	        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//	            @Override
//	            public void handle(KeyEvent event) {
//	                switch (event.getCode()) {
//	                    case F11:
//	                    	stage.setFullScreen(!stage.isFullScreen());
//	                    	break;
//					default:
//						break;
//	                }
//	            }
//	        });
	        
	        stage.setScene(scene);
	        stage.setTitle("IP Calc");
	        stage.setOnCloseRequest(e -> {
	        	controller.saveSettings();
	        });
	//        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png"))); 
	        stage.show();
	        
		} catch (Exception e) {
			e.printStackTrace();
//			logError(TEXT_START, e);
//			createDialog(TEXT_START, e);
		}
	}

}
