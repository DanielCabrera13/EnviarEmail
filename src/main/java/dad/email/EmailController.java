package dad.email;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class EmailController implements Initializable {
	
	//MODEL
	
	StringProperty nombreIP = new SimpleStringProperty();
	StringProperty puerto = new SimpleStringProperty();
	BooleanProperty ssl = new SimpleBooleanProperty();
	StringProperty from = new SimpleStringProperty();
	StringProperty psswd = new SimpleStringProperty();
	StringProperty to = new SimpleStringProperty();
	StringProperty asunto = new SimpleStringProperty();
	StringProperty mensaje = new SimpleStringProperty();
	
	
	//VISTA
	
    @FXML
    private TextField asuntoText;

    @FXML
    private VBox buttonBox;

    @FXML
    private Button cerrarButton;

    @FXML
    private PasswordField claveText;

    @FXML
    private TextField destinatarioText;

    @FXML
    private Button enviarButton;

    @FXML
    private TextField puertoText;

    @FXML
    private TextField remitenteText;

    @FXML
    private TextField servidorText;

    @FXML
    private CheckBox sslCheck;

    @FXML
    private Button vaciadoButton;
    
    @FXML
    private TextArea mensajeText;

    @FXML
    private GridPane view;
    

    public EmailController() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/email.fxml"));
	loader.setController(this);
	loader.load();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//BINDINGS
		
		nombreIP.bind(servidorText.textProperty());
		puerto.bind(puertoText.textProperty());
		ssl.bind(sslCheck.selectedProperty());
		from.bind(remitenteText.textProperty());
		psswd.bind(claveText.textProperty());
		to.bind(destinatarioText.textProperty());
		asunto.bind(asuntoText.textProperty());		
		mensaje.bind(mensajeText.textProperty());
		
	}
	
	@FXML
    void onCerrarAction(ActionEvent e) {
		
		Node source = (Node) e.getSource(); // Me da el elemento en el que hice click
		Stage stage = (Stage) source.getScene().getWindow();// Me devuelve la ventana donde se encuentra dicho elemento
		stage.close(); // me cierra la ventana

    }

    @FXML
    void onEnviarAction(ActionEvent e) {
    	
    	Task<Void> emailTask = new Task<Void>() {

			protected Void call() throws Exception {
				Thread.sleep(5000L);

				int puertoNumber = Integer.parseInt(puerto.get());

				Email email = new SimpleEmail();
				email.setHostName(nombreIP.get());
				email.setSmtpPort(puertoNumber);
				email.setAuthenticator(new DefaultAuthenticator(from.get(), psswd.get()));
				email.setSSLOnConnect(ssl.get());
				email.setFrom(from.get());
				email.setSubject(asunto.get());
				email.setMsg(mensaje.get());
				email.addTo(to.get());
				email.send();
				
				return null;
			}
		};
		
		emailTask.setOnSucceeded(event -> {
			
			Alert alertInf = new Alert(AlertType.INFORMATION);
	    	Stage stage = (Stage) alertInf.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/email-send-icon-32x32.png")));
			alertInf.setTitle("Mensaje enviado");
			alertInf.setHeaderText("Mensaje enviado con éxito a: "+from.get());
			alertInf.showAndWait();
			
			destinatarioText.clear();
			asuntoText.clear();
			mensajeText.clear();
			;
		});
		
		emailTask.setOnFailed(event -> {
			
			Alert alertError = new Alert(AlertType.ERROR);
    		Stage stage = (Stage) alertError.getDialogPane().getScene().getWindow();
    		stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/email-send-icon-32x32.png")));
			alertError.setTitle("Error");
			alertError.setHeaderText("No se pudo enviar el email.");
			alertError.setContentText("Error producido: " + event.getSource().getException().getMessage());
			alertError.showAndWait();
		});
		
		new Thread(emailTask).start();

    }

    @FXML
    void onVaciarAction(ActionEvent e) {
    	
    	servidorText.clear();
    	puertoText.clear();
    	sslCheck.setSelected(false);
    	remitenteText.clear();
    	claveText.clear();
    	destinatarioText.clear();
		asuntoText.clear();
		mensajeText.clear();

    }

	
	public GridPane getView() {
		return view;
	}

}
