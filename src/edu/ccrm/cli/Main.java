package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        // Get the AppConfig instance (it loads config automatically)
        AppConfig config = AppConfig.getInstance();
        
        // Create and run the CLI application
        CLIApplication app = new CLIApplication();
        app.run();
    }
}