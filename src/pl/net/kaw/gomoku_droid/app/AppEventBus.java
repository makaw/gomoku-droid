/*
 *  Gomoku 4 Android
 *  https://github.com/makaw/gomoku-droid
 *  
 */
package pl.net.kaw.gomoku_droid.app;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;


/**
 * Szyna zdarzeń: statyczny wrapper dla Guava EventBus
 * 
 * @author Maciej Kawecki
 *
 */
public class AppEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        AppBase.getEventBus().eventBus.post(event);
    }

    public static void register(final Object object) {
        AppBase.getEventBus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        AppBase.getEventBus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
    
}
