package boundary;
import entity.User;
import boundary.utility.AlternateRowRenderer;
import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.FlightAgentGuiFactory;
import boundary.utility.Header;
import control.FlightAgentController;
import entity.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuppressWarnings("serial")
public class AgentFlightScheduleUI extends JPanel {

    private static final String FONT = "Segoe UI Light";

    private final User agent;
    private final FlightAgentController controller;
    private final FlightAgentGuiFactory factory;
    private final Header header;
    private JTable table;

    public AgentFlightScheduleUI(User agent) {
        this.agent = agent;
        this.controller = new FlightAgentController();
        this.factory = new FlightAgentGuiFactory(agent);
        this.header = factory.createHeader();
        setupUI();
        loadFlights();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(CustomerUIBanner.createBanner(agent), BorderLayout.NORTH);
        topPanel.add(header.createHeader(), BorderLayout.SOUTH);
        topPanel.setBackground(new Color(45, 59, 73));
        add(topPanel, BorderLayout.NORTH);

        add(createSchedulePanel(), BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(CustomerUIFooter.createFooter(), BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(45, 59, 73));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel title = new JLabel("Flight Schedule", SwingConstants.LEFT);
        title.setFont(new Font(FONT, Font.BOLD, 20));
        title.setForeground(new Color(45, 59, 73));

        String[] cols = {
                "Flight", "Aircraft", "Origin", "Destination",
                "Departure Time", "Arrival Time", "Status"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new AlternateRowRenderer());
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadFlights() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Flight> flights = controller.getAllFlights();
        for (Flight f : flights) {
            String origin = (f.getRoute() != null) ? f.getRoute().getOrigin() : "";
            String dest = (f.getRoute() != null) ? f.getRoute().getDestination() : "";

            model.addRow(new Object[]{
                    f.getFlightName(),
                    f.getModel().getAircraftModel(),
                    origin,
                    dest,
                    (f.getDepartureTime() != null) ? f.getDepartureTime().format(fmt) : "",
                    (f.getArrivalTime() != null) ? f.getArrivalTime().format(fmt) : "",
                    f.getStatus()
            });
        }
    }
}
