package com.github.jan5757.restaurantvoting.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;

@UtilityClass
public class TimeUtil {
    private static Clock clock = Clock.systemDefaultZone();

    public static void setClock(Clock newClock) {
        clock = newClock;
    }

    public static Clock getClock() {
        return clock;
    }
}
