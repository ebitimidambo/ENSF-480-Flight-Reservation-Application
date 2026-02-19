package boundary.utility;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

import entity.Flight;

public class BookDecorator extends FlightBoxDecorator{
	private final String prompt = "BOOK NOW!";
    private boolean selected = false;
    private Flight flight;
    private boolean isDeparture;
    private FlightSelectionListener listener;
    private RoundedButton button = new RoundedButton(prompt);


	public BookDecorator(FlightBox decoratedBox, Flight flight, boolean isDeparture, FlightSelectionListener listener) {
    	super(decoratedBox);
    	this.flight = flight;
    	this.isDeparture = isDeparture;
    	this.listener = listener;
    	
    	if (listener instanceof FlightSearchBody) {
            ((FlightSearchBody) listener).registerBookDecorator(flight, this);
        }
    }

	public JPanel createBox() {
		JPanel panel = super.createBox();
		if (!(panel.getLayout() instanceof BorderLayout)) {
			panel.setLayout(new BorderLayout());
		}

		button.setDrawBorder(false);
		button.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		button.setPreferredSize(new Dimension(90, 20));
		button.setForeground(new Color(45,59,73));
		button.setBackground(new Color(255, 255, 255));
		JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setOpaque(false); // Keep background transparent if needed

        buttonWrapper.add(button);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	selected = !selected;
                if (selected) {
                    button.setText("Selected");
                    listener.onFlightSelected(flight, isDeparture);
                } else {
                    button.setText(prompt);
                    listener.onFlightDeselected(flight, isDeparture);
                }
            }
        });
        
        panel.add(buttonWrapper, BorderLayout.SOUTH);
		return panel;
	}
	
	public void deselect() {
	    selected = false;
	    button.setText(prompt);
	}
}
