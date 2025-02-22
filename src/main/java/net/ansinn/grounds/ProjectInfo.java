package net.ansinn.grounds;

import java.nio.file.Path;

public record ProjectInfo(
        Path rootPath,
        Path configPath,
        Path contentsPath,
        Path componentsPath,
        Path templatesPath
) { }
