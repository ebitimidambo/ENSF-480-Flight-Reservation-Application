package boundary.utility;

import entity.User;

public class FlightAgentGuiFactory {

    private final User user;

    public FlightAgentGuiFactory(User user) {
        this.user = user;
    }

    public Header createHeader() {
        return new FlightAgentHeader(user);
    }

    public MidSection createMidSection() {
        return new FlightAgentMidSection();
    }

    public Body createBody() {
        return new FlightAgentBody(user);
    }
}
