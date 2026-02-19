package boundary.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import entity.Seat;

@SuppressWarnings({ "serial", "unused" })
public class SeatMapPanel extends JPanel {
    private static final String FONT = "Segoe UI Light";
    private ArrayList<Seat> seats;
    private Seat selectedSeat = null;
    private Set<Integer> globallySelectedSeatIds;
    private JLabel seatStatusLabel;
    private Runnable onClose; // callback to run when selection is confirmed
    private Seat preselectedSeat;

    public SeatMapPanel(ArrayList<Seat> seats, JLabel seatStatusLabel, Runnable onClose, 
    		Set<Integer> globallySelectedSeatIds, Seat preselectedSeat) {
        this.seats = seats;
        this.seatStatusLabel = seatStatusLabel;
        this.onClose = onClose;
        this.globallySelectedSeatIds = globallySelectedSeatIds;
        this.preselectedSeat = preselectedSeat;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildSeatGrid(preselectedSeat);
    }

    private void buildSeatGrid(Seat preselectedSeat) {
        JPanel gridPanel = new JPanel(new GridLayout(0, 6, 10, 10)); // 6 seats per row
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(Color.WHITE);

        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatNumber());
            seatButton.setFont(new Font(FONT, Font.BOLD, 12));
            seatButton.setFocusPainted(false);
            seatButton.setMaximumSize(new Dimension(60, 40));

            // color based on seat class
            switch (seat.getSeatClass().toLowerCase()) {
                case "business":
                    seatButton.setBackground(new Color(173, 216, 230));
                    break;
                case "premium economy":
                    seatButton.setBackground(new Color(144, 238, 144));
                    break;
                default:
                    seatButton.setBackground(new Color(240, 240, 240));
                    break;
            }

            // gray out if unavailable
            if (!seat.getAvailability() || globallySelectedSeatIds.contains(seat.getSeatId())) {
            	System.out.println("Seat: " + seat.getSeatNumber() + " is " + seat.getAvailability());
                seatButton.setEnabled(false);
                seatButton.setBackground(new Color(200, 200, 200));
            }
            
            // ✅ highlight if this is the preselected seat
            if (preselectedSeat != null && seat.getSeatId() == preselectedSeat.getSeatId()) {
                seatButton.setBackground(new Color(255, 223, 186)); // orange highlight
                selectedSeat = seat;
            }

            seatButton.addActionListener(e -> handleSeatSelection(seat, seatButton, gridPanel));
            gridPanel.add(seatButton);
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JButton confirmBtn = new RoundedButton("Confirm Seat");
        confirmBtn.setPreferredSize(new Dimension(150, 35));
        confirmBtn.addActionListener(e -> confirmSelection());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(confirmBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleSeatSelection(Seat seat, JButton button, JPanel gridPanel) {
        if (selectedSeat != null && !selectedSeat.equals(seat)) {
            for (Component c : gridPanel.getComponents()) {
                if (c instanceof JButton) {
                    JButton b = (JButton) c;
                    if (b.getText().equals(selectedSeat.getSeatNumber())) {
                        // reset color
                        resetSeatColor(b, selectedSeat);
                        break;
                    }
                }
            }
        }
        selectedSeat = seat;
        button.setBackground(new Color(255, 223, 186)); // highlight selection
    }

    private void resetSeatColor(JButton button, Seat seat) {
        switch (seat.getSeatClass().toLowerCase()) {
            case "business":
                button.setBackground(new Color(173, 216, 230));
                break;
            case "premium":
                button.setBackground(new Color(144, 238, 144));
                break;
            default:
                button.setBackground(new Color(240, 240, 240));
                break;
        }
    }

    private void confirmSelection() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.");
            return;
        }
        
        if (seatStatusLabel.getText().startsWith("Seat: ") && !seatStatusLabel.getText().equals("Seat: None")) {
            String oldSeatNumber = seatStatusLabel.getText().substring(6);
            seats.stream()
                 .filter(s -> s.getSeatNumber().equals(oldSeatNumber))
                 .findFirst()
                 .ifPresent(oldSeat -> globallySelectedSeatIds.remove(oldSeat.getSeatId()));
        }
        
        globallySelectedSeatIds.add(selectedSeat.getSeatId());
        
        seatStatusLabel.setText("Seat: " + selectedSeat.getSeatNumber());
        if (onClose != null) onClose.run();
    }
    
    public Seat getSelectedSeat() {
        return selectedSeat;
    }
}