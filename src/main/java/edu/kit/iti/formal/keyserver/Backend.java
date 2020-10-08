package edu.kit.iti.formal.keyserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

/**
 * @author Alexander Weigl
 * @version 1 (26.08.19)
 */
public class Backend {
    private final TokenGenerator tokenGenerator = new TokenGenerator();
    private final Map<String, EmailKey> waitConfirmAdd = new HashMap<>();
    private final Map<String, EmailKey> waitConfirmDel = new HashMap<>();
    private final List<EmailKey> database = new LinkedList<>();

    public @NotNull String add(String email, String key) {
        if (email == null || key == null) {
            throw new IllegalArgumentException("Either email or key is not given.");
        }
        String token = tokenGenerator.freshToken();
        waitConfirmAdd.put(token, new EmailKey(email, key));
        return token;
    }

    public void confirmAdd(String token) {
        if (token == null) {
            throw new IllegalArgumentException("No token given.");
        }
        EmailKey ek = waitConfirmAdd.get(token);
        if (ek == null) {
            System.out.println(waitConfirmAdd.keySet());
            throw new IllegalStateException("Unkown token: " + token);
        }
        database.add(ek);
    }

    public @Nullable String get(@Nullable String email) {
        if (email == null || email.isBlank())
            return null;
        return findByEmail(email).map(it -> it.key).orElseThrow();
    }

    public String del(@NotNull String email, @NotNull String key) {
        @NotNull Optional<EmailKey> ek = findBy(email, key);
        if (ek.isPresent()) {
            String token = tokenGenerator.freshToken();
            waitConfirmDel.put(token, ek.get());
            return token;
        }
        throw new IllegalStateException("Key unknown");
    }

    public void confirmDel(@NotNull String token) {
        EmailKey ek = waitConfirmDel.get(token);
        if (ek != null) {
            database.remove(ek);
        } else {
            throw new IllegalStateException("Token unknown.");
        }
    }

    @NotNull
    private Optional<EmailKey> findByEmail(@NotNull String email) {
        return database.stream()
                .filter(it -> it.email.equals(email))
                .findAny();
    }

    @NotNull
    private Optional<EmailKey> findBy(@NotNull String email, @NotNull String key) {
        return database.stream()
                .filter(it -> it.email.equals(email) && it.key.equals(key))
                .findAny();
    }

    private static class EmailKey {
        public final String email, key;

        private EmailKey(String email, String key) {
            this.email = email;
            this.key = key;
        }
    }

    public void save(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(database);
            oos.writeObject(waitConfirmAdd);
            oos.writeObject(waitConfirmDel);
        }
    }

    public void load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file))) {
            List<EmailKey> database = (List<EmailKey>) oos.readObject();
            Map<String, EmailKey> waitConfirmAdd = (Map<String, EmailKey>) oos.readObject();
            Map<String, EmailKey> waitConfirmDel = (Map<String, EmailKey>) oos.readObject();

            this.database.clear();
            this.database.addAll(database);

            this.waitConfirmAdd.clear();
            this.waitConfirmAdd.putAll(waitConfirmAdd);

            this.waitConfirmDel.clear();
            this.waitConfirmDel.putAll(waitConfirmDel);
        }
    }
}