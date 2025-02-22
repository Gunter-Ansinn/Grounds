package net.ansinn.grounds;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GroundsCLI {

    private final Map<String, Command> commandMap = new HashMap<>();

    public GroundsCLI() {
        registerCommand("init", "Initialize a new project", "help <command>", this::init);
        registerCommand("build", "Build a project and export the files to a given directory", "help <command>", this::build);
        registerCommand("serve", "Build a project and deploy a test environment for it.", "help <command>", this::serve);
    }

    public void run(final String[] args) {
        if (args == null || args.length == 0)
            return;

        var commandName = args[0];
        var command = commandMap.get(commandName);

        if (command == null)
            return;

        command.executor().execute(parseArgs());
    }

    public Map<String, String> parseArgs() {
        return Map.of();
    }

    private void init(final Map<String, String> args) {

    }

    private void build(final Map<String, String> args) {

    }

    private void serve(final Map<String, String> args) {

    }

    public void registerCommand(String name, String description, String example, CommandExecutor executor) {
        Objects.requireNonNull(name, "Commands cannot have a null name,");
        Objects.requireNonNull(description, "Commands require a description.");
        Objects.requireNonNull(description, "Commands require an example.");
        Objects.requireNonNull(executor, "Commands require an executor.");

        commandMap.put(name, new Command(description, example, executor));
    }

    private record Command(String description, String example, CommandExecutor executor) {}

    public interface CommandExecutor {
        void execute(Map<String, String> args);
    }

}
