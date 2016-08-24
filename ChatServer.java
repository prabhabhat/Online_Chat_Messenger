import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

 public class ChatServer extends Application
  {
	String clientmsg=null,servermsg=null;     // the client and server messages
		 
	 public static void main(String[] args) 
	 {
		Application.launch(args);
	 }
	 
	 @Override 
     public void start(Stage primaryStage)   // Override the start method in the Application class 
	  {
       BorderPane paneForTextField2 = new BorderPane();   //Panel to hold the label and text field
	   paneForTextField2.setPadding(new Insets(5, 5, 5, 5));
	   paneForTextField2.setStyle("-fx-border-color: red");
	   paneForTextField2.setLeft(new Label("Enter a message: "));
		 
       TextField tfs = new TextField();
	   tfs.setAlignment(Pos.BOTTOM_RIGHT);
	   paneForTextField2.setCenter(tfs);
		
	   BorderPane mainPane = new BorderPane();
	   TextArea tas = new TextArea();     // Text area for displaying contents
	   mainPane.setCenter(new ScrollPane(tas));
	   mainPane.setTop(paneForTextField2);
		
	   Scene scene = new Scene(mainPane, 450, 250);    // Create a scene and place it in the stage
	   primaryStage.setTitle("Chat Server");         // Set the stage title
	   primaryStage.setScene(scene); // Place the scene in the stage
	   primaryStage.show(); // Display the stage

	   new Thread(() -> 
       {
        try 
          {
           ServerSocket serverSocket = new ServerSocket(8000);   // Create a server socket
    	   Platform.runLater(() ->
		   tas.appendText("Server started at " + new Date() + '\n'));
			
		   Socket socket = serverSocket.accept();   // Listen for a connection request
			
           DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());    // Create data input streams
           DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());  // Create data output streams
           while (true) 
             {
        	 //  clientmsg = String.valueOf(inputFromClient.readChar());  // Receive client message from the client
        	   clientmsg = inputFromClient.readUTF();
        	   Platform.runLater(() -> 
			   {
                tas.appendText("Client message : " + clientmsg + "\n");
               });
 
          //   fromClient = new DataInputStream(socket.getInputStream());   //Create an input stream to receive data from the client
        	   tfs.setOnAction(e -> 
               {
           	 try 
           	   {   	 
           	      servermsg = tfs.getText().trim();    // Enter Server message
           	     outputToClient.writeUTF(servermsg);   // Send the message to the server
           	    outputToClient.flush();
           	    
           	      // Send server message back to the client
           	     tfs.setText("");
           	      Platform.runLater(() -> 
           	       {
           	          tas.appendText("Server message : " + servermsg + '\n');
           	       });
           	  }
           	 catch(IOException ex)
           	   {
           		   System.err.println(ex); 
           	   }
              }); 
        	   
            }
         }
      catch(IOException ex) 
        {
          ex.printStackTrace();
        }
       }).start();  
  }
 }