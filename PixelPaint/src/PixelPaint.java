import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PixelPaint extends Application{

	@Override
	public void start(Stage primaryStage){
		BorderPane mainPane = new BorderPane();
		GridPane canvasPane = new GridPane();
		VBox vBox = new VBox();
		Button saveBTN = new Button("Save");
		Button readFromFilebtn = new Button("Read File");
		Button eraseBTN = new Button("Erase");
		final ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(Color.BLUE);
		canvasPane.setMinSize(32,32);

		Rectangle[][] pixel = new Rectangle[32][32];


		for(int i = 0; i < 32; i++){
			int indexi = i;
			for(int j = 0; j < 32; j++){
				int indexj = j;
				pixel[i][j] = new Rectangle();
				pixel[i][j].setWidth(15);
				pixel[i][j].setHeight(15);
				pixel[i][j].setStroke(Color.BLACK);
				pixel[i][j].setFill(Color.WHITE);

				pixel[i][j].setOnMouseClicked(e->{
					//System.out.println("Clicked rectangle (" + i + "," + j + ")");

					pixel[indexi][indexj].setFill(colorPicker.getValue());
					//pixel[i][j].setStroke(colorPicker.getValue());

				});
			}
		}

		for(int i = 0; i < 32; i++){
			for(int j = 0; j < 32; j++){
				canvasPane.add(pixel[i][j],i,j);
			}
		}


		eraseBTN.setOnAction(e->{
			for(int i = 0; i < 32; i++){
					int indexi = i;
				for(int j = 0; j < 32; j++){
					int indexj = j;
					pixel[indexi][indexj].setFill(Color.WHITE);
				}
			}
		});
		
		saveBTN.setOnAction(e->{
			
			try{
				BufferedWriter writer = null;
				File pictureFile = new File("picturefile.txt");
				writer = new BufferedWriter(new FileWriter(pictureFile));
				
				for(int i = 0; i < 32; i++){
					int indexi = i;
					for(int j = 0; j < 32; j++){
						int indexj = j;
						Color c = (Color) pixel[indexi][indexj].getFill();
						String hex = String.format("r%d g%d b%d",
								(int)(c.getRed() * 255),
								(int)(c.getGreen() * 255),
								(int)(c.getBlue() * 255)
								);
						
						writer.write(hex);
						writer.newLine();
					}
				}
				System.out.println("Imaged Saved to picturefile.txt (If File exists it will be overwritten!)");
				writer.close();
				
			}catch(Exception ee){
				System.out.println("An error has occured!");
			}
			
		});
		
		
		readFromFilebtn.setOnAction(e->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose a TXT file");
			File newFile = fileChooser.showOpenDialog(primaryStage);
			
			try{
				BufferedReader reader = new BufferedReader(new FileReader(newFile));
				
				for(int i = 0; i < 32; i++){
					int indexi = i;
					for(int j = 0; j < 32; j++){
						int indexj = j;
						String currentLine = reader.readLine();
						int redStart = currentLine.indexOf("r");
						int greenStart = currentLine.indexOf("g");
						int blueStart = currentLine.indexOf("b");
						
						String redString = "";
						for(int k = (redStart+1); k < (greenStart-1); k++){
							redString += currentLine.charAt(k);
						}
						double newRed = Double.parseDouble(redString);
						
						String greenString = "";
						for(int k = (greenStart+1); k < (blueStart-1); k++){
							greenString += currentLine.charAt(k);
						}
						double newGreen = Double.parseDouble(greenString);
						
						String blueString = "";
						for(int k = (blueStart+1); k < currentLine.length(); k++){
							blueString += currentLine.charAt(k);
						}
						double newBlue = Double.parseDouble(blueString);
						
						pixel[indexi][indexj].setFill(Color.color(newRed/255, newGreen/255, newBlue/255));
						
					}
				}
				reader.close();
			}catch(Exception ee){
				System.out.println("An error has occured!");
			}
			
		});
		
		vBox.setSpacing(10);
		vBox.getChildren().add(saveBTN);
		vBox.getChildren().add(readFromFilebtn);
		vBox.getChildren().add(eraseBTN);
		vBox.getChildren().add(colorPicker);
		mainPane.setCenter(canvasPane);
		mainPane.setLeft(vBox);
		Scene scene = new Scene(mainPane, 700,600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Pixel Paint");
		primaryStage.show();
	}

	public void changeColor(Rectangle rect,ColorPicker colorPicker){
		rect.setFill(colorPicker.getValue());
		rect.setStroke(colorPicker.getValue());
	}
	
	public static void main(String[] args){
		PixelPaint.launch();
	}
}