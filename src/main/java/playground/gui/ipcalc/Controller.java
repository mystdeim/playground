package playground.gui.ipcalc;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import playground.helper.IPHelper;

public class Controller implements Initializable {
	
	private boolean isStable = false;

	@FXML private TextField fieldIPReadable;
	@FXML private TextField fieldIPBinary;
	@FXML private TextField fieldIPInt;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fieldIPReadable.textProperty().addListener(l -> {
			runBlockingOperation(() -> {
				fieldIPInt.setText(Integer.toUnsignedString(IPHelper.toInt(fieldIPReadable.getText())));
			});
		});
		fieldIPInt.textProperty().addListener(l -> {
			runBlockingOperation(() -> {
				fieldIPReadable.setText(
						IPHelper.toString(
								Integer.parseUnsignedInt(fieldIPInt.getText().replaceAll("\\s", ""))));
			});
		});
		isStable = true;
	}
	
	private boolean tryStableLock() {
		if (isStable) {
			isStable = false;
			return true;
		} else return false;
	}
	
	private void releaseStableLock() {
		isStable = true;
	}
	
	private void runBlockingOperation(Runnable task) {
		if (isStable) {
			isStable = false;
			try {
				task.run();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isStable = true;
			}
		}
	}

}
