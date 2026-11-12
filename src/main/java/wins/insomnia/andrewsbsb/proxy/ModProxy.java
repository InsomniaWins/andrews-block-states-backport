package wins.insomnia.andrewsbsb.proxy;

public abstract class ModProxy {

    private final String id;

    public ModProxy(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract boolean isClientProxy();

}
