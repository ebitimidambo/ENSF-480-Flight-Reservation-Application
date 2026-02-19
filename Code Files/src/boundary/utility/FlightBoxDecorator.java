package boundary.utility;

import javax.swing.JPanel;

public abstract class FlightBoxDecorator extends FlightBox{
	protected FlightBox decoratedBox;
	
	public FlightBoxDecorator(FlightBox decoratedBox) {
		this.decoratedBox = decoratedBox;
	}
	
	public JPanel createBox() {
		return decoratedBox.createBox();
	}
}
