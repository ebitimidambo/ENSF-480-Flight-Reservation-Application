package boundary;


public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = MainFrame.getInstance();

        mainFrame.showPanel(new LoginUI());

        mainFrame.showFrame();
    }
}
