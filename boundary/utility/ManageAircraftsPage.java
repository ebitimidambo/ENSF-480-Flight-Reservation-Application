package boundary.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import control.AircraftDAO;
import entity.Aircraft;

@SuppressWarnings("serial")
public class ManageAircraftsPage extends JPanel{
	
	public ManageAircraftsPage() {
        setLayout(new BorderLayout());
        setBackground(new java.awt.Color(45, 59, 73));
        buildContent();
    }
	
	@SuppressWarnings("unused")
	private void buildContent() {
	
		String[] columns = {
	            "ID",
	            "Aircraft Model",
	            "Capacity",
	            "Haul Type"
	        };
	
	        AircraftDAO aircraftDAO = new AircraftDAO();
	        List<Aircraft> aircrafts = aircraftDAO.getAllAircrafts();
	
	        Object[][] data = new Object[aircrafts.size()][columns.length];
	
	        for (int i = 0; i < aircrafts.size(); i++) {
	            Aircraft a = aircrafts.get(i);
	
	            data[i][0] = a.getAircraftId();
	            data[i][1] = a.getAircraftModel();
	            data[i][2] = a.getCapacity();
	            data[i][3] = a.getHaulType();
	        }
	
	        PanelTable table = new PanelTable(columns, data);
	
	        table.setRowActionListener(new RowActionListener() {
	            @Override
	            public void onEdit(int id) {
	            	AircraftDAO dao = new AircraftDAO();
	                Aircraft aircraft = dao.getAircraftById(id);
	
	                EditAircraftDialog dialog = new EditAircraftDialog(
	                    SwingUtilities.getWindowAncestor(ManageAircraftsPage.this),
	                    aircraft
	                );
	
	                dialog.setVisible(true);
	
	                if (dialog.isSaved()) {
	                    refresh();
	                }
	            }
	
	            @Override
	            public void onDelete(int id) {
	                int confirm = JOptionPane.showConfirmDialog(
	                    ManageAircraftsPage.this,
	                    "Delete Aircraft " + id + "?",
	                    "Confirm Delete",
	                    JOptionPane.YES_NO_OPTION
	                );
	                if (confirm == JOptionPane.YES_OPTION) {
	                    AircraftDAO dao = new AircraftDAO();
	                    dao.deleteAircraft(id);
	                    refresh();
	                }
	            }
	        });
	
	        JScrollPane scrollPane = new JScrollPane(
	                table,
	                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
	        );
	        scrollPane.setBorder(null);
	        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	        scrollPane.getViewport().setBackground(new Color(45, 59, 73));
	        scrollPane.setBackground(new Color(45, 59, 73));
	        add(scrollPane, BorderLayout.CENTER);
	
	        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JButton addButton = createAddButton("Add Aircraft");
	        addButton.addActionListener(e -> handleAddAircraft());
	        bottomPanel.add(addButton);
	        bottomPanel.setBackground(new Color(45, 59, 73));
	        
	        add(bottomPanel, BorderLayout.SOUTH);
	}

    private JButton createAddButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setMinimumSize(new Dimension(150, 40));
        btn.setMaximumSize(new Dimension(150, 40));
        btn.setBackground(new java.awt.Color(120, 200, 120)); // green
        btn.setForeground(java.awt.Color.BLACK);
        btn.setFont(new java.awt.Font("Segoe UI Light", java.awt.Font.BOLD, 12));
        btn.setBorder(new javax.swing.border.LineBorder(java.awt.Color.DARK_GRAY, 1));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new java.awt.Color(90, 170, 90));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new java.awt.Color(120, 200, 120));
            }
        });
        return btn;
    }

    private void handleAddAircraft() {
        String aircraftModel = JOptionPane.showInputDialog(
            this,
            "Enter aircraft model (e.g., Airbus320):",
            "Add Model",
            JOptionPane.QUESTION_MESSAGE
        );
        if (aircraftModel == null ||aircraftModel.isBlank()) {
            return;
        }
        
        String strCapacity = JOptionPane.showInputDialog(
        	    this,
        	    "Enter capacity:",
        	    "Add Aircraft",
        	    JOptionPane.QUESTION_MESSAGE
        );
        if (strCapacity.trim().isEmpty()) {
        	JOptionPane.showMessageDialog(this, "Value cannot be empty.");
        	return;
        }    
        int capacity;
        try {
        	capacity = Integer.parseInt(strCapacity);
        	if (capacity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Capacity must be a positive number.",
                "Invalid Distance", JOptionPane.ERROR_MESSAGE);
            return;
        }        
        
        String haulTypes[] = {"Short-haul", "Medium-haul", "Long-haul",
        		"Ultra-long-haul", "Regional"};
        
        Object selected = JOptionPane.showInputDialog(
                this,
                "Select route:",
                "Add Flight",
                JOptionPane.QUESTION_MESSAGE,
                null,
                haulTypes,
                haulTypes[0]
            );
        if (selected == null) {
                return;
        }
            
        int chosenIndex = -1;
        for (int i = 0; i < haulTypes.length; i++) {
        	if (haulTypes[i].equals(selected)) {
        		chosenIndex = i;
        		break;
        	}
        }
        if (chosenIndex < 0) return;
        String haulType = haulTypes[chosenIndex];

        try {
            AircraftDAO dao = new AircraftDAO();
            dao.addAircraft(aircraftModel, capacity, haulType);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error adding flight: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refresh() {
        removeAll();
        buildContent();
        revalidate();
        repaint();
    }
}