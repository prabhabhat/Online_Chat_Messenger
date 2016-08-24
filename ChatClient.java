import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

 public class ChatClient extends Application
  {
   DataOutputStream toServer = null;    // Output stream to carry data out from client
   DataInputStream fromServer = null;   // Input stream to bring data into the client
   String clientmsg=null,servermsg=null;  // the client and server messages
      
   public static void main(String[] args)   // main method to invoke chatClient.
   {
	Application.launch(args);
   }
   
   @Override 
   public void start(Stage primaryStage)    // Override the start method in the Application class 
    {
     BorderPane paneForTextField1 = new BorderPane();   // Panel to hold the label and text fields
	 paneForTextField1.setPadding(new Insets(5, 5, 5, 5));
	 paneForTextField1.setStyle("-fx-border-color: red");
	 paneForTextField1.setLeft(new Label("Enter a message: "));  // Label for textField

     TextField tfc = new TextField();       
	 tfc.setAlignment(Pos.BOTTOM_RIGHT);
	 paneForTextField1.setCenter(tfc);

	 BorderPane mainPane = new BorderPane();
	 TextArea tac = new TextArea();   	 // Text area to display conversation.
	 mainPane.setCenter(new ScrollPane(tac));
	 mainPane.setTop(paneForTextField1);

	 Scene scene = new Scene(mainPane, 400, 250);    // Create a scene and place it in the stage
	 primaryStage.setTitle("Chat Client"); // providing the stage title
	 primaryStage.setScene(scene); // Place the scene in the stage
	 primaryStage.show(); // Display the stage

      tfc.setOnAction(e ->             // set action for text field
       {
        try
           {
			 clientmsg = tfc.getText().trim();   // Get the client message from the text field
	 		 toServer.writeUTF(clientmsg);       // Send the message to the server
			 toServer.flush();
			 
			 tac.appendText("Client message : " + clientmsg + '\n');     // Display the client message in the text area
			 tfc.setText("");                  // clear the text field
		      
	     	 servermsg = fromServer.readUTF();   // Get message from the server
			 tac.appendText("Server message : " + servermsg + '\n');  // Display the server message in the text area
			}
         catch (IOException ex) 
           {
             System.err.println(ex);
           }
         }); 
      	    
        try 
          {
           Socket socket = new Socket("localhost", 8000);   // Create a socket to connect to the server
		   fromServer = new DataInputStream(socket.getInputStream());   // Create an input stream to receive data from the server
		   toServer = new DataOutputStream(socket.getOutputStream());   // Create an output stream to send data to the server
		  }
        catch (IOException ex) 
          {
		    tac.appendText(ex.toString() + '\n');
		  }
     }
}