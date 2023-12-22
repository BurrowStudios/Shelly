package org.burrow_studios.shelly.util;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TurtleUtilTest {
    @Test
    void checkTime() {
        final long millis    = System.currentTimeMillis();
        final int  service   = new Random().nextInt(1 << TurtleUtil.SERVICE_BITS);
        final int  increment = new Random().nextInt(1 << TurtleUtil.INCREMENT_BITS);

        final long id = TurtleUtil.newId(millis, service, increment);

        assertEquals(millis, TurtleUtil.getTime(id));
    }

    @Test
    void checkService() {
        final int max = 1 << TurtleUtil.SERVICE_BITS;

        final long millis    = System.currentTimeMillis();
        final int  increment = new Random().nextInt(1 << TurtleUtil.INCREMENT_BITS);

        for (int i = 0; i < max; i++) {
            final long id = TurtleUtil.newId(millis, i, increment);

            assertEquals(i, TurtleUtil.getService(id));
        }
    }

    @Test
    void checkIncrement() {
        final int max = 1 << TurtleUtil.INCREMENT_BITS;

        final long millis  = System.currentTimeMillis();
        final int  service = (int) TurtleUtil.SERVICE;

        for (int i = 0; i < max; i++) {
            final long id = TurtleUtil.newId(millis, service, i);

            assertEquals(i, TurtleUtil.getIncrement(id));
        }
    }
}