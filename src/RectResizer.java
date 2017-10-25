
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class RectResizer {

	private Rectangle rect;
	private boolean dragging;
	private double prevY;
	private final int MARGIN_BUFFER = 12;
	private final int minSize = 10;
	private DragType dragType;
	private double prevX;
	private boolean moving;
	private double northOffset;
	private double westOffset;

	private RectResizer(Rectangle rect){
		this.rect = rect;
		dragType = DragType.NONE;
	}

	/**
	 * Makes the specified Rectangle resizable and movable in the scene
	 * @param rect
	 */
	public static void makeResizable(Rectangle rect){
		final RectResizer resizer = new RectResizer(rect);

        rect.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }});
        rect.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }});
        rect.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseMoved(event);
            }});
        rect.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }});
	}

	protected void mouseReleased(MouseEvent event) {
		dragging = false;
		dragType = DragType.NONE;
		rect.setCursor(Cursor.DEFAULT);
	}

	protected void mouseMoved(MouseEvent event) {
		DragType currentResizeType = getCurrentResizeType(event);
		if(currentResizeType == DragType.NORTH || currentResizeType == DragType.SOUTH
				|| dragType == DragType.NORTH || dragType == DragType.SOUTH){
			rect.setCursor(Cursor.S_RESIZE);
		}
		else if(currentResizeType == DragType.EAST || currentResizeType == DragType.WEST
				|| dragType == DragType.EAST || dragType == DragType.WEST){
			rect.setCursor(Cursor.E_RESIZE);
		}
		else if(currentResizeType == DragType.NW || currentResizeType == DragType.SE
				|| dragType == DragType.NW || dragType == DragType.SE){
			rect.setCursor(Cursor.NW_RESIZE);
		}
		else if(currentResizeType != DragType.NONE || dragType != DragType.NONE){
			rect.setCursor(Cursor.NE_RESIZE);
		}
		else {
			rect.setCursor(Cursor.DEFAULT);
		}
	}

	protected void mouseDragged(MouseEvent event) {
		if(dragging){
			if(dragType == DragType.NORTH || dragType == DragType.NE || dragType == DragType.NW){
				double newHeight = rect.getHeight() - event.getSceneY() + this.prevY;
				if(newHeight >= minSize){
					rect.setLayoutY(event.getSceneY() - northOffset);
					rect.setHeight(newHeight);
				}
				else {
					double oldBottom = rect.getLayoutY()+ rect.getHeight();
					rect.setHeight(minSize);
					rect.setLayoutY(oldBottom - minSize);
				}
				this.prevY = rect.getLayoutY() + northOffset;
			}
			else if(dragType == DragType.SOUTH || dragType == DragType.SE || dragType == DragType.SW){
				double newHeight = rect.getHeight() + event.getSceneY() - this.prevY;
				this.prevY = event.getSceneY();
				if(newHeight < minSize){
					newHeight = minSize;
					this.prevY = rect.getLayoutY() + newHeight;
				}
				rect.setHeight(newHeight);

			}
			if(dragType == DragType.EAST || dragType == DragType.SE || dragType == DragType.NE) {
				//rect.setWidth(rect.getWidth() + event.getSceneX() - this.prevX);
				double newWidth = rect.getWidth() + event.getSceneX() - this.prevX;
				this.prevX = event.getSceneX();
				if(newWidth < minSize){
					newWidth = minSize;
					this.prevX = rect.getLayoutX() + newWidth;
				}
				rect.setWidth(newWidth);
			}
			else if(dragType == DragType.WEST || dragType == DragType.SW || dragType == DragType.NW){
				double newWidth = rect.getWidth() - event.getSceneX() + this.prevX;
				if(newWidth >= minSize){
					rect.setLayoutX(event.getSceneX() - westOffset);
					rect.setWidth(newWidth);
				}
				else {
					double oldRightSide = rect.getLayoutX()+ rect.getWidth();
					rect.setWidth(minSize);
					rect.setLayoutX(oldRightSide - minSize);
				}
				this.prevX = rect.getLayoutX() + westOffset;
			}
			//this.prevY = event.getSceneY();
			//this.prevX = event.getSceneX();
		}
		else if(moving){
			double xOffset = event.getSceneX() - prevX;
			double yOffset = event.getSceneY() - prevY;
			rect.setLayoutX(rect.getLayoutX() + xOffset);
			rect.setLayoutY(rect.getLayoutY() + yOffset);
			this.prevY = event.getSceneY();
			this.prevX = event.getSceneX();
		}
	}

	protected void mousePressed(MouseEvent event) {
		this.dragType = getCurrentResizeType(event);
		if(dragType != DragType.NONE){
			this.dragging = true;
		}
		else {
			this.moving = true;
		}
		this.prevY = event.getSceneY();
		this.prevX = event.getSceneX();

	}

	private DragType getCurrentResizeType(MouseEvent event) {
		DragType result = DragType.NONE;
		if(event.getY() > rect.getHeight() - MARGIN_BUFFER){
			result = DragType.SOUTH;
		}
		else if(event.getY() < MARGIN_BUFFER){
			result = DragType.NORTH;
			this.northOffset = event.getY();
		}
		if(event.getX() > rect.getWidth() - MARGIN_BUFFER){
			if(result == DragType.SOUTH){
				result = DragType.SE;
			}
			else if(result == DragType.NORTH){
				result = DragType.NE;
			}
			else {
				result = DragType.EAST;
			}
		}
		else if(event.getX() < MARGIN_BUFFER){
			this.westOffset = event.getX();
			if(result == DragType.SOUTH){
				result = DragType.SW;
			}
			else if(result == DragType.NORTH){
				result = DragType.NW;
			}
			else {
				result = DragType.WEST;
			}
		}
		return result;
	}

	private static enum DragType {
		NONE,
		NORTH,
		SOUTH,
		EAST,
		WEST,
		NW,
		SW,
		NE,
		SE;
	}
}
