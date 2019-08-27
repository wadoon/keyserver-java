package edu.kit.iti.formal.keyserver;

import com.blade.Blade;
import com.blade.mvc.RouteContext;
import com.blade.mvc.http.Response;

import java.util.function.Consumer;

/**
 * @author Alexander Weigl
 * @version 1 (26.08.19)
 */
public class Frontend {
    private static Backend backend = new Backend();

    public static void main(String[] args) {
        Blade.of()
                .get("/get/:email", Frontend::get)
                .post("/add/:email", Frontend::add)
                .get("/add!/:token", Frontend::addc)
                .post("/del/:email", Frontend::del)
                .get("/del!/:token", Frontend::delc)
                .start();
    }

    private static void del(RouteContext routeContext) {
        String e = routeContext.pathString(":email");
        String k = routeContext.bodyToString();
        handleException(routeContext.response(), f -> {
            String token = backend.del(e, k);
            routeContext.response().json(token);
        });
    }

    private static void delc(RouteContext routeContext) {
        String token = routeContext.pathString(":token");
        handleException(routeContext.response(), f ->
                backend.confirmDel(token));
    }

    private static void addc(RouteContext routeContext) {
        String token = routeContext.pathString(":token");
        handleException(routeContext.response(), (a) -> backend.confirmAdd(token));
    }

    private static void handleException(Response response, Consumer<Void> f) {
        try {
            f.accept(null);
        } catch (Exception e) {
            response.json(e.getMessage());
            response.status(500);
        }
    }

    private static void add(RouteContext routeContext) {
        String e = routeContext.pathString(":email");
        String k = routeContext.bodyToString();
        handleException(routeContext.response(), f -> {
            String token = backend.add(e, k);
            routeContext.response().json(token);
        });
    }

    private static void get(RouteContext routeContext) {
        String email = routeContext.pathString(":email");
        handleException(routeContext.response(), f ->
                routeContext.response().json(backend.get(email)));
    }
}
