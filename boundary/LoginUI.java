package boundary;

import control.LoginController;
import control.RegistrationController;
import entity.User;

import javax.swing.*;

import boundary.utility.BackgroundPanel;
import boundary.utility.DateValidator;
import boundary.utility.EmailValidator;
import boundary.utility.RoundedButton;
import boundary.utility.RoundedJTextField;
import boundary.utility.RoundedPasswordField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class LoginUI extends JPanel {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel messageLabel;
	private JButton loginButton;
	private LoginController controller;
	private static final int Time = 500;
	private static final String FONT = "Segoe UI Light";

	public LoginUI() {
		controller = new LoginController();
		setupUI();
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		BackgroundPanel backgroundPanel = new BackgroundPanel("SkyBackground1.png");
		this.add(backgroundPanel, BorderLayout.CENTER);

		backgroundPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(20, 20, 20, 20); // Add some padding around the form

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);

		ImageIcon logoIcon = new ImageIcon("images/AEAELogo.png");
		Image logoImage = logoIcon.getImage();
		Image scaledLogoImage = logoImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Width: 100px, Height:
																							// 100px
		ImageIcon scaledLogoIcon = new ImageIcon(scaledLogoImage);
		JLabel logoLabel = new JLabel(scaledLogoIcon);
		logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// logoLabel.setPreferredSize(new Dimension(100, 100));

		usernameField = new RoundedJTextField(20, "Username");
		usernameField.setPreferredSize(new Dimension(250, 40));
		usernameField.setBackground(new Color(45, 45, 45));
		usernameField.setForeground(Color.WHITE);
		usernameField.setCaretColor(Color.WHITE);
		usernameField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
		usernameField.setFont(new Font(FONT, Font.PLAIN, 14));

		passwordField = new RoundedPasswordField(20, "Password");
		passwordField.setPreferredSize(new Dimension(250, 40));
		passwordField.setBackground(new Color(45, 45, 45));
		passwordField.setForeground(Color.WHITE);
		passwordField.setCaretColor(Color.WHITE);
		passwordField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
		passwordField.setFont(new Font(FONT, Font.PLAIN, 14));

		loginButton = new RoundedButton("Login");
		loginButton.setPreferredSize(new Dimension(250, 40));
		loginButton.setBackground(new Color(45, 59, 73));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font(FONT, Font.BOLD, 16));
		loginButton.setFocusPainted(false);
		loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton registerButton = new JButton("Don't have an account? Register");
		registerButton.setPreferredSize(new Dimension(250, 40));
		registerButton.setOpaque(false);
		registerButton.setForeground(new Color(45, 59, 73));
		registerButton.setFont(new Font(FONT, Font.BOLD, 14));
		registerButton.setFocusPainted(false);
		registerButton.setBorderPainted(false);
		registerButton.setContentAreaFilled(false);
		registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		loginButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				loginButton.setBackground(new Color(59, 75, 93));
			}

			public void mouseExited(MouseEvent e) {
				loginButton.setBackground(new Color(45, 59, 73));
			}
		});

		registerButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				registerButton.setForeground(new Color(72, 93, 111));
			}

			public void mouseExited(MouseEvent e) {
				registerButton.setForeground(new Color(45, 59, 73));
			}
		});

		formPanel.add(Box.createVerticalStrut(50));
		formPanel.add(logoLabel);
		formPanel.add(Box.createVerticalStrut(30));
		formPanel.add(usernameField);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(passwordField);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(loginButton);
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(registerButton);
		formPanel.add(Box.createVerticalStrut(30));

		backgroundPanel.add(formPanel, gbc);

		messageLabel = new JLabel("");
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(messageLabel);

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				User user = controller.login(username, password);

				Timer resetNotification = new Timer(1500, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						messageLabel.setText("");
					}
				});

				if (user != null) {
					System.out.println(user.toString());
					messageLabel.setText("Login Successful....");
					messageLabel.setForeground(new Color(184, 134, 11));

					Timer loadHomeUI = new Timer(Time, new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							MainFrame mainFrame = MainFrame.getInstance();
							if (user.getRoleName().contentEquals("CUSTOMER")) {
								mainFrame.showPanel(new CustomerUI(user));
							} else if (user.getRoleName().contentEquals("SYSTEM_ADMIN")) {
								mainFrame.showPanel(new AdminUI(user));
							} else if (user.getRoleName().contentEquals("FLIGHT_AGENT")) {
								mainFrame.showPanel(new FlightAgentUI(user));
							}
						}
					});

					loadHomeUI.setRepeats(false);
					loadHomeUI.start();
				} else {
					System.out.println("User does not exist");
					messageLabel.setText("Login Failed. Invalid Credentials");
					messageLabel.setForeground(new Color(92, 1, 32));

					resetNotification.setRepeats(false);
					resetNotification.start();
				}
			}
		});

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showRegistrationDialog(); // Show registration dialog when clicked
			}
		});
	}

	private void showRegistrationDialog() {
		JPanel registrationPanel = new JPanel(new GridLayout(7, 2, 10, 10));

		JTextField firstNameField = new JTextField(20);
		JTextField lastNameField = new JTextField(20);
		JTextField dateOfBirth = new JTextField(20);
		JTextField emailField = new JTextField(20);
		JTextField addressField = new JTextField(20);
		JTextField usernameField = new JTextField(20);
		JPasswordField passwordField = new JPasswordField(20);

		registrationPanel.add(new JLabel("First Name:"));
		registrationPanel.add(firstNameField);
		registrationPanel.add(new JLabel("Last Name:"));
		registrationPanel.add(lastNameField);
		registrationPanel.add(new JLabel("Date Of Birth:"));
		registrationPanel.add(dateOfBirth);
		registrationPanel.add(new JLabel("Email:"));
		registrationPanel.add(emailField);
		registrationPanel.add(new JLabel("Address:"));
		registrationPanel.add(addressField);
		registrationPanel.add(new JLabel("Username:"));
		registrationPanel.add(usernameField);
		registrationPanel.add(new JLabel("Password:"));
		registrationPanel.add(passwordField);

		int option = JOptionPane.showConfirmDialog(this, registrationPanel, "Register New User",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String dob = dateOfBirth.getText().replaceAll("^\\s+|\\s+$", "");
			String email = emailField.getText().replaceAll("^\\s+|\\s+$", "");
			String address = addressField.getText();
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());

			if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || username.isEmpty()
					|| password.isEmpty() || dob.isEmpty()) {
				JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validate email
			if (!EmailValidator.validateEmail(email)) {
				JOptionPane.showMessageDialog(this, "Invalid email addrress", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!DateValidator.validateFormDate(dob)) {
				JOptionPane.showMessageDialog(this, "Invalid Date Format", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Create new user and register
			User user = new User(0, username, password, firstName, lastName, email, "CUSTOMER");
			user.setDateOfBirth(dob);
			user.setAddress(address);

			RegistrationController registrar = new RegistrationController();
			int success = registrar.registerUser(user);

			switch (success) {
			case 1:
				JOptionPane.showMessageDialog(this,
						"🎉 Registration successful!\nYou can now log in using your new account.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			case 0:
				JOptionPane.showMessageDialog(this,
						"⚠️ This username or email is already registered.\nPlease try a different one.",
						"User Already Exists", JOptionPane.WARNING_MESSAGE);
				break;
			case -1:
			default:
				JOptionPane.showMessageDialog(this,
						"❌ Something went wrong during registration.\nPlease try again later.", "Database Error",
						JOptionPane.ERROR_MESSAGE);
				break;
			}

			if (success == 1) {
				usernameField.setText(username);
				passwordField.setText(password);
			}

		}
	}
}
