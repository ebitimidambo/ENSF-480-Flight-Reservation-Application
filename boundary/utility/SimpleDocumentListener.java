package boundary.utility;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface SimpleDocumentListener extends DocumentListener {
	void update();
	
	default void insertUpdate(DocumentEvent e) {
		update();
	}

	default void removeUpdate(DocumentEvent e) {
		update();
	}

	default void changedUpdate(DocumentEvent e) {
		update();
	}

}
