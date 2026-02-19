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

import control.FlightDAO;
import control.RouteDAO;
import entity.Route;

@SuppressWarnings("serial")
public class ManageRoutesPage extends JPanel {

    public ManageRoutesPage() {
        setLayout(new BorderLayout());
        setBackground(new java.awt.Color(45, 59, 73));
        buildContent();
    }

    @SuppressWarnings("unused")
	private void buildContent() {

        String[] columns = {
            "Route ID",
            "Origin",
            "Destination",
            "Distance (km)",
            "Status"
        };

        RouteDAO routeDAO = new RouteDAO();
        List<Route> routes = routeDAO.getAllRoutesForAdmin();

        Object[][] data = new Object[routes.size()][columns.length];

        for (int i = 0; i < routes.size(); i++) {
            Route r = routes.get(i);
            data[i][0] = r.getRouteId();
            data[i][1] = r.getOrigin();
            data[i][2] = r.getDestination();
            data[i][3] = r.getDistance();
            data[i][4] = r.getStatus();
        }

        PanelTable table = new PanelTable(columns, data);

        table.setRowActionListener(new RowActionListener() {

            @Override
            public void onEdit(int id) {
            	RouteDAO dao = new RouteDAO();
                Route route = dao.getRouteById(id);

                EditRouteDialog dialog = new EditRouteDialog(
                    SwingUtilities.getWindowAncestor(ManageRoutesPage.this),
                    route
                );

                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    refresh();
                }
            }

            @Override
            public void onDelete(int id) {
                int confirm = JOptionPane.showConfirmDialog(
                    ManageRoutesPage.this,
                    "Deleting this route will set all related flights' status to 'Canceled'.\nContinue?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    FlightDAO flightDAO = new FlightDAO();
                    flightDAO.cancelFlightsByRoute(id);

                    routeDAO.deleteRoute(id);
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

        // ----- Add button below table, right-aligned -----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = createAddButton("Add Route");
        addButton.addActionListener(e -> handleAddRoute());
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
        btn.setBackground(new java.awt.Color(120, 200, 120));
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

    private void handleAddRoute() {
        String origin = JOptionPane.showInputDialog(
            this,
            "Enter origin:",
            "Add Route",
            JOptionPane.QUESTION_MESSAGE
        );
        if (origin == null || origin.isBlank()) return;

        String destination = JOptionPane.showInputDialog(
            this,
            "Enter destination:",
            "Add Route",
            JOptionPane.QUESTION_MESSAGE
        );
        if (destination == null || destination.isBlank()) return;

        String distanceStr = JOptionPane.showInputDialog(
            this,
            "Enter distance (km):",
            "Add Route",
            JOptionPane.QUESTION_MESSAGE
        );
        if (distanceStr == null || distanceStr.isBlank()) return;

        try {
            double distance = Double.parseDouble(distanceStr);

            RouteDAO dao = new RouteDAO();
            dao.addRoute(origin, destination, distance);
            refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Invalid distance value.",
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
