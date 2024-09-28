package modules;

import com.google.inject.AbstractModule;
import services.AuctionScheduler;

public class AuctionSchedulerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuctionScheduler.class).asEagerSingleton();
    }
}
