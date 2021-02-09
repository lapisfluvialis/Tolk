package io.github.lapisfluvialis.tolk;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;

public class LanguageTransformer implements ClassFileTransformer {
    private final Map<String, String> mappings;
    private final String jsonPath;

    public LanguageTransformer(Map<String, String> mappings, String jsonPath) {
        this.mappings = mappings;
        this.jsonPath = jsonPath;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.equals(mappings.get("Language"))) return null;

        ClassPool cp = ClassPool.getDefault();

        try (InputStream is = new ByteArrayInputStream(classfileBuffer)) {
            CtClass cc = cp.makeClass(is);
            CtClass[] params = cp.get(new String[]{"java.io.InputStream", "java.util.function.BiConsumer"});
            CtMethod cm = cc.getDeclaredMethod(mappings.get("loadFromJson"), params);

            cm.insertBefore("$1 = java.nio.file.Files.newInputStream(java.nio.file.Paths.get(\"" + jsonPath + "\", new String[0]), new java.nio.file.OpenOption[0]);");
            cm.insertAfter("$1.close();");

            return cc.toBytecode();
        } catch (IOException | NotFoundException | CannotCompileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
