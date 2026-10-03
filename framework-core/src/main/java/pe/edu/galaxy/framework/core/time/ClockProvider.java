package pe.edu.galaxy.framework.core.time;

import java.time.Clock;

@FunctionalInterface
public interface ClockProvider {

    Clock current();

    static ClockProvider systemUtc() {
        return Clock::systemUTC;
    }
}
