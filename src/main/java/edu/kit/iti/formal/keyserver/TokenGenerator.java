package edu.kit.iti.formal.keyserver;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

/**
 * @author Alexander Weigl
 * @version 1 (26.08.19)
 */
public class TokenGenerator {
    public @NotNull String freshToken() {
        return UUID.randomUUID().toString();
    }
}
