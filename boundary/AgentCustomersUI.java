package boundary;

import boundary.utility.AlternateRowRenderer;

import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.DateTimePickerDialog;
import boundary.utility.FlightAgentGuiFactory;
import boundary.utility.Header;
import control.FlightAgentController;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("serial")
public class AgentCustomersUI extends JPanel {

    private static final String FONT = "Segoe UI Light";

    private final User agent;
    private final FlightAgentController controller;
    private final FlightAgentGuiFactory factory;
    private final Header header;
    private JTable table;
    private List<User> originalCustomers;

    public AgentCustomersUI(User agent) {
        this.agent = agent;
        this.controller = new FlightAgentController();
        this.factory = new FlightAgentGuiFactory(agent);
        this.header = factory.createHeader();
        setupUI();
        loadCustomers();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(CustomerUIBanner.createBanner(agent), BorderLayout.NORTH);
        topPanel.add(header.createHeader(), BorderLayout.SOUTH);
        topPanel.setBackground(new Color(45, 59, 73));
        add(topPanel, BorderLayout.NORTH);

        add(createCustomersPanel(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(CustomerUIFooter.createFooter(), BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(45, 59, 73));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel title = new JLabel("Manage Customer Data", SwingConstants.LEFT);
        title.setFont(new Font(FONT, Font.BOLD, 20));
        title.setForeground(new Color(45, 59, 73));

        String[] cols = {
        	    "ID",
        	    "First Name",
        	    "Last Name",
        	    "Username",
        	    "Password",
        	    "Email",
        	    "Address"
        	};
        
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
            	return col != 0;
            }
        };

        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new AlternateRowRenderer());
        JScrollPane scrollPane = new JScrollPane(table);

        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font(FONT, Font.BOLD, 13));
        saveButton.setBackground(new Color(45, 59, 73));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(this::handleSaveChanges);
        
        JButton addButton = new JButton("Add Customer");
        addButton.setFont(new Font(FONT, Font.BOLD, 13));
        addButton.setBackground(new Color(45, 59, 73));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this::handleAddCustomer);
        
        JButton deleteButton = new JButton("Delete Customer");
        deleteButton.setFont(new Font(FONT, Font.BOLD, 13));
        deleteButton.setBackground(new Color(45, 59, 73));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(this::handleDeleteCustomer);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(saveButton);
        bottom.add(addButton);
        bottom.add(deleteButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void loadCustomers() {
    	 originalCustomers = controller.getAllCustomers();

    	    DefaultTableModel model = (DefaultTableModel) table.getModel();
    	    model.setRowCount(0);

    	    for (User u : originalCustomers) {
    	        model.addRow(new Object[]{
    	            u.getUserID(),
    	            u.getFirstName(),
    	            u.getLastName(),
    	            u.getUsername(),
    	            u.getPassword(),
    	            u.getEmailAddress(),
    	            u.getAddress()
    	        });
    	 }
    }
    
    
    private void handleAddCustomer(ActionEvent e) {
    	JTextField firstField = new JTextField();
        JTextField lastField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField dobField = new JTextField(10);
        dobField.setEditable(false);
        dobField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        dobField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window parent = SwingUtilities.getWindowAncestor(dobField);

                DateTimePickerDialog picker =
                    new DateTimePickerDialog(parent, "Select Date of Birth", true);

                picker.setVisible(true);

                String selected = picker.getSelectedDateTime();

                if (selected != null) {
                    dobField.setText(selected);
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("First Name:"));
        panel.add(firstField);

        panel.add(new JLabel("Last Name:"));
        panel.add(lastField);

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        
        panel.add(new JLabel("Date of Birth:"));
        panel.add(dobField);
        	

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String first = firstField.getText().trim();
        String last = lastField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobField.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth.");
            return;
        }

        // ==== VALIDATION ====
        if (!isValidName(first)) {
            error("Invalid First Name"); return;
        }

        if (!isValidName(last)) {
            error("Invalid Last Name"); return;
        }

        if (!isValidUsername(username)) {
            error("Invalid Username"); return;
        }

        if (!isValidPassword(password)) {
            error("Password must be at least 6 characters"); return;
        }

        if (!isValidEmail(email)) {
            error("Invalid Email Address"); return;
        }
           
        try {
            controller.addCustomer(first, last, username, password, email, address, dob);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error adding customer: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        loadCustomers();
    }

    private void handleSaveChanges(ActionEvent e) {
    	if (table.isEditing()) {
    	    table.getCellEditor().stopCellEditing();
    	}
    	
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        boolean anyChanged = false;	

        for (int i = 0; i < model.getRowCount(); i++) {
        	
        	User original = originalCustomers.get(i);
            int id = original.getUserID();
        	
        	String first = (String) model.getValueAt(i, 1);
        	String last = (String) model.getValueAt(i, 2);
        	String username = (String) model.getValueAt(i, 3);
        	String password = (String) model.getValueAt(i, 4);
        	String email = (String) model.getValueAt(i, 5);
        	String address = (String) model.getValueAt(i, 6);
        	
        	boolean rowChanged =
        	        !equalsSafe(first, original.getFirstName()) ||
        	        !equalsSafe(last, original.getLastName()) ||
        	        !equalsSafe(username, original.getUsername()) ||
        	        !equalsSafe(password, original.getPassword()) ||
        	        !equalsSafe(email, original.getEmailAddress()) ||
        	        !equalsSafe(address, original.getAddress());

        	if (!rowChanged) continue;

        	anyChanged = true;
        	
        	if (!isValidName(first)) {
                error("Invalid first name at row " + (i + 1)); return;
            }
            if (!isValidName(last)) {
                error("Invalid last name at row " + (i + 1)); return;
            }
            if (!isValidUsername(username)) {
                error("Invalid username at row " + (i + 1)); return;
            }
            if (!isValidPassword(password)) {
                error("Password must be at least 6 characters (row " + (i + 1) + ")");
                return;
            }
            if (!isValidEmail(email)) {
                error("Invalid email address at row " + (i + 1)); return;
            }

            User updated = new User(id, username, password, first, last, email, "CUSTOMER");
            updated.setAddress(address);
            controller.updateCustomerProfile(updated);
        }
        
        if (!anyChanged) {
            JOptionPane.showMessageDialog(
                this,
                "No changes detected.",
                "Nothing to save",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
       }

        JOptionPane.showMessageDialog(
                this,
                "Customer information updated.",
                "Manage Customer Data",
                JOptionPane.INFORMATION_MESSAGE
        );
        loadCustomers();
    }
    
    private void handleDeleteCustomer(ActionEvent e) {

        if (originalCustomers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers available.");
            return;
        }

        String[] options = new String[originalCustomers.size()];
        int[] customerIDs = new int[originalCustomers.size()];

        for (int i = 0; i < originalCustomers.size(); i++) {
            User u = originalCustomers.get(i);
            customerIDs[i] = u.getUserID();
            options[i] = u.getUserID() + " - " + u.getFirstName() + " " + u.getLastName() + " (" + u.getUsername() + ")";
        }

        JComboBox<String> customerBox = new JComboBox<>(options);

        int result = JOptionPane.showConfirmDialog(
                this,
                customerBox,
                "Select Customer to Delete",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        int index = customerBox.getSelectedIndex();

        if (index < 0) {
            JOptionPane.showMessageDialog(this, "No customer selected!");
            return;
        }

        int userID = customerIDs[index];

        // ✅ CONFIRM DELETE
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this customer permanently?\nThis will cancel ALL related reservations.",
                "Are you sure?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        // ✅ DELETE
        controller.deleteUser(userID);

        // ✅ REFRESH TABLE
        loadCustomers();

        JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
    }

    
    private boolean isValidEmail(String email) {
        return email != null &&
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    private boolean isValidUsername(String username) {
        return username != null &&
               username.length() >= 3 &&
               username.matches("[A-Za-z0-9._-]+");
    }
    
    /*
    private boolean isValidDOB(String dob) {
        try {
            LocalDate date = LocalDate.parse(dob);
            return !date.isAfter(LocalDate.now()) &&
                   date.isAfter(LocalDate.now().minusYears(120));
        } catch (Exception e) {
            return false;
        }
    }
    */
    
    private boolean equalsSafe(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }
    
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    private boolean isValidName(String name) {
        return name != null &&
               name.matches("[A-Za-z][A-Za-z\\- ]{1,29}");
    }
    
    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }
}