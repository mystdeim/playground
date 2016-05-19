package playground.gui.networkscanner;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.LongAdder;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import playground.gui.util.UserSettings;
import playground.gui.util.UserSettingsSavable;
import playground.helper.IPHelper;
import playground.util.NetworkScanner;
import playground.util.NetworkScanner.Listener;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;

public class Controller implements Initializable, UserSettingsSavable {
	
	public static final String TEXT_READY = "Ready";
	public static final String TEXT_START = "Start";
	public static final String TEXT_STOP = "Stop";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		data = FXCollections.observableArrayList();
		accumulator = new LongAdder();
		
		scanner = new NetworkScanner();
		scanner.addListener(new Listener() {
			@Override
			public void ping(int ip) {
				
			}
			@Override
			public void pong(int ip, boolean isReachable) {
				Row row = new Row(ip, isReachable);
				accumulator.increment();
				Platform.runLater(() -> {
					data.add(row);
					lblSummary.setText(accumulator.toString());
					double progress = accumulator.doubleValue() / (double) count;
					progressBarStatus.setProgress(progress);
					if (progress < 1) {
						lblStatus.setText(String.format("%.1f%%",  progress * 100));
					} else {
						lblStatus.setText(TEXT_READY);
					}
				});
			}
		});
//		fieldNetwork.setText("10.1.1.35-10.1.1.38");
//		fieldNetwork.setText(UserSettings.);
		
		tableNetwork.setItems(data);
		columnIP.setCellValueFactory(c -> c.getValue().ip);
		columnIP.setCellFactory(c -> {
			return new TableCell<Row, Number>() {
				@Override
				protected void updateItem(Number item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) this.setText("");
					else this.setText(IPHelper.toString(item.intValue()) + " " + item.intValue());
				};
			};
		});
//		columnIP.setComparator(new Comparator<Number>() {
//			@Override
//			public int compare(Number o1, Number o2) {
//				return new BigDecimal(o1.toString()).compareTo(new BigDecimal(o2.toString()));
//			}
//		});
		columnIsReachable.setCellValueFactory(c -> c.getValue().isReachable);
		columnIsReachable.setCellFactory(c -> {
			return new TableCell<Row, Boolean>() {
				@Override
				protected void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) this.setText("");
					else {
						if (item) {
							this.setTextFill(Color.GREEN);
							this.setText("online");
						} else {
							this.setTextFill(Color.RED);
							this.setText("offline");
						}
					}
				};
			};
		});
		
		progressBarStatus.visibleProperty().bind(
				progressBarStatus.progressProperty().lessThan(1)
		);
		
		loadSettings();
	}
	
	// Properties
	// -----------------------------------------------------------------------------------------------------------------

	
	private NetworkScanner scanner;
	private ObservableList<Row> data;
	private LongAdder accumulator;
	private long count;
	
	// FXML
	// -----------------------------------------------------------------------------------------------------------------
	
	@FXML private TextField fieldNetwork;
	@FXML private TableView<Row> tableNetwork;
	@FXML private Button btnStart;
	@FXML TableColumn<Row, Number> columnIP;
	@FXML TableColumn<Row, Boolean> columnIsReachable;
	@FXML ProgressBar progressBarStatus;
	@FXML Label lblStatus;
	@FXML Label lblSummary;
	
	@FXML public void onStart(ActionEvent event) {
		Runnable r = () -> {
			Platform.runLater(() -> {
				data.clear();
				lblSummary.setText("");
				btnStart.setText(TEXT_STOP);
			});
			scanner.setSubnet(fieldNetwork.getText().trim());
			count = scanner.getRangeSize();
			accumulator.reset();
			scanner.runParallel();
			Platform.runLater(() -> {
				btnStart.setText(TEXT_START);
			});
		};
		new Thread(r).start();
	}
	
	// UserSettings
	// -----------------------------------------------------------------------------------------------------------------
	
	public static final String USER_SETTINGS_NETWORK = "network";
	
	private void loadSettings() {
		UserSettings settings = getUserSettings();
		fieldNetwork.setText(settings.get(USER_SETTINGS_NETWORK));
	}
	
	@Override
	public void saveSettings() {
		UserSettings settings = getUserSettings();
		settings.put(USER_SETTINGS_NETWORK, fieldNetwork.getText());
	}
	
	// Row
	//------------------------------------------------------------------------------------------------------------------
	
	private class Row {
		public final IntegerProperty ip;
		public final BooleanProperty isReachable;
		public Row(int ip, boolean isReachable) {
			this.ip = new SimpleIntegerProperty(ip);
			this.isReachable = new SimpleBooleanProperty(isReachable);
		}
	}

}
