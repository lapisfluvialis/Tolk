# Tolk
## Usage (1.16.5)
```sh
$ java -javaagent:tolk.jar=ly,a,ja_jp.json -jar server.jar nogui
```

### Agent Arguments
1. `net.minecraft.locale.Language`
2. `void loadFromJson(java.io.InputStream,java.util.function.BiConsumer)`
3. path to lang file

- 1st & 2nd args are in the obfuscation map: `server.json`

## Build
```sh
$ ./gradlew shadowJar
```
