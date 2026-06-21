package click.mevi.dev.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class YAMLConfiguration extends FileConfiguration {
    private final HashMap<String, Object> values;

    public YAMLConfiguration(Path parentDirectory, String fileName) {
        super(parentDirectory, fileName);
        this.values = new HashMap<>();
    }
    private YAMLConfiguration(File file, HashMap<String, Object> values) {
        super(Path.of(file.getParent()), Path.of(file.getPath()).getFileName().toString());
        this.values = values;
    }

    @Override
    public void save() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        if (!getFile().exists()) {
            create();
        }
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(getFile(), values);
    }

    @Override
    public void create() throws IOException {
        File file = new File(directory.toFile(), fileName);

        if (file.exists()) {
            System.out.println("File " + fileName + " already exists, avoiding it...");
            return;
        }

        if (file.createNewFile()) {
            System.out.println("File " + fileName + " was created.");
        }
    }

    public static YAMLConfiguration load(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        HashMap<String, Object> values = (HashMap<String, Object>) objectMapper.readValue(file, HashMap.class);

        return new YAMLConfiguration(file, values);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String[] parts = key.split("\\.");

        Object current = values;

        for (String part : parts) {
            if (!(current instanceof Map<?, ?> map)) return null;

            current = map.get(part);

            if (current == null) return null;
        }

        return clazz.cast(current);
    }

    @Override
    public void set(String key, Object value) {
        String[] parts = key.split("\\.");

        Map<String, Object> current = values;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];

            Object next = current.get(part);

            if (!(next instanceof Map)) {
                next = new HashMap<String, Object>();
                current.put(part, next);
            }

            current = (Map<String, Object>) next;
        }

        current.put(parts[parts.length - 1], value);
    }
}
