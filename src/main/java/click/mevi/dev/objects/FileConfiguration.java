package click.mevi.dev.objects;

import click.mevi.dev.FileConfigurationAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class FileConfiguration {
    protected final Path directory;
    protected final String fileName;

    public FileConfiguration(Path directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
    }

    public abstract void save() throws IOException;
    public abstract void create() throws IOException;

    public boolean exists() {
        return Files.exists(Path.of(directory.toString(), fileName));
    }
    public boolean delete() throws IOException {
        return Files.deleteIfExists(Path.of(directory.toString(), fileName));
    }

    public abstract <T> T get(String key, Class<T> clazz);
    public abstract void set(String key, Object value);

    public Path getDirectory() {
        return directory;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return new File(directory.toString(), fileName);
    }
}
