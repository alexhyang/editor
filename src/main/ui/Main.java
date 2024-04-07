package ui;

import model.Event;
import model.EventLog;

public class Main {
    private static EventLog eventLog = EventLog.getInstance();

    // code of this method is based on FitLifeGymKiosk project
    public static void main(String[] args) {
        // Terminal terminal = new Terminal();
        // terminal.start();
        // terminal.endProgram();
        new AppUI();
        for (Event event: eventLog) {
            System.out.println(event);
        }
    }
}
