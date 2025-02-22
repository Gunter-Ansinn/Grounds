package net.ansinn.grounds;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * A wrapper class for the Properties type. This class produces a string with interpolated data.
 *
 * @see Properties
 */
public class Config extends Properties {

    private static final Pattern injectionSyntax = Pattern.compile("\\{(.+?)\\}");
    private static final Map<String, BiFunction<Config, String[], String>> DynamicValues = new HashMap<>();

    @Override
    public String getProperty(String key) {
        var original = super.getProperty(key);

        if (original != null)
            return resolveProperty(original);
        else
            return super.getProperty(key);
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    private String resolveProperty(String propertyInput) {
        var matcher = injectionSyntax.matcher(propertyInput);
        var sb = new StringBuilder();

        while (matcher.find()) {
            var placeholder = matcher.group(1);
            var tokens = placeholder.split("\\.");

            var key = tokens[0];
            var args = new String[0];
            if (tokens.length > 1)
                args = Arrays.copyOfRange(tokens, 1, tokens.length);

            var replacement = DynamicValues.getOrDefault(key, (c, a) -> "{" + placeholder + "}");

            matcher.appendReplacement(sb, replacement.apply(this, args));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    public static void registerDynamicProvider(String key, BiFunction<Config, String[], String> replacementFunction) {
        Objects.requireNonNull(key, "Dynamic providers require a key");
        Objects.requireNonNull(replacementFunction, "Dynamic providers require a function to parse data.");

        DynamicValues.put(key, replacementFunction);
    }

    static {
        registerDynamicProvider("now",         (config, args) -> Instant.now().toString());
        registerDynamicProvider("today",       (config, args) -> {
            var now = Instant.now();
            var zonedNow = now.atZone(ZoneId.systemDefault());

            var formatter = switch (args[0]) {
                case "day" -> DateTimeFormatter.ofPattern("EEEE");
                case "month" -> DateTimeFormatter.ofPattern("MMMM");
                case "year" -> DateTimeFormatter.ofPattern("yyyy");
                default -> DateTimeFormatter.ofPattern("M/d/yyyy");
            };

            return zonedNow.format(formatter);
        });
        registerDynamicProvider("properties",  (config, args) -> {
            Objects.requireNonNull(config, "This function shouldn't be called outside of Config.class");
            Objects.requireNonNull(args, "This function requires arguments to read properties.");

            // Join the arguments with a dot (.) separator
            String joinedArgs = String.join(".", args);

            // Get the property value from the config
            String property = config.getProperty(joinedArgs);

            // Return the property if it exists, otherwise return the default key
            return property != null ? property : "properties." + joinedArgs;
        });
    }
}
