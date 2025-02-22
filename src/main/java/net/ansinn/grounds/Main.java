package net.ansinn.grounds;

import java.io.IOException;

public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        var CLI = new GroundsCLI();
        CLI.run(args);
    }
}