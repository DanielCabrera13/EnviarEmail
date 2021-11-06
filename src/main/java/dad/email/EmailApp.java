package dad.email;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EmailApp extends Application {
	
	private EmailController emailController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		emailController  = new EmailController();
		
		Scene scene = new Scene(emailController.getView());
		
		primaryStage.setTitle("Enviar email");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/email-send-icon-32x32.png")));
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

}
