import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import java.util.Optional;
import javafx.animation.AnimationTimer;
import javafx.scene.control.ColorPicker;

public class Paint extends Application
{
	int plyrX = 0;
	int plyrY = 0;
	boolean spacePressed = false;
	int brushSize = 25;
	Color brushColor = new Color(0, 0, 0, 0);
	final long startNanoTime = System.nanoTime();
	
	public static void main(String[] args) 
	{
		launch(args);
	}

	public void start(Stage theStage)
	{
		theStage.setTitle("Paint");
		
		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);
		
		final Canvas canvas = new Canvas(1075, 725);
		final Canvas brush = new Canvas(1075, 725);
		
		final ColorPicker colorPicker = new ColorPicker();
		
		root.getChildren().add(canvas);
		root.getChildren().add(brush);
		root.getChildren().add(colorPicker);
		colorPicker.setVisible(false);
		
		final GraphicsContext gcCanvas = canvas.getGraphicsContext2D();
		final GraphicsContext gcBrush = brush.getGraphicsContext2D();

		new AnimationTimer()
		{
			public void handle(long currentNanoTime)
			{	
				
				double t = (currentNanoTime - startNanoTime) / 100000000.0;
				
			if(spacePressed == true)
				{
					gcBrush.setFill(Color.BLACK);
				
					gcCanvas.fillRect(plyrX, plyrY, brushSize, brushSize);
					gcCanvas.setFill(brushColor);
				}	
				else
				{
					gcBrush.clearRect(brush.getWidth() - brush.getWidth(), brush.getHeight() - brush.getHeight(), brush.getWidth(), brush.getHeight());
					gcBrush.fillRect(plyrX, plyrY, brushSize, brushSize);
					gcBrush.setFill(Color.BLACK);
					if((int)t % 2 == 0)
					{
						gcBrush.setFill(Color.WHITE);
					}
					
				}                                                      
			}
		}.start();
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent keyEvent)
			{
				if(colorPicker.isVisible() == false)
				{
					switch(keyEvent.getCode())
					{
					case W:
						if(plyrY == (int)canvas.getHeight() - (int)canvas.getHeight())
							plyrY = (int)(canvas.getHeight() - (int)canvas.getHeight());
						else
							plyrY -= brushSize;
						System.out.println("W (" + plyrX + ", " + plyrY + ")");
						break;
					case A:
						if(plyrX == (int)canvas.getWidth() - (int)canvas.getWidth())
							plyrX = (int)canvas.getWidth() - (int)canvas.getWidth();
						else
							plyrX -= brushSize;
						System.out.println("A (" + plyrX + ", " + plyrY + ")");
						break;
					case S:	
						if(plyrY == (int)canvas.getHeight() - brushSize)
							plyrY = (int)canvas.getHeight() - brushSize;
						else
							plyrY += brushSize;
						System.out.println("S (" + plyrX + ", " + plyrY + ")");
						break;
					case D:
						if(plyrX == (int)canvas.getWidth() - brushSize)
							plyrX = (int)canvas.getWidth() - brushSize;
						else
							plyrX += brushSize;
						System.out.println("D (" + plyrX + ", " + plyrY + ")");
						break;
					case SPACE:	
						spacePressed = true;
						System.out.println("SPACE");
						break;
					case C:
						colorPicker.setVisible(true);
						break;
					case CONTROL:
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Paint Helper");
						alert.setContentText("Are you sure you want to clear the canvas?");
						Optional<ButtonType> canvasResult = alert.showAndWait();
						if(canvasResult.get() == ButtonType.OK)
						{
							gcCanvas.clearRect(0, 0, gcCanvas.getCanvas().getWidth(), gcCanvas.getCanvas().getHeight());
						}
						System.out.println("Choice: " + canvasResult.get());
						break;
					default:
						break;
					}
				}
			}
		});
		
		theScene.setOnKeyReleased(new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent keyEvent)
			{
				switch(keyEvent.getCode())
				{
				case SPACE:
					spacePressed = false;
					break;
				}
			}
		});
		
		colorPicker.setOnAction(new EventHandler()
		{
			public void handle(Event t)
			{
				brushColor = colorPicker.getValue();
				System.out.println("Color: " + brushColor);
				colorPicker.setVisible(false);
			}
		});
		theStage.show();
	}
}