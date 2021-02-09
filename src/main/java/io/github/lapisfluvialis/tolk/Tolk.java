package io.github.lapisfluvialis.tolk;

import java.lang.instrument.Instrumentation;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Tolk {
    public static void premain(String agentArgs, Instrumentation inst) {
        String[] args = agentArgs.split(",", 0);

        Map<String, String> mappings = new HashMap<>();
        mappings.put("Language", args[0]);
        mappings.put("loadFromJson", args[1]);

        String jsonPath = Paths.get(args[2]).toAbsolutePath().toString();

        inst.addTransformer(new LanguageTransformer(mappings, jsonPath));
    }
}
