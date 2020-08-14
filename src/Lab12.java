import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lab12 extends Application{
	private ComboBox<String> setBase;
	private ListView<String> toppings;
	private RadioButton large, medium, small;
	private Label lb_base, lb_tops, lb_addorder, lb_order, 
		lb_total, lb_subtotal, lb_size;
	private TextField addtoorder;
	private TextField subtotal;
	private TextField total;
	private TextArea order;
	private Button addorder, orderit, clearit, clearall;
	private ObservableList<String> base =
			FXCollections.observableArrayList (
					"Plain", "Meat", "Margherita", 
					"Hawiian", "Supreme",
					"Bacon Cheese Burger", "Vegi");
	private ObservableList<String> tps =
			FXCollections.observableArrayList (
					"Peperoni", "Sausage",
					"Peppers", "Onions",
					"Beef", "Chicken", "Ham", "Mushrooms",
					"Cheese", "Bacon", "Spinach",
					"Olives", "Pineapple");
	
	String tbsize = "";
	double toppCost = 0.75;
	double sub, tot, szCost, subSize, subBase, subTopps, subCost, totCost, cost = 0.0;
	
	public void start(Stage primaryStage) {
		// areas to place the various components
		VBox pane = new VBox(15);
		pane.setPadding(new Insets(10, 0, 0, 10));
		HBox chk_pane = new HBox(15);
		chk_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox setBase_pane = new HBox(10);
		setBase_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox btn_pane = new HBox(10);
		btn_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox subtotal_pane = new HBox(10);
		subtotal_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox total_pane = new HBox(10);
		total_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox topping_pane = new HBox(10);
		topping_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox addorder_pane = new HBox(10);
		addorder_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox order_pane = new HBox(10);
		order_pane.setPadding(new Insets(10, 0, 0, 10));
		HBox l3_pane = new HBox(10);
		l3_pane.setPadding(new Insets(5, 0, 0, 10));
		
		CheckBox delivery = new CheckBox("Delivery");
		CheckBox carryOut = new CheckBox("Carry Out");
		chk_pane.getChildren().addAll(delivery, carryOut);
		
		 // label on pane for input text
        Label l = new Label("no text input"); 
  
        TextInputDialog td = new TextInputDialog(); 
 
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	td.setHeaderText(" ");
            	if(carryOut.isSelected()) {
            		td.setHeaderText("Enter your name");
            		delivery.setSelected(false);
            	}
                // show the text input dialog 
                td.showAndWait(); 
                // set the text of the label 
                l.setText(td.getEditor().getText());
            } 
        }; 
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {   	
            	if(delivery.isSelected()) {
            		td.setHeaderText("Enter your address");
            		carryOut.setSelected(false);
            		}
                // show the text input dialog 
                td.showAndWait(); 
                // set the text of the label 
                l.setText(td.getEditor().getText());
            } 
        }; 
        // set on action of event 
        delivery.setOnAction(event1);
        carryOut.setOnAction(event);
        chk_pane.getChildren().add(l); 
		
		// Select base pizza
		lb_base = new Label("Base Pizza:");
		setBase = new ComboBox<String>(base);
		setBase.setVisibleRowCount(4);
		setBase.setValue("None Selected"); // display the first one
		setBase.setOnAction(e -> {
			orderHandler();
		});
		
		// add method to add to text box
		setBase_pane.getChildren().addAll(lb_base, setBase);
		
		//pick toppings
		lb_tops = new Label("Toppings:");
		toppings = new ListView<String>(tps);
		toppings.setPrefSize(100,80);
		toppings.setOnMouseClicked(e -> {
			orderHandler();
		});
		// turn on multiple selections
		toppings.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		topping_pane.getChildren().addAll(lb_tops, toppings);
		
		// current order text field
		lb_addorder = new Label("Current selection:");
		addtoorder = new TextField();
		addtoorder.setEditable(false);
		addtoorder.setPrefColumnCount(25);

		addorder_pane.getChildren().addAll(lb_addorder, addtoorder);
		
		// total cost for current selection
		lb_subtotal = new Label("Subtotal:");
		subtotal = new TextField();
		subtotal.setEditable(false);
		subtotal.setPrefColumnCount(10);
		subtotal.setText("0.00");
		
		subtotal_pane.getChildren().addAll(lb_subtotal, subtotal);
		
		// total cost for complete order
		lb_total = new Label("Total:");
		total = new TextField();
		total.setEditable(false);
		total.setPrefColumnCount(35);
		total.setText("0.00");;
		
		total_pane.getChildren().addAll(lb_total, total);
		
		// text area listing all the selections waiting to complete order
		lb_order = new Label("Final Selections:");
		order = new TextArea();
		order.setEditable(false);
		order.setPrefColumnCount(25);

		order_pane.getChildren().addAll(lb_order, order);
	
		addorder = new Button("Add current to Order:");
		orderit = new Button("Place Order:");
		clearit = new Button("Clear current selection");
		clearall = new Button("Clear all");
		
		//Add radio buttons to select pizza size
		lb_size = new Label("Pizza size:");
		small = new RadioButton("Small");
		medium = new RadioButton("Medium");
		large = new RadioButton("Large");
		
		ToggleGroup sizes = new ToggleGroup();
		small.setToggleGroup(sizes);
		medium.setToggleGroup(sizes);
		large.setToggleGroup(sizes);
		
		Label l2 = new Label("nothing selected");
		
		sizes.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> 
			observable, Toggle oldValue, Toggle newValue) {
				// TODO Auto-generated method stub
				RadioButton sh = (RadioButton)sizes.getSelectedToggle();
				if (sh != null) { 
                    String s = sh.getText(); 
                    // change the label 
                    l2.setText(s); 
                } 
				if (small.isSelected()) {
					szCost = 9.95;
					tbsize = " Sm ";
				}
				else if (medium.isSelected()) {
					szCost = 10.95;
					tbsize = " Med ";
				}
				else if  (large.isSelected()){
					szCost = 12.95;
					tbsize = " Lg ";
				}
				subSize = szCost;
			}
			
		});
		
		btn_pane.getChildren().addAll(lb_size, small, medium, large, l2);
		
		Label l3 = new Label("");
		l3_pane.getChildren().add(l3);
		
		// Adds the current selection to the text area from the text box
		addorder.setOnMouseClicked(e -> { // implements Event Handler and override the handle method
			
			total.setText(Double.toString((Double.parseDouble(subtotal.getText())
					+(Double.parseDouble(total.getText())))));
			
			small.setSelected(false);
			medium.setSelected(false);
			large.setSelected(false);
			order.appendText(addtoorder.getText() + subtotal.getText() + "\n");
			toppings.getSelectionModel().clearSelection();
			setBase.getSelectionModel().clearSelection();
			addtoorder.setText("");
			subtotal.clear();
		});
		// finalizes the customers order from the text area
		orderit.setOnMouseClicked(e -> { // implements Event Handler and override the handle method
			l3.setText(">>>>>>>>>> Your order has been sent! >>>>>>>>>>>>");
			addtoorder.setText("");
			total.setText("");
			subtotal.setText("");
			small.setSelected(false);
			medium.setSelected(false);
			large.setSelected(false);
			order.setText("");
			toppings.getSelectionModel().clearSelection();
			setBase.getSelectionModel().clearSelection();
			orderHandler();
		});
		// clears the current selection
		clearit.setOnMouseClicked(e -> { // implements Event Handler and override the handle method
			addtoorder.setText("");
			subtotal.setText("");
			small.setSelected(false);
			medium.setSelected(false);
			large.setSelected(false);
			toppings.getSelectionModel().clearSelection();
			setBase.getSelectionModel().clearSelection();
		});
		// Clears the whole pane
		clearall.setOnMouseClicked(e -> { // implements Event Handler and override the handle method
			
			addtoorder.setText("");
			total.setText("");
			subtotal.setText("");
			small.setSelected(false);
			medium.setSelected(false);
			large.setSelected(false);
			order.setText("");
			toppings.getSelectionModel().clearSelection();
			setBase.getSelectionModel().clearSelection();
		});
		
		pane.getChildren().addAll(chk_pane, btn_pane, setBase_pane,
				  topping_pane, addorder_pane, addorder, subtotal_pane, clearit, 
				  order_pane, total_pane, orderit, l3_pane, clearall);
		
		Scene scene = new Scene(pane, 500, 800);
		scene.getStylesheets().add("MainStyler.css");
		primaryStage.setTitle("Pizza order");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	


public void orderHandler() {
		int count = 0;
		// get the base pizza
		String result = setBase.getValue(); // special that was picked
		if(result == "Plain") {
			subBase = 0.00;
		}
		else if(result == "Meat" || result == "Hawiian") {
			subBase = 2.00;
		}
		else if(result == "Margherita" || result == "Supreme" || result == "Bacon Cheese Burger") {
			subBase = 3.00;
		}
		else if(result == "Vegi") {
			subBase = 2.50;
		}
		
		// get the toppings
		for (String t:toppings.getSelectionModel().getSelectedItems()) {
			result = result + " with " + t + " ";
			//for every t I need to add $0.75 to subTopps
			count = count + 1;	
		
 		}
		subTopps = count * toppCost;
		addtoorder.setText(tbsize+result);
		subCost = subSize + subTopps + subBase;
		subtotal.setText(Double.toString(subCost));
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}
	
}
