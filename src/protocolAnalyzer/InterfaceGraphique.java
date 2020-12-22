package protocolAnalyzer;

import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.stage.Stage;
import tools.TraceFile;
import tools.Traces;
import javafx.geometry.*;  
import javafx.scene.text.*;  
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import java.io.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter; 


	public class InterfaceGraphique  extends Application implements EventHandler<ActionEvent> {
		private BufferedWriter outer = null;
		private BufferedReader reader = null;
		  private int lineCount = 0;
		  private Button save;
		  private Button buttonSelectFile;
		  private Button buttonReadLine;
		  private Button buttonSelectOutFile;
		  private Label labelFileName;
		  private Text textLineFields;
		  private Label result;
		  private static final int WINDOW_WIDTH  = 1000;
		  private static final int WINDOW_HEIGHT = 600;
		  private String string="";
		  @Override
		  public void start(Stage primaryStage)
		  {
		    primaryStage.setTitle("ProtocolAnalyzer");
		    primaryStage.setResizable(false);

		    buttonSelectFile = new Button("Select Input Text File");
		    buttonSelectOutFile = new Button("Select Output Text File");
		    buttonSelectFile.setOnAction(this);
		    buttonSelectOutFile.setOnAction(this);
		    save=new Button("Save");
		    save.setOnAction(this);
		    buttonReadLine = new Button("Analyze");
		    buttonReadLine.setOnAction(this);
		    buttonReadLine.setDisable(true); //User cannot read line until a file has been opened.
		    
		    labelFileName = new Label();
		    textLineFields = new Text();
		    StackPane root = new StackPane();
		    
		   

		    //Make both buttons the same width.
		    buttonSelectFile.setMaxWidth(WINDOW_WIDTH/2);
		    labelFileName.setMaxWidth(WINDOW_WIDTH/2);
		    buttonSelectOutFile.setMaxWidth(WINDOW_WIDTH/2);
		    buttonReadLine.setMaxWidth(WINDOW_WIDTH/2);
		    save.setMaxWidth(WINDOW_WIDTH/2);
		    final FlowPane buttonBox = new FlowPane();
		    buttonBox.setPadding(new Insets(10, 10, 10, 10));  //Sets the space around the buttonBox.
		    ScrollPane scrollPane = new ScrollPane();
	        scrollPane.setContent(buttonBox);
	        
	        // Pannable.
	        scrollPane.setPannable(true);
	        Scene scene = new Scene(scrollPane, 1000, 800);
		    buttonBox.getChildren().addAll(
		      save,
		      buttonSelectOutFile,
		      buttonSelectFile,
		      labelFileName,
		      buttonReadLine,
		      textLineFields
		    );

		    
		    root.getChildren().add(buttonBox);
		    primaryStage.setScene(scene);
		    primaryStage.show();
		  }
		  private boolean openFile()
		  {
		    FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Open Text File");
		    fileChooser.setInitialDirectory(new File("."));
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		    File selectedFile = fileChooser.showOpenDialog(null);
		    if (selectedFile == null)
		    {
		      String fileName = "Fox.txt";
		      try
		      {
		        reader = new BufferedReader(new FileReader("data/"+fileName));
		      }
		      catch (IOException e)
		      { return false;
		      }
		      labelFileName.setText(fileName);
		      buttonReadLine.setDisable(false);
		    }

		    else
		    {
		      try
		      {
		        reader = new BufferedReader(new FileReader(selectedFile));
		        buttonReadLine.setDisable(false);
		      }
		      catch (IOException e)
		      { showErrorDialog("IO Exception: " + e.getMessage());
		        return false;
		      }
		      labelFileName.setText(selectedFile.getName());
		    }


		    textLineFields.setText("");
		    lineCount = 0;
		    return true;
		  }
		  
		  private boolean openFileOut()
		  {
		    FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Open Text File");
		    fileChooser.setInitialDirectory(new File("."));
		    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		    File selectedFile = fileChooser.showOpenDialog(null);
		    if (selectedFile == null)
		    {
		      String fileName = "Fox.txt";
		      try
		      {
		        outer = new BufferedWriter(new FileWriter("data/"+fileName));
		      }
		      catch (IOException e)
		      { return false;
		      }
		      labelFileName.setText(fileName);
		      buttonReadLine.setDisable(false);
		    }

		    else
		    {
		      try
		      {
		        outer = new BufferedWriter(new FileWriter(selectedFile));
		        buttonReadLine.setDisable(false);
		      }
		      catch (IOException e)
		      { showErrorDialog("IO Exception: " + e.getMessage());
		        return false;
		      }
		      labelFileName.setText(selectedFile.getName());
		    }


		    textLineFields.setText("");
		    lineCount = 0;
		    return true;
		  }

		  private String readLineIntoFields()
		  {
			Traces trames=new Traces();
		    String str = "";
		    try
		    {
		    	TraceFile.FileToTracesGraphique(trames,reader);
		    }
		    catch (IOException e)
		    { showErrorDialog("readWordsOnLine(): IO Exception: " + e.getMessage());
		      e.printStackTrace();
		      System.exit(0);
		    }
		    for(Trace trame:trames.getTraces()) {
		    	str+=trame.toString();
		    }
		    string=str;
		    return  str;
		  }
		  private void whriteIntoFile() throws IOException
		  {
		    	Trace.stringToFileGraph(outer,string);
		
		  }
		  
		  @Override
		  public void handle(ActionEvent event)
		  {
		    Object source = event.getSource();

		    if (source == buttonSelectFile) openFile();
		    else if (source == buttonReadLine) displayNextRecord();
		    else if(source==buttonSelectOutFile)  openFileOut();
		    else if(source==save) {
				try {
					whriteIntoFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		  }
		  private void displayNextRecord()
		  {
		  

		    String fieldList = readLineIntoFields();

		    if (fieldList == "")
		    {
		      textLineFields.setText("le fichier est vide");
		      buttonReadLine.setDisable(true);
		      return;
		    }
		    textLineFields.setText(fieldList);
		  }
		  private void showErrorDialog(String msg)
		  {
		    Alert alert = new Alert(Alert.AlertType.ERROR);
		    alert.setTitle("Error");
		    alert.setContentText(msg);
		    alert.showAndWait();
		  }
		  public static void main(String[ ] args)
		  {
		    launch(args);
		  }

		}
