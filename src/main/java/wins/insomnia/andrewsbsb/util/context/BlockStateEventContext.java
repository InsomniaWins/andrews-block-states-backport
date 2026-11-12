package wins.insomnia.andrewsbsb.util.context;

import wins.insomnia.andrewsbsb.event.custom.SetBlockStateEvent;

public class BlockStateEventContext {

    private static final ThreadLocal<SetBlockStateEvent.Context> CONTEXT = new ThreadLocal<>();

    public static SetBlockStateEvent.Context get() {
        SetBlockStateEvent.Context context = CONTEXT.get();
        if (context == null) {
            return SetBlockStateEvent.UNKNOWN;
        }

        return context;
    }

    public static Scope push(SetBlockStateEvent.Context context) {
        SetBlockStateEvent.Context previous = CONTEXT.get();
        CONTEXT.set(context);
        return new Scope(previous);
    }

    public static final class Scope implements AutoCloseable {

        private final SetBlockStateEvent.Context previous;
        private boolean closed;

        private Scope(SetBlockStateEvent.Context previous) {
            this.previous = previous;
        }

        @Override
        public void close() {
            if (closed) {
                return;
            }

            closed = true;

            if (previous == null) {
                CONTEXT.remove();
            } else {
                CONTEXT.set(previous);
            }
        }
    }

}
