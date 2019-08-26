package edu.kit.iti.formal.keyserver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * @author Alexander Weigl
 * @version 1 (26.08.19)
 */
public class BackendTest {
    private Backend backend;

    @Before
    public void setup() {
        backend = new Backend();
    }


    @Test
    public void test1() {
        String tok1 = backend.add("w@e.com", "thisismykey");
        String tok2 = backend.add("q@b.eu", "yekymsisiht");

        backend.confirmAdd(tok2);
        backend.confirmAdd(tok1);

        String key1 = backend.get("w@e.com");
        String key2 = backend.get("q@b.eu");

        Assert.assertEquals(key1, "thisismykey");
        Assert.assertEquals(key2, "yekymsisiht");

        String tok3 = backend.del("w@e.com", "thisismykey");
        String tok4 = backend.del("q@b.eu", "yekymsisiht");

        backend.confirmDel(tok4);
        try {
            backend.get("q@b.eu");
            Assert.fail("Exception expected");
        } catch (NoSuchElementException ignored) {
        }
    }
}