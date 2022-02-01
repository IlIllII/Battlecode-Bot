package player35;

import battlecode.common.*;

strictfp class LaboratoryStrategy {

    static final int TRANSMUTATION_RATE = 3; // lower is faster

    static void run(RobotController rc) throws GameActionException {
        if (rc.getRoundNum() % TRANSMUTATION_RATE == 0 && rc.isActionReady() && rc.canTransmute()) {
            rc.transmute();
        }
    }
}
