package net.neferett.httpserver.api.Routing;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;
import net.neferett.httpserver.api.Types.HttpTypes;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@Data
public class RouterManager {

    private final HttpTypes type;

    List<RoutingProperties> routes = new ArrayList<>();

    @Delegate
    private List<Class<? extends RoutingProperties>> clazz = new ArrayList<>();

    @SneakyThrows
    private void instantiate(Class<? extends RoutingProperties> clazz) {
        Constructor constructor = clazz.getConstructors()[0];

        RoutingProperties properties = (RoutingProperties) constructor.newInstance();

        properties.setName(clazz.getAnnotation(Route.class).name());
        properties.setType(this.type);
        properties.setSetParams(clazz.getAnnotation(Route.class).params());

        this.routes.add(properties);
    }

    private void createInstance() {
        this.clazz
                .stream()
                .filter(e -> e.isAnnotationPresent(Route.class) && e.getAnnotation(Route.class).activated())
                .forEach(this::instantiate);
    }

    @SneakyThrows
    private Class<? extends RoutingProperties> buildClass(String path, String name) {
        return (Class<? extends RoutingProperties>) Class.forName(path + "." + name);
    }

    public void addFromPath(String path, String name) {
        this.clazz.add(this.buildClass(path, name));
    }

    public void addFromPathList(String path, List<String> names) {
        names.forEach(e -> this.clazz.add(this.buildClass(path, e)));
    }

    public void addFromList(List<Class<? extends RoutingProperties>> classList) {
        this.clazz.addAll(classList);
    }

    public void build() {
        this.createInstance();
    }
}