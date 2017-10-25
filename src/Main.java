/**
 * Playground JavaFX Application that creates some rectangles and makes them resizable
 *
 * Author: Jonah Weisse
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Rectangle Playground");

        Rectangle rectangle = new Rectangle(200, 200);
        rectangle.setLayoutX(100);
        rectangle.setLayoutY(100);
        rectangle.setFill(Color.RED);
       	RectResizer.makeResizable(rectangle);
       	makeRectColorChangable(rectangle);

       	Rectangle rectangle2 = new Rectangle(100, 200);
        rectangle2.setLayoutX(350);
        rectangle2.setLayoutY(400);
        rectangle2.setFill(Color.BLUE);
       	RectResizer.makeResizable(rectangle2);
       	makeRectColorChangable(rectangle2);

        Pane layout = new Pane();
        Scene scene = new Scene(layout,700,700);

        layout.setOnMouseClicked(e -> {
        	if(e.getButton() == MouseButton.SECONDARY){
        		Rectangle newRect = new Rectangle(100, 100);
                newRect.setLayoutX(e.getX());
                newRect.setLayoutY(e.getY());
                newRect.setFill(getRandomColor());
               	RectResizer.makeResizable(newRect);
               	makeRectColorChangable(newRect);
               	layout.getChildren().add(newRect);
        	}
        });

        layout.getChildren().addAll(rectangle, rectangle2);
        primaryStage.setScene(scene);
        primaryStage.show();

	}

	private Paint getRandomColor() {
		Color col = new Color(Math.random(), Math.random(), Math.random(), 1);
		return col;
	}

	/**
	 * Allows rectangle color change
	 * @param rect
	 */
	private void makeRectColorChangable(Rectangle rect){
       	rect.setOnMouseClicked(m ->{
       		if(m.getButton().equals(MouseButton.PRIMARY)){
       			if(m.getClickCount() == 2){
       				rect.setFill(getRandomColor());
       			}
       		}
       	});
	}

}
