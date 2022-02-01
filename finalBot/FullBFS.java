package player35;

import battlecode.common.*;

public class FullBFS {

    /*
     * These static variables describe all tiles within vision range of a
     * soldier, meaning it is every tile within a squared radius of 20 from
     * an arbitrary origin.
     * 
     * Each tile can be thought of as an object containing:
     * - A tile location. Prefix by l, for location.
     * - A rubble count for that tile. Prefixed by p, for passability.
     * - The path cost from the origin to that tile. Prefixed by v, for value.
     * - The initial direction you should move from the origin to follow
     * the path to the tile in question. Previxed by d, for direction.
     * 
     * We don't package them into classes or objects because we want to save on
     * bytecode.
     */
    static MapLocation l17;
    static int v17;
    static Direction d17;
    static int p17;

    static MapLocation l18;
    static int v18;
    static Direction d18;
    static int p18;

    static MapLocation l19;
    static int v19;
    static Direction d19;
    static int p19;

    static MapLocation l20;
    static int v20;
    static Direction d20;
    static int p20;

    static MapLocation l21;
    static int v21;
    static Direction d21;
    static int p21;

    static MapLocation l29;
    static int v29;
    static Direction d29;
    static int p29;

    static MapLocation l30;
    static int v30;
    static Direction d30;
    static int p30;

    static MapLocation l31;
    static int v31;
    static Direction d31;
    static int p31;

    static MapLocation l32;
    static int v32;
    static Direction d32;
    static int p32;

    static MapLocation l33;
    static int v33;
    static Direction d33;
    static int p33;

    static MapLocation l34;
    static int v34;
    static Direction d34;
    static int p34;

    static MapLocation l35;
    static int v35;
    static Direction d35;
    static int p35;

    static MapLocation l41;
    static int v41;
    static Direction d41;
    static int p41;

    static MapLocation l42;
    static int v42;
    static Direction d42;
    static int p42;

    static MapLocation l43;
    static int v43;
    static Direction d43;
    static int p43;

    static MapLocation l44;
    static int v44;
    static Direction d44;
    static int p44;

    static MapLocation l45;
    static int v45;
    static Direction d45;
    static int p45;

    static MapLocation l46;
    static int v46;
    static Direction d46;
    static int p46;

    static MapLocation l47;
    static int v47;
    static Direction d47;
    static int p47;

    static MapLocation l48;
    static int v48;
    static Direction d48;
    static int p48;

    static MapLocation l49;
    static int v49;
    static Direction d49;
    static int p49;

    static MapLocation l53;
    static int v53;
    static Direction d53;
    static int p53;

    static MapLocation l54;
    static int v54;
    static Direction d54;
    static int p54;

    static MapLocation l55;
    static int v55;
    static Direction d55;
    static int p55;

    static MapLocation l56;
    static int v56;
    static Direction d56;
    static int p56;

    static MapLocation l57;
    static int v57;
    static Direction d57;
    static int p57;

    static MapLocation l58;
    static int v58;
    static Direction d58;
    static int p58;

    static MapLocation l59;
    static int v59;
    static Direction d59;
    static int p59;

    static MapLocation l60;
    static int v60;
    static Direction d60;
    static int p60;

    static MapLocation l61;
    static int v61;
    static Direction d61;
    static int p61;

    static MapLocation l62;
    static int v62;
    static Direction d62;
    static int p62;

    static MapLocation l63;
    static int v63;
    static Direction d63;
    static int p63;

    static MapLocation l66;
    static int v66;
    static Direction d66;
    static int p66;

    static MapLocation l67;
    static int v67;
    static Direction d67;
    static int p67;

    static MapLocation l68;
    static int v68;
    static Direction d68;
    static int p68;

    static MapLocation l69;
    static int v69;
    static Direction d69;
    static int p69;

    static MapLocation l70;
    static int v70;
    static Direction d70;
    static int p70;

    static MapLocation l71;
    static int v71;
    static Direction d71;
    static int p71;

    static MapLocation l72;
    static int v72;
    static Direction d72;
    static int p72;

    static MapLocation l73;
    static int v73;
    static Direction d73;
    static int p73;

    static MapLocation l74;
    static int v74;
    static Direction d74;
    static int p74;

    static MapLocation l75;
    static int v75;
    static Direction d75;
    static int p75;

    static MapLocation l76;
    static int v76;
    static Direction d76;
    static int p76;

    static MapLocation l79;
    static int v79;
    static Direction d79;
    static int p79;

    static MapLocation l80;
    static int v80;
    static Direction d80;
    static int p80;

    static MapLocation l81;
    static int v81;
    static Direction d81;
    static int p81;

    static MapLocation l82;
    static int v82;
    static Direction d82;
    static int p82;

    static MapLocation l83;
    static int v83;
    static Direction d83;
    static int p83;

    static MapLocation l84;
    static int v84;
    static Direction d84;
    static int p84;

    static MapLocation l85;
    static int v85;
    static Direction d85;
    static int p85;

    static MapLocation l86;
    static int v86;
    static Direction d86;
    static int p86;

    static MapLocation l87;
    static int v87;
    static Direction d87;
    static int p87;

    static MapLocation l88;
    static int v88;
    static Direction d88;
    static int p88;

    static MapLocation l89;
    static int v89;
    static Direction d89;
    static int p89;

    static MapLocation l92;
    static int v92;
    static Direction d92;
    static int p92;

    static MapLocation l93;
    static int v93;
    static Direction d93;
    static int p93;

    static MapLocation l94;
    static int v94;
    static Direction d94;
    static int p94;

    static MapLocation l95;
    static int v95;
    static Direction d95;
    static int p95;

    static MapLocation l96;
    static int v96;
    static Direction d96;
    static int p96;

    static MapLocation l97;
    static int v97;
    static Direction d97;
    static int p97;

    static MapLocation l98;
    static int v98;
    static Direction d98;
    static int p98;

    static MapLocation l99;
    static int v99;
    static Direction d99;
    static int p99;

    static MapLocation l100;
    static int v100;
    static Direction d100;
    static int p100;

    static MapLocation l101;
    static int v101;
    static Direction d101;
    static int p101;

    static MapLocation l102;
    static int v102;
    static Direction d102;
    static int p102;

    static MapLocation l105;
    static int v105;
    static Direction d105;
    static int p105;

    static MapLocation l106;
    static int v106;
    static Direction d106;
    static int p106;

    static MapLocation l107;
    static int v107;
    static Direction d107;
    static int p107;

    static MapLocation l108;
    static int v108;
    static Direction d108;
    static int p108;

    static MapLocation l109;
    static int v109;
    static Direction d109;
    static int p109;

    static MapLocation l110;
    static int v110;
    static Direction d110;
    static int p110;

    static MapLocation l111;
    static int v111;
    static Direction d111;
    static int p111;

    static MapLocation l112;
    static int v112;
    static Direction d112;
    static int p112;

    static MapLocation l113;
    static int v113;
    static Direction d113;
    static int p113;

    static MapLocation l114;
    static int v114;
    static Direction d114;
    static int p114;

    static MapLocation l115;
    static int v115;
    static Direction d115;
    static int p115;

    static MapLocation l119;
    static int v119;
    static Direction d119;
    static int p119;

    static MapLocation l120;
    static int v120;
    static Direction d120;
    static int p120;

    static MapLocation l121;
    static int v121;
    static Direction d121;
    static int p121;

    static MapLocation l122;
    static int v122;
    static Direction d122;
    static int p122;

    static MapLocation l123;
    static int v123;
    static Direction d123;
    static int p123;

    static MapLocation l124;
    static int v124;
    static Direction d124;
    static int p124;

    static MapLocation l125;
    static int v125;
    static Direction d125;
    static int p125;

    static MapLocation l126;
    static int v126;
    static Direction d126;
    static int p126;

    static MapLocation l127;
    static int v127;
    static Direction d127;
    static int p127;

    static MapLocation l133;
    static int v133;
    static Direction d133;
    static int p133;

    static MapLocation l134;
    static int v134;
    static Direction d134;
    static int p134;

    static MapLocation l135;
    static int v135;
    static Direction d135;
    static int p135;

    static MapLocation l136;
    static int v136;
    static Direction d136;
    static int p136;

    static MapLocation l137;
    static int v137;
    static Direction d137;
    static int p137;

    static MapLocation l138;
    static int v138;
    static Direction d138;
    static int p138;

    static MapLocation l139;
    static int v139;
    static Direction d139;
    static int p139;

    static MapLocation l147;
    static int v147;
    static Direction d147;
    static int p147;

    static MapLocation l148;
    static int v148;
    static Direction d148;
    static int p148;

    static MapLocation l149;
    static int v149;
    static Direction d149;
    static int p149;

    static MapLocation l150;
    static int v150;
    static Direction d150;
    static int p150;

    static MapLocation l151;
    static int v151;
    static Direction d151;
    static int p151;

    /**
     * Bytecode cost: 6300.
     * 
     * Performs unrolled breadth first search with soldier vision radius.
     * 
     * @param rc
     * @param target - target location that can be outside of vision range.
     * @return direction to move from origin.
     */
    static Direction getBestDir(RobotController rc, MapLocation target) {

        // We populate static variables with actual tiles from environment.
        // Costs 870 bytecode.
        l84 = rc.getLocation();
        v84 = 0;
        l85 = l84.add(Direction.NORTH);
        v85 = 1000000;
        d85 = null;
        l72 = l85.add(Direction.WEST);
        v72 = 1000000;
        d72 = null;
        l71 = l72.add(Direction.SOUTH);
        v71 = 1000000;
        d71 = null;
        l70 = l71.add(Direction.SOUTH);
        v70 = 1000000;
        d70 = null;
        l83 = l70.add(Direction.EAST);
        v83 = 1000000;
        d83 = null;
        l96 = l83.add(Direction.EAST);
        v96 = 1000000;
        d96 = null;
        l97 = l96.add(Direction.NORTH);
        v97 = 1000000;
        d97 = null;
        l98 = l97.add(Direction.NORTH);
        v98 = 1000000;
        d98 = null;
        l99 = l98.add(Direction.NORTH);
        v99 = 1000000;
        d99 = null;
        l86 = l99.add(Direction.WEST);
        v86 = 1000000;
        d86 = null;
        l73 = l86.add(Direction.WEST);
        v73 = 1000000;
        d73 = null;
        l60 = l73.add(Direction.WEST);
        v60 = 1000000;
        d60 = null;
        l59 = l60.add(Direction.SOUTH);
        v59 = 1000000;
        d59 = null;
        l58 = l59.add(Direction.SOUTH);
        v58 = 1000000;
        d58 = null;
        l57 = l58.add(Direction.SOUTH);
        v57 = 1000000;
        d57 = null;
        l56 = l57.add(Direction.SOUTH);
        v56 = 1000000;
        d56 = null;
        l69 = l56.add(Direction.EAST);
        v69 = 1000000;
        d69 = null;
        l82 = l69.add(Direction.EAST);
        v82 = 1000000;
        d82 = null;
        l95 = l82.add(Direction.EAST);
        v95 = 1000000;
        d95 = null;
        l108 = l95.add(Direction.EAST);
        v108 = 1000000;
        d108 = null;
        l109 = l108.add(Direction.NORTH);
        v109 = 1000000;
        d109 = null;
        l110 = l109.add(Direction.NORTH);
        v110 = 1000000;
        d110 = null;
        l111 = l110.add(Direction.NORTH);
        v111 = 1000000;
        d111 = null;
        l112 = l111.add(Direction.NORTH);
        v112 = 1000000;
        d112 = null;
        l100 = l112.add(Direction.NORTHWEST);
        v100 = 1000000;
        d100 = null;
        l87 = l100.add(Direction.WEST);
        v87 = 1000000;
        d87 = null;
        l74 = l87.add(Direction.WEST);
        v74 = 1000000;
        d74 = null;
        l61 = l74.add(Direction.WEST);
        v61 = 1000000;
        d61 = null;
        l47 = l61.add(Direction.SOUTHWEST);
        v47 = 1000000;
        d47 = null;
        l46 = l47.add(Direction.SOUTH);
        v46 = 1000000;
        d46 = null;
        l45 = l46.add(Direction.SOUTH);
        v45 = 1000000;
        d45 = null;
        l44 = l45.add(Direction.SOUTH);
        v44 = 1000000;
        d44 = null;
        l43 = l44.add(Direction.SOUTH);
        v43 = 1000000;
        d43 = null;
        l55 = l43.add(Direction.SOUTHEAST);
        v55 = 1000000;
        d55 = null;
        l68 = l55.add(Direction.EAST);
        v68 = 1000000;
        d68 = null;
        l81 = l68.add(Direction.EAST);
        v81 = 1000000;
        d81 = null;
        l94 = l81.add(Direction.EAST);
        v94 = 1000000;
        d94 = null;
        l107 = l94.add(Direction.EAST);
        v107 = 1000000;
        d107 = null;
        l121 = l107.add(Direction.NORTHEAST);
        v121 = 1000000;
        d121 = null;
        l122 = l121.add(Direction.NORTH);
        v122 = 1000000;
        d122 = null;
        l123 = l122.add(Direction.NORTH);
        v123 = 1000000;
        d123 = null;
        l124 = l123.add(Direction.NORTH);
        v124 = 1000000;
        d124 = null;
        l125 = l124.add(Direction.NORTH);
        v125 = 1000000;
        d125 = null;
        l113 = l125.add(Direction.NORTHWEST);
        v113 = 1000000;
        d113 = null;
        l101 = l113.add(Direction.NORTHWEST);
        v101 = 1000000;
        d101 = null;
        l88 = l101.add(Direction.WEST);
        v88 = 1000000;
        d88 = null;
        l75 = l88.add(Direction.WEST);
        v75 = 1000000;
        d75 = null;
        l62 = l75.add(Direction.WEST);
        v62 = 1000000;
        d62 = null;
        l48 = l62.add(Direction.SOUTHWEST);
        v48 = 1000000;
        d48 = null;
        l34 = l48.add(Direction.SOUTHWEST);
        v34 = 1000000;
        d34 = null;
        l33 = l34.add(Direction.SOUTH);
        v33 = 1000000;
        d33 = null;
        l32 = l33.add(Direction.SOUTH);
        v32 = 1000000;
        d32 = null;
        l31 = l32.add(Direction.SOUTH);
        v31 = 1000000;
        d31 = null;
        l30 = l31.add(Direction.SOUTH);
        v30 = 1000000;
        d30 = null;
        l42 = l30.add(Direction.SOUTHEAST);
        v42 = 1000000;
        d42 = null;
        l54 = l42.add(Direction.SOUTHEAST);
        v54 = 1000000;
        d54 = null;
        l67 = l54.add(Direction.EAST);
        v67 = 1000000;
        d67 = null;
        l80 = l67.add(Direction.EAST);
        v80 = 1000000;
        d80 = null;
        l93 = l80.add(Direction.EAST);
        v93 = 1000000;
        d93 = null;
        l106 = l93.add(Direction.EAST);
        v106 = 1000000;
        d106 = null;
        l120 = l106.add(Direction.NORTHEAST);
        v120 = 1000000;
        d120 = null;
        l134 = l120.add(Direction.NORTHEAST);
        v134 = 1000000;
        d134 = null;
        l135 = l134.add(Direction.NORTH);
        v135 = 1000000;
        d135 = null;
        l136 = l135.add(Direction.NORTH);
        v136 = 1000000;
        d136 = null;
        l137 = l136.add(Direction.NORTH);
        v137 = 1000000;
        d137 = null;
        l138 = l137.add(Direction.NORTH);
        v138 = 1000000;
        d138 = null;
        l126 = l138.add(Direction.NORTHWEST);
        v126 = 1000000;
        d126 = null;
        l114 = l126.add(Direction.NORTHWEST);
        v114 = 1000000;
        d114 = null;
        l102 = l114.add(Direction.NORTHWEST);
        v102 = 1000000;
        d102 = null;
        l89 = l102.add(Direction.WEST);
        v89 = 1000000;
        d89 = null;
        l76 = l89.add(Direction.WEST);
        v76 = 1000000;
        d76 = null;
        l63 = l76.add(Direction.WEST);
        v63 = 1000000;
        d63 = null;
        l49 = l63.add(Direction.SOUTHWEST);
        v49 = 1000000;
        d49 = null;
        l35 = l49.add(Direction.SOUTHWEST);
        v35 = 1000000;
        d35 = null;
        l21 = l35.add(Direction.SOUTHWEST);
        v21 = 1000000;
        d21 = null;
        l20 = l21.add(Direction.SOUTH);
        v20 = 1000000;
        d20 = null;
        l19 = l20.add(Direction.SOUTH);
        v19 = 1000000;
        d19 = null;
        l18 = l19.add(Direction.SOUTH);
        v18 = 1000000;
        d18 = null;
        l17 = l18.add(Direction.SOUTH);
        v17 = 1000000;
        d17 = null;
        l29 = l17.add(Direction.SOUTHEAST);
        v29 = 1000000;
        d29 = null;
        l41 = l29.add(Direction.SOUTHEAST);
        v41 = 1000000;
        d41 = null;
        l53 = l41.add(Direction.SOUTHEAST);
        v53 = 1000000;
        d53 = null;
        l66 = l53.add(Direction.EAST);
        v66 = 1000000;
        d66 = null;
        l79 = l66.add(Direction.EAST);
        v79 = 1000000;
        d79 = null;
        l92 = l79.add(Direction.EAST);
        v92 = 1000000;
        d92 = null;
        l105 = l92.add(Direction.EAST);
        v105 = 1000000;
        d105 = null;
        l119 = l105.add(Direction.NORTHEAST);
        v119 = 1000000;
        d119 = null;
        l133 = l119.add(Direction.NORTHEAST);
        v133 = 1000000;
        d133 = null;
        l147 = l133.add(Direction.NORTHEAST);
        v147 = 1000000;
        d147 = null;
        l148 = l147.add(Direction.NORTH);
        v148 = 1000000;
        d148 = null;
        l149 = l148.add(Direction.NORTH);
        v149 = 1000000;
        d149 = null;
        l150 = l149.add(Direction.NORTH);
        v150 = 1000000;
        d150 = null;
        l151 = l150.add(Direction.NORTH);
        v151 = 1000000;
        d151 = null;
        l139 = l151.add(Direction.NORTHWEST);
        v139 = 1000000;
        d139 = null;
        l127 = l139.add(Direction.NORTHWEST);
        v127 = 1000000;
        d127 = null;
        l115 = l127.add(Direction.NORTHWEST);
        v115 = 1000000;
        d115 = null;

        /*
         * Now we find the shortest weighted path to every
         * tile in our radius. We only check if immediately proximal
         * tiles are occupied both to save on bytecode and to improve
         * movement when units are tightly following eachother.
         * 
         * For the cost of the path, we take into account the amount of
         * rubble on a tile. Rubble slows down movement by multiplying
         * the cooldown by (1 + (rubble / 10)), meaning a 100 rubble tile
         * increases movement cooldown by a factor of 11.
         * 
         * We multiply above equation by 10 to arrive at a passability value
         * that is simply rubble + 10.
         */
        try {
            if (rc.canSenseLocation(l71)) {
                if (rc.isLocationOccupied(l71)) {
                    p71 = 100;
                } else {
                    p71 = rc.senseRubble(l71) + 10;
                }
                if (v71 > v84 + p71) {
                    v71 = v84 + p71;
                    d71 = Direction.WEST;
                }
            }
            if (rc.canSenseLocation(l83)) {
                if (rc.isLocationOccupied(l83)) {
                    p83 = 100;
                } else {
                    p83 = rc.senseRubble(l83) + 10;
                }
                if (v83 > v84 + p83) {
                    v83 = v84 + p83;
                    d83 = Direction.SOUTH;
                }
                if (v83 > v71 + p83) {
                    v83 = v71 + p83;
                    d83 = d71;
                }
            }
            if (rc.canSenseLocation(l85)) {
                if (rc.isLocationOccupied(l85)) {
                    p85 = 100;
                } else {
                    p85 = rc.senseRubble(l85) + 10;
                }
                if (v85 > v84 + p85) {
                    v85 = v84 + p85;
                    d85 = Direction.NORTH;
                }
                if (v85 > v71 + p85) {
                    v85 = v71 + p85;
                    d85 = d71;
                }
            }
            if (rc.canSenseLocation(l97)) {
                if (rc.isLocationOccupied(l97)) {
                    p97 = 100;
                } else {
                    p97 = rc.senseRubble(l97) + 10;
                }
                if (v97 > v84 + p97) {
                    v97 = v84 + p97;
                    d97 = Direction.EAST;
                }
                if (v97 > v85 + p97) {
                    v97 = v85 + p97;
                    d97 = d85;
                }
                if (v97 > v83 + p97) {
                    v97 = v83 + p97;
                    d97 = d83;
                }
            }
            if (rc.canSenseLocation(l70)) {
                if (rc.isLocationOccupied(l70)) {
                    p70 = 100;
                } else {
                    p70 = rc.senseRubble(l70) + 10;
                }
                if (v70 > v84 + p70) {
                    v70 = v84 + p70;
                    d70 = Direction.SOUTHWEST;
                }
                if (v70 > v71 + p70) {
                    v70 = v71 + p70;
                    d70 = d71;
                }
                if (v70 > v83 + p70) {
                    v70 = v83 + p70;
                    d70 = d83;
                }
            }
            if (rc.canSenseLocation(l72)) {
                if (rc.isLocationOccupied(l72)) {
                    p72 = 100;
                } else {
                    p72 = rc.senseRubble(l72) + 10;
                }
                if (v72 > v84 + p72) {
                    v72 = v84 + p72;
                    d72 = Direction.NORTHWEST;
                }
                if (v72 > v71 + p72) {
                    v72 = v71 + p72;
                    d72 = d71;
                }
                if (v72 > v85 + p72) {
                    v72 = v85 + p72;
                    d72 = d85;
                }
            }
            if (rc.canSenseLocation(l96)) {
                if (rc.isLocationOccupied(l96)) {
                    p96 = 100;
                } else {
                    p96 = rc.senseRubble(l96) + 10;
                }
                if (v96 > v84 + p96) {
                    v96 = v84 + p96;
                    d96 = Direction.SOUTHEAST;
                }
                if (v96 > v97 + p96) {
                    v96 = v97 + p96;
                    d96 = d97;
                }
                if (v96 > v83 + p96) {
                    v96 = v83 + p96;
                    d96 = d83;
                }
            }
            if (rc.canSenseLocation(l98)) {
                if (rc.isLocationOccupied(l98)) {
                    p98 = 100;
                } else {
                    p98 = rc.senseRubble(l98) + 10;
                }
                if (v98 > v84 + p98) {
                    v98 = v84 + p98;
                    d98 = Direction.NORTHEAST;
                }
                if (v98 > v85 + p98) {
                    v98 = v85 + p98;
                    d98 = d85;
                }
                if (v98 > v97 + p98) {
                    v98 = v97 + p98;
                    d98 = d97;
                }
            }
            if (rc.canSenseLocation(l58)) {
                p58 = rc.senseRubble(l58) + 10;
                if (v58 > v71 + p58) {
                    v58 = v71 + p58;
                    d58 = d71;
                }
                if (v58 > v70 + p58) {
                    v58 = v70 + p58;
                    d58 = d70;
                }
                if (v58 > v72 + p58) {
                    v58 = v72 + p58;
                    d58 = d72;
                }
            }
            if (rc.canSenseLocation(l82)) {
                p82 = rc.senseRubble(l82) + 10;
                if (v82 > v83 + p82) {
                    v82 = v83 + p82;
                    d82 = d83;
                }
                if (v82 > v70 + p82) {
                    v82 = v70 + p82;
                    d82 = d70;
                }
                if (v82 > v96 + p82) {
                    v82 = v96 + p82;
                    d82 = d96;
                }
            }
            if (rc.canSenseLocation(l86)) {
                p86 = rc.senseRubble(l86) + 10;
                if (v86 > v85 + p86) {
                    v86 = v85 + p86;
                    d86 = d85;
                }
                if (v86 > v72 + p86) {
                    v86 = v72 + p86;
                    d86 = d72;
                }
                if (v86 > v98 + p86) {
                    v86 = v98 + p86;
                    d86 = d98;
                }
            }
            if (rc.canSenseLocation(l110)) {
                p110 = rc.senseRubble(l110) + 10;
                if (v110 > v97 + p110) {
                    v110 = v97 + p110;
                    d110 = d97;
                }
                if (v110 > v98 + p110) {
                    v110 = v98 + p110;
                    d110 = d98;
                }
                if (v110 > v96 + p110) {
                    v110 = v96 + p110;
                    d110 = d96;
                }
            }
            if (rc.canSenseLocation(l57)) {
                p57 = rc.senseRubble(l57) + 10;
                if (v57 > v71 + p57) {
                    v57 = v71 + p57;
                    d57 = d71;
                }
                if (v57 > v70 + p57) {
                    v57 = v70 + p57;
                    d57 = d70;
                }
                if (v57 > v58 + p57) {
                    v57 = v58 + p57;
                    d57 = d58;
                }
            }
            if (rc.canSenseLocation(l59)) {
                p59 = rc.senseRubble(l59) + 10;
                if (v59 > v71 + p59) {
                    v59 = v71 + p59;
                    d59 = d71;
                }
                if (v59 > v72 + p59) {
                    v59 = v72 + p59;
                    d59 = d72;
                }
                if (v59 > v58 + p59) {
                    v59 = v58 + p59;
                    d59 = d58;
                }
            }
            if (rc.canSenseLocation(l69)) {
                p69 = rc.senseRubble(l69) + 10;
                if (v69 > v83 + p69) {
                    v69 = v83 + p69;
                    d69 = d83;
                }
                if (v69 > v70 + p69) {
                    v69 = v70 + p69;
                    d69 = d70;
                }
                if (v69 > v82 + p69) {
                    v69 = v82 + p69;
                    d69 = d82;
                }
                if (v69 > v57 + p69) {
                    v69 = v57 + p69;
                    d69 = d57;
                }
            }
            if (rc.canSenseLocation(l73)) {
                p73 = rc.senseRubble(l73) + 10;
                if (v73 > v85 + p73) {
                    v73 = v85 + p73;
                    d73 = d85;
                }
                if (v73 > v72 + p73) {
                    v73 = v72 + p73;
                    d73 = d72;
                }
                if (v73 > v86 + p73) {
                    v73 = v86 + p73;
                    d73 = d86;
                }
                if (v73 > v59 + p73) {
                    v73 = v59 + p73;
                    d73 = d59;
                }
            }
            if (rc.canSenseLocation(l95)) {
                p95 = rc.senseRubble(l95) + 10;
                if (v95 > v83 + p95) {
                    v95 = v83 + p95;
                    d95 = d83;
                }
                if (v95 > v96 + p95) {
                    v95 = v96 + p95;
                    d95 = d96;
                }
                if (v95 > v82 + p95) {
                    v95 = v82 + p95;
                    d95 = d82;
                }
            }
            if (rc.canSenseLocation(l99)) {
                p99 = rc.senseRubble(l99) + 10;
                if (v99 > v85 + p99) {
                    v99 = v85 + p99;
                    d99 = d85;
                }
                if (v99 > v98 + p99) {
                    v99 = v98 + p99;
                    d99 = d98;
                }
                if (v99 > v86 + p99) {
                    v99 = v86 + p99;
                    d99 = d86;
                }
            }
            if (rc.canSenseLocation(l109)) {
                p109 = rc.senseRubble(l109) + 10;
                if (v109 > v97 + p109) {
                    v109 = v97 + p109;
                    d109 = d97;
                }
                if (v109 > v96 + p109) {
                    v109 = v96 + p109;
                    d109 = d96;
                }
                if (v109 > v110 + p109) {
                    v109 = v110 + p109;
                    d109 = d110;
                }
                if (v109 > v95 + p109) {
                    v109 = v95 + p109;
                    d109 = d95;
                }
            }
            if (rc.canSenseLocation(l111)) {
                p111 = rc.senseRubble(l111) + 10;
                if (v111 > v97 + p111) {
                    v111 = v97 + p111;
                    d111 = d97;
                }
                if (v111 > v98 + p111) {
                    v111 = v98 + p111;
                    d111 = d98;
                }
                if (v111 > v110 + p111) {
                    v111 = v110 + p111;
                    d111 = d110;
                }
                if (v111 > v99 + p111) {
                    v111 = v99 + p111;
                    d111 = d99;
                }
            }
            if (rc.canSenseLocation(l56)) {
                p56 = rc.senseRubble(l56) + 10;
                if (v56 > v70 + p56) {
                    v56 = v70 + p56;
                    d56 = d70;
                }
                if (v56 > v57 + p56) {
                    v56 = v57 + p56;
                    d56 = d57;
                }
                if (v56 > v69 + p56) {
                    v56 = v69 + p56;
                    d56 = d69;
                }
            }
            if (rc.canSenseLocation(l60)) {
                p60 = rc.senseRubble(l60) + 10;
                if (v60 > v72 + p60) {
                    v60 = v72 + p60;
                    d60 = d72;
                }
                if (v60 > v59 + p60) {
                    v60 = v59 + p60;
                    d60 = d59;
                }
                if (v60 > v73 + p60) {
                    v60 = v73 + p60;
                    d60 = d73;
                }
            }
            if (rc.canSenseLocation(l108)) {
                p108 = rc.senseRubble(l108) + 10;
                if (v108 > v96 + p108) {
                    v108 = v96 + p108;
                    d108 = d96;
                }
                if (v108 > v109 + p108) {
                    v108 = v109 + p108;
                    d108 = d109;
                }
                if (v108 > v95 + p108) {
                    v108 = v95 + p108;
                    d108 = d95;
                }
            }
            if (rc.canSenseLocation(l112)) {
                p112 = rc.senseRubble(l112) + 10;
                if (v112 > v98 + p112) {
                    v112 = v98 + p112;
                    d112 = d98;
                }
                if (v112 > v99 + p112) {
                    v112 = v99 + p112;
                    d112 = d99;
                }
                if (v112 > v111 + p112) {
                    v112 = v111 + p112;
                    d112 = d111;
                }
            }
            if (rc.canSenseLocation(l45)) {
                p45 = rc.senseRubble(l45) + 10;
                if (v45 > v58 + p45) {
                    v45 = v58 + p45;
                    d45 = d58;
                }
                if (v45 > v57 + p45) {
                    v45 = v57 + p45;
                    d45 = d57;
                }
                if (v45 > v59 + p45) {
                    v45 = v59 + p45;
                    d45 = d59;
                }
            }
            if (rc.canSenseLocation(l81)) {
                p81 = rc.senseRubble(l81) + 10;
                if (v81 > v82 + p81) {
                    v81 = v82 + p81;
                    d81 = d82;
                }
                if (v81 > v69 + p81) {
                    v81 = v69 + p81;
                    d81 = d69;
                }
                if (v81 > v95 + p81) {
                    v81 = v95 + p81;
                    d81 = d95;
                }
            }
            if (rc.canSenseLocation(l87)) {
                p87 = rc.senseRubble(l87) + 10;
                if (v87 > v86 + p87) {
                    v87 = v86 + p87;
                    d87 = d86;
                }
                if (v87 > v73 + p87) {
                    v87 = v73 + p87;
                    d87 = d73;
                }
                if (v87 > v99 + p87) {
                    v87 = v99 + p87;
                    d87 = d99;
                }
            }
            if (rc.canSenseLocation(l123)) {
                p123 = rc.senseRubble(l123) + 10;
                if (v123 > v110 + p123) {
                    v123 = v110 + p123;
                    d123 = d110;
                }
                if (v123 > v111 + p123) {
                    v123 = v111 + p123;
                    d123 = d111;
                }
                if (v123 > v109 + p123) {
                    v123 = v109 + p123;
                    d123 = d109;
                }
            }
            if (rc.canSenseLocation(l44)) {
                p44 = rc.senseRubble(l44) + 10;
                if (v44 > v58 + p44) {
                    v44 = v58 + p44;
                    d44 = d58;
                }
                if (v44 > v57 + p44) {
                    v44 = v57 + p44;
                    d44 = d57;
                }
                if (v44 > v56 + p44) {
                    v44 = v56 + p44;
                    d44 = d56;
                }
                if (v44 > v45 + p44) {
                    v44 = v45 + p44;
                    d44 = d45;
                }
            }
            if (rc.canSenseLocation(l46)) {
                p46 = rc.senseRubble(l46) + 10;
                if (v46 > v58 + p46) {
                    v46 = v58 + p46;
                    d46 = d58;
                }
                if (v46 > v59 + p46) {
                    v46 = v59 + p46;
                    d46 = d59;
                }
                if (v46 > v60 + p46) {
                    v46 = v60 + p46;
                    d46 = d60;
                }
                if (v46 > v45 + p46) {
                    v46 = v45 + p46;
                    d46 = d45;
                }
            }
            if (rc.canSenseLocation(l68)) {
                p68 = rc.senseRubble(l68) + 10;
                if (v68 > v82 + p68) {
                    v68 = v82 + p68;
                    d68 = d82;
                }
                if (v68 > v69 + p68) {
                    v68 = v69 + p68;
                    d68 = d69;
                }
                if (v68 > v56 + p68) {
                    v68 = v56 + p68;
                    d68 = d56;
                }
                if (v68 > v81 + p68) {
                    v68 = v81 + p68;
                    d68 = d81;
                }
            }
            if (rc.canSenseLocation(l74)) {
                p74 = rc.senseRubble(l74) + 10;
                if (v74 > v86 + p74) {
                    v74 = v86 + p74;
                    d74 = d86;
                }
                if (v74 > v73 + p74) {
                    v74 = v73 + p74;
                    d74 = d73;
                }
                if (v74 > v60 + p74) {
                    v74 = v60 + p74;
                    d74 = d60;
                }
                if (v74 > v87 + p74) {
                    v74 = v87 + p74;
                    d74 = d87;
                }
            }
            if (rc.canSenseLocation(l94)) {
                p94 = rc.senseRubble(l94) + 10;
                if (v94 > v82 + p94) {
                    v94 = v82 + p94;
                    d94 = d82;
                }
                if (v94 > v95 + p94) {
                    v94 = v95 + p94;
                    d94 = d95;
                }
                if (v94 > v108 + p94) {
                    v94 = v108 + p94;
                    d94 = d108;
                }
                if (v94 > v81 + p94) {
                    v94 = v81 + p94;
                    d94 = d81;
                }
            }
            if (rc.canSenseLocation(l100)) {
                p100 = rc.senseRubble(l100) + 10;
                if (v100 > v86 + p100) {
                    v100 = v86 + p100;
                    d100 = d86;
                }
                if (v100 > v99 + p100) {
                    v100 = v99 + p100;
                    d100 = d99;
                }
                if (v100 > v112 + p100) {
                    v100 = v112 + p100;
                    d100 = d112;
                }
                if (v100 > v87 + p100) {
                    v100 = v87 + p100;
                    d100 = d87;
                }
            }
            if (rc.canSenseLocation(l122)) {
                p122 = rc.senseRubble(l122) + 10;
                if (v122 > v110 + p122) {
                    v122 = v110 + p122;
                    d122 = d110;
                }
                if (v122 > v109 + p122) {
                    v122 = v109 + p122;
                    d122 = d109;
                }
                if (v122 > v108 + p122) {
                    v122 = v108 + p122;
                    d122 = d108;
                }
                if (v122 > v123 + p122) {
                    v122 = v123 + p122;
                    d122 = d123;
                }
            }
            if (rc.canSenseLocation(l124)) {
                p124 = rc.senseRubble(l124) + 10;
                if (v124 > v110 + p124) {
                    v124 = v110 + p124;
                    d124 = d110;
                }
                if (v124 > v111 + p124) {
                    v124 = v111 + p124;
                    d124 = d111;
                }
                if (v124 > v112 + p124) {
                    v124 = v112 + p124;
                    d124 = d112;
                }
                if (v124 > v123 + p124) {
                    v124 = v123 + p124;
                    d124 = d123;
                }
            }
            if (rc.canSenseLocation(l43)) {
                p43 = rc.senseRubble(l43) + 10;
                if (v43 > v57 + p43) {
                    v43 = v57 + p43;
                    d43 = d57;
                }
                if (v43 > v56 + p43) {
                    v43 = v56 + p43;
                    d43 = d56;
                }
                if (v43 > v44 + p43) {
                    v43 = v44 + p43;
                    d43 = d44;
                }
            }
            if (rc.canSenseLocation(l47)) {
                p47 = rc.senseRubble(l47) + 10;
                if (v47 > v59 + p47) {
                    v47 = v59 + p47;
                    d47 = d59;
                }
                if (v47 > v60 + p47) {
                    v47 = v60 + p47;
                    d47 = d60;
                }
                if (v47 > v46 + p47) {
                    v47 = v46 + p47;
                    d47 = d46;
                }
            }
            if (rc.canSenseLocation(l55)) {
                p55 = rc.senseRubble(l55) + 10;
                if (v55 > v69 + p55) {
                    v55 = v69 + p55;
                    d55 = d69;
                }
                if (v55 > v56 + p55) {
                    v55 = v56 + p55;
                    d55 = d56;
                }
                if (v55 > v68 + p55) {
                    v55 = v68 + p55;
                    d55 = d68;
                }
                if (v55 > v43 + p55) {
                    v55 = v43 + p55;
                    d55 = d43;
                }
            }
            if (rc.canSenseLocation(l61)) {
                p61 = rc.senseRubble(l61) + 10;
                if (v61 > v73 + p61) {
                    v61 = v73 + p61;
                    d61 = d73;
                }
                if (v61 > v60 + p61) {
                    v61 = v60 + p61;
                    d61 = d60;
                }
                if (v61 > v74 + p61) {
                    v61 = v74 + p61;
                    d61 = d74;
                }
                if (v61 > v47 + p61) {
                    v61 = v47 + p61;
                    d61 = d47;
                }
            }
            if (rc.canSenseLocation(l107)) {
                p107 = rc.senseRubble(l107) + 10;
                if (v107 > v95 + p107) {
                    v107 = v95 + p107;
                    d107 = d95;
                }
                if (v107 > v108 + p107) {
                    v107 = v108 + p107;
                    d107 = d108;
                }
                if (v107 > v94 + p107) {
                    v107 = v94 + p107;
                    d107 = d94;
                }
            }
            if (rc.canSenseLocation(l113)) {
                p113 = rc.senseRubble(l113) + 10;
                if (v113 > v99 + p113) {
                    v113 = v99 + p113;
                    d113 = d99;
                }
                if (v113 > v112 + p113) {
                    v113 = v112 + p113;
                    d113 = d112;
                }
                if (v113 > v100 + p113) {
                    v113 = v100 + p113;
                    d113 = d100;
                }
            }
            if (rc.canSenseLocation(l121)) {
                p121 = rc.senseRubble(l121) + 10;
                if (v121 > v109 + p121) {
                    v121 = v109 + p121;
                    d121 = d109;
                }
                if (v121 > v108 + p121) {
                    v121 = v108 + p121;
                    d121 = d108;
                }
                if (v121 > v122 + p121) {
                    v121 = v122 + p121;
                    d121 = d122;
                }
                if (v121 > v107 + p121) {
                    v121 = v107 + p121;
                    d121 = d107;
                }
            }
            if (rc.canSenseLocation(l125)) {
                p125 = rc.senseRubble(l125) + 10;
                if (v125 > v111 + p125) {
                    v125 = v111 + p125;
                    d125 = d111;
                }
                if (v125 > v112 + p125) {
                    v125 = v112 + p125;
                    d125 = d112;
                }
                if (v125 > v124 + p125) {
                    v125 = v124 + p125;
                    d125 = d124;
                }
                if (v125 > v113 + p125) {
                    v125 = v113 + p125;
                    d125 = d113;
                }
            }
            if (rc.canSenseLocation(l32)) {
                p32 = rc.senseRubble(l32) + 10;
                if (v32 > v45 + p32) {
                    v32 = v45 + p32;
                    d32 = d45;
                }
                if (v32 > v44 + p32) {
                    v32 = v44 + p32;
                    d32 = d44;
                }
                if (v32 > v46 + p32) {
                    v32 = v46 + p32;
                    d32 = d46;
                }
            }
            if (rc.canSenseLocation(l80)) {
                p80 = rc.senseRubble(l80) + 10;
                if (v80 > v81 + p80) {
                    v80 = v81 + p80;
                    d80 = d81;
                }
                if (v80 > v68 + p80) {
                    v80 = v68 + p80;
                    d80 = d68;
                }
                if (v80 > v94 + p80) {
                    v80 = v94 + p80;
                    d80 = d94;
                }
            }
            if (rc.canSenseLocation(l88)) {
                p88 = rc.senseRubble(l88) + 10;
                if (v88 > v87 + p88) {
                    v88 = v87 + p88;
                    d88 = d87;
                }
                if (v88 > v74 + p88) {
                    v88 = v74 + p88;
                    d88 = d74;
                }
                if (v88 > v100 + p88) {
                    v88 = v100 + p88;
                    d88 = d100;
                }
            }
            if (rc.canSenseLocation(l136)) {
                p136 = rc.senseRubble(l136) + 10;
                if (v136 > v123 + p136) {
                    v136 = v123 + p136;
                    d136 = d123;
                }
                if (v136 > v124 + p136) {
                    v136 = v124 + p136;
                    d136 = d124;
                }
                if (v136 > v122 + p136) {
                    v136 = v122 + p136;
                    d136 = d122;
                }
            }
            if (rc.canSenseLocation(l31)) {
                p31 = rc.senseRubble(l31) + 10;
                if (v31 > v45 + p31) {
                    v31 = v45 + p31;
                    d31 = d45;
                }
                if (v31 > v44 + p31) {
                    v31 = v44 + p31;
                    d31 = d44;
                }
                if (v31 > v43 + p31) {
                    v31 = v43 + p31;
                    d31 = d43;
                }
                if (v31 > v32 + p31) {
                    v31 = v32 + p31;
                    d31 = d32;
                }
            }
            if (rc.canSenseLocation(l33)) {
                p33 = rc.senseRubble(l33) + 10;
                if (v33 > v45 + p33) {
                    v33 = v45 + p33;
                    d33 = d45;
                }
                if (v33 > v46 + p33) {
                    v33 = v46 + p33;
                    d33 = d46;
                }
                if (v33 > v47 + p33) {
                    v33 = v47 + p33;
                    d33 = d47;
                }
                if (v33 > v32 + p33) {
                    v33 = v32 + p33;
                    d33 = d32;
                }
            }
            if (rc.canSenseLocation(l67)) {
                p67 = rc.senseRubble(l67) + 10;
                if (v67 > v81 + p67) {
                    v67 = v81 + p67;
                    d67 = d81;
                }
                if (v67 > v68 + p67) {
                    v67 = v68 + p67;
                    d67 = d68;
                }
                if (v67 > v55 + p67) {
                    v67 = v55 + p67;
                    d67 = d55;
                }
                if (v67 > v80 + p67) {
                    v67 = v80 + p67;
                    d67 = d80;
                }
            }
            if (rc.canSenseLocation(l75)) {
                p75 = rc.senseRubble(l75) + 10;
                if (v75 > v87 + p75) {
                    v75 = v87 + p75;
                    d75 = d87;
                }
                if (v75 > v74 + p75) {
                    v75 = v74 + p75;
                    d75 = d74;
                }
                if (v75 > v61 + p75) {
                    v75 = v61 + p75;
                    d75 = d61;
                }
                if (v75 > v88 + p75) {
                    v75 = v88 + p75;
                    d75 = d88;
                }
            }
            if (rc.canSenseLocation(l93)) {
                p93 = rc.senseRubble(l93) + 10;
                if (v93 > v81 + p93) {
                    v93 = v81 + p93;
                    d93 = d81;
                }
                if (v93 > v94 + p93) {
                    v93 = v94 + p93;
                    d93 = d94;
                }
                if (v93 > v107 + p93) {
                    v93 = v107 + p93;
                    d93 = d107;
                }
                if (v93 > v80 + p93) {
                    v93 = v80 + p93;
                    d93 = d80;
                }
            }
            if (rc.canSenseLocation(l101)) {
                p101 = rc.senseRubble(l101) + 10;
                if (v101 > v87 + p101) {
                    v101 = v87 + p101;
                    d101 = d87;
                }
                if (v101 > v100 + p101) {
                    v101 = v100 + p101;
                    d101 = d100;
                }
                if (v101 > v113 + p101) {
                    v101 = v113 + p101;
                    d101 = d113;
                }
                if (v101 > v88 + p101) {
                    v101 = v88 + p101;
                    d101 = d88;
                }
            }
            if (rc.canSenseLocation(l135)) {
                p135 = rc.senseRubble(l135) + 10;
                if (v135 > v123 + p135) {
                    v135 = v123 + p135;
                    d135 = d123;
                }
                if (v135 > v122 + p135) {
                    v135 = v122 + p135;
                    d135 = d122;
                }
                if (v135 > v121 + p135) {
                    v135 = v121 + p135;
                    d135 = d121;
                }
                if (v135 > v136 + p135) {
                    v135 = v136 + p135;
                    d135 = d136;
                }
            }
            if (rc.canSenseLocation(l137)) {
                p137 = rc.senseRubble(l137) + 10;
                if (v137 > v123 + p137) {
                    v137 = v123 + p137;
                    d137 = d123;
                }
                if (v137 > v124 + p137) {
                    v137 = v124 + p137;
                    d137 = d124;
                }
                if (v137 > v125 + p137) {
                    v137 = v125 + p137;
                    d137 = d125;
                }
                if (v137 > v136 + p137) {
                    v137 = v136 + p137;
                    d137 = d136;
                }
            }
            if (rc.canSenseLocation(l42)) {
                p42 = rc.senseRubble(l42) + 10;
                if (v42 > v56 + p42) {
                    v42 = v56 + p42;
                    d42 = d56;
                }
                if (v42 > v43 + p42) {
                    v42 = v43 + p42;
                    d42 = d43;
                }
                if (v42 > v55 + p42) {
                    v42 = v55 + p42;
                    d42 = d55;
                }
            }
            if (rc.canSenseLocation(l48)) {
                p48 = rc.senseRubble(l48) + 10;
                if (v48 > v60 + p48) {
                    v48 = v60 + p48;
                    d48 = d60;
                }
                if (v48 > v47 + p48) {
                    v48 = v47 + p48;
                    d48 = d47;
                }
                if (v48 > v61 + p48) {
                    v48 = v61 + p48;
                    d48 = d61;
                }
            }
            if (rc.canSenseLocation(l120)) {
                p120 = rc.senseRubble(l120) + 10;
                if (v120 > v108 + p120) {
                    v120 = v108 + p120;
                    d120 = d108;
                }
                if (v120 > v121 + p120) {
                    v120 = v121 + p120;
                    d120 = d121;
                }
                if (v120 > v107 + p120) {
                    v120 = v107 + p120;
                    d120 = d107;
                }
            }
            if (rc.canSenseLocation(l126)) {
                p126 = rc.senseRubble(l126) + 10;
                if (v126 > v112 + p126) {
                    v126 = v112 + p126;
                    d126 = d112;
                }
                if (v126 > v113 + p126) {
                    v126 = v113 + p126;
                    d126 = d113;
                }
                if (v126 > v125 + p126) {
                    v126 = v125 + p126;
                    d126 = d125;
                }
            }
            if (rc.canSenseLocation(l30)) {
                p30 = rc.senseRubble(l30) + 10;
                if (v30 > v44 + p30) {
                    v30 = v44 + p30;
                    d30 = d44;
                }
                if (v30 > v43 + p30) {
                    v30 = v43 + p30;
                    d30 = d43;
                }
                if (v30 > v31 + p30) {
                    v30 = v31 + p30;
                    d30 = d31;
                }
                if (v30 > v42 + p30) {
                    v30 = v42 + p30;
                    d30 = d42;
                }
            }
            if (rc.canSenseLocation(l34)) {
                p34 = rc.senseRubble(l34) + 10;
                if (v34 > v46 + p34) {
                    v34 = v46 + p34;
                    d34 = d46;
                }
                if (v34 > v47 + p34) {
                    v34 = v47 + p34;
                    d34 = d47;
                }
                if (v34 > v33 + p34) {
                    v34 = v33 + p34;
                    d34 = d33;
                }
                if (v34 > v48 + p34) {
                    v34 = v48 + p34;
                    d34 = d48;
                }
            }
            if (rc.canSenseLocation(l54)) {
                p54 = rc.senseRubble(l54) + 10;
                if (v54 > v68 + p54) {
                    v54 = v68 + p54;
                    d54 = d68;
                }
                if (v54 > v55 + p54) {
                    v54 = v55 + p54;
                    d54 = d55;
                }
                if (v54 > v67 + p54) {
                    v54 = v67 + p54;
                    d54 = d67;
                }
                if (v54 > v42 + p54) {
                    v54 = v42 + p54;
                    d54 = d42;
                }
            }
            if (rc.canSenseLocation(l62)) {
                p62 = rc.senseRubble(l62) + 10;
                if (v62 > v74 + p62) {
                    v62 = v74 + p62;
                    d62 = d74;
                }
                if (v62 > v61 + p62) {
                    v62 = v61 + p62;
                    d62 = d61;
                }
                if (v62 > v75 + p62) {
                    v62 = v75 + p62;
                    d62 = d75;
                }
                if (v62 > v48 + p62) {
                    v62 = v48 + p62;
                    d62 = d48;
                }
            }
            if (rc.canSenseLocation(l106)) {
                p106 = rc.senseRubble(l106) + 10;
                if (v106 > v94 + p106) {
                    v106 = v94 + p106;
                    d106 = d94;
                }
                if (v106 > v107 + p106) {
                    v106 = v107 + p106;
                    d106 = d107;
                }
                if (v106 > v93 + p106) {
                    v106 = v93 + p106;
                    d106 = d93;
                }
                if (v106 > v120 + p106) {
                    v106 = v120 + p106;
                    d106 = d120;
                }
            }
            if (rc.canSenseLocation(l114)) {
                p114 = rc.senseRubble(l114) + 10;
                if (v114 > v100 + p114) {
                    v114 = v100 + p114;
                    d114 = d100;
                }
                if (v114 > v113 + p114) {
                    v114 = v113 + p114;
                    d114 = d113;
                }
                if (v114 > v101 + p114) {
                    v114 = v101 + p114;
                    d114 = d101;
                }
                if (v114 > v126 + p114) {
                    v114 = v126 + p114;
                    d114 = d126;
                }
            }
            if (rc.canSenseLocation(l134)) {
                p134 = rc.senseRubble(l134) + 10;
                if (v134 > v122 + p134) {
                    v134 = v122 + p134;
                    d134 = d122;
                }
                if (v134 > v121 + p134) {
                    v134 = v121 + p134;
                    d134 = d121;
                }
                if (v134 > v135 + p134) {
                    v134 = v135 + p134;
                    d134 = d135;
                }
                if (v134 > v120 + p134) {
                    v134 = v120 + p134;
                    d134 = d120;
                }
            }
            if (rc.canSenseLocation(l138)) {
                p138 = rc.senseRubble(l138) + 10;
                if (v138 > v124 + p138) {
                    v138 = v124 + p138;
                    d138 = d124;
                }
                if (v138 > v125 + p138) {
                    v138 = v125 + p138;
                    d138 = d125;
                }
                if (v138 > v137 + p138) {
                    v138 = v137 + p138;
                    d138 = d137;
                }
                if (v138 > v126 + p138) {
                    v138 = v126 + p138;
                    d138 = d126;
                }
            }

            /*
             * We do a big switch to handle when our target tile is within
             * our vision range. If so, we just return the direction
             * to the tile in question.
             * 
             * The switch saves a lot of bytecode.
             */
            int dx = target.x - l84.x;
            int dy = target.y - l84.y;
            switch (dx) {
                case -4:
                    switch (dy) {
                        case -2:
                            return d30;
                        case -1:
                            return d31;
                        case 0:
                            return d32;
                        case 1:
                            return d33;
                        case 2:
                            return d34;
                    }
                    break;
                case -3:
                    switch (dy) {
                        case -3:
                            return d42;
                        case -2:
                            return d43;
                        case -1:
                            return d44;
                        case 0:
                            return d45;
                        case 1:
                            return d46;
                        case 2:
                            return d47;
                        case 3:
                            return d48;
                    }
                    break;
                case -2:
                    switch (dy) {
                        case -4:
                            return d54;
                        case -3:
                            return d55;
                        case -2:
                            return d56;
                        case -1:
                            return d57;
                        case 0:
                            return d58;
                        case 1:
                            return d59;
                        case 2:
                            return d60;
                        case 3:
                            return d61;
                        case 4:
                            return d62;
                    }
                    break;
                case -1:
                    switch (dy) {
                        case -4:
                            return d67;
                        case -3:
                            return d68;
                        case -2:
                            return d69;
                        case -1:
                            return d70;
                        case 0:
                            return d71;
                        case 1:
                            return d72;
                        case 2:
                            return d73;
                        case 3:
                            return d74;
                        case 4:
                            return d75;
                    }
                    break;
                case 0:
                    switch (dy) {
                        case -4:
                            return d80;
                        case -3:
                            return d81;
                        case -2:
                            return d82;
                        case -1:
                            return d83;
                        case 0:
                            return d84;
                        case 1:
                            return d85;
                        case 2:
                            return d86;
                        case 3:
                            return d87;
                        case 4:
                            return d88;
                    }
                    break;
                case 1:
                    switch (dy) {
                        case -4:
                            return d93;
                        case -3:
                            return d94;
                        case -2:
                            return d95;
                        case -1:
                            return d96;
                        case 0:
                            return d97;
                        case 1:
                            return d98;
                        case 2:
                            return d99;
                        case 3:
                            return d100;
                        case 4:
                            return d101;
                    }
                    break;
                case 2:
                    switch (dy) {
                        case -4:
                            return d106;
                        case -3:
                            return d107;
                        case -2:
                            return d108;
                        case -1:
                            return d109;
                        case 0:
                            return d110;
                        case 1:
                            return d111;
                        case 2:
                            return d112;
                        case 3:
                            return d113;
                        case 4:
                            return d114;
                    }
                    break;
                case 3:
                    switch (dy) {
                        case -3:
                            return d120;
                        case -2:
                            return d121;
                        case -1:
                            return d122;
                        case 0:
                            return d123;
                        case 1:
                            return d124;
                        case 2:
                            return d125;
                        case 3:
                            return d126;
                    }
                    break;
                case 4:
                    switch (dy) {
                        case -2:
                            return d134;
                        case -1:
                            return d135;
                        case 0:
                            return d136;
                        case 1:
                            return d137;
                        case 2:
                            return d138;
                    }
                    break;
            }

            /*
             * In the event the tile is outside our vision range, we compare
             * all the tiles on the vision frontier, taking into account total path
             * weights as well as distance to target.
             * 
             * The result of our comparison will be the best path that gets us
             * strictly closer to the target, with best being defined as:
             * 
             * (initialDistanceToTarget - finalDistanceToTarget) / pathWeight
             * 
             */
            Direction ans = Direction.CENTER;
            double bestEstimation = 0;
            double initialDist = Math.sqrt(l84.distanceSquaredTo(target));

            double dist30 = (initialDist - Math.sqrt(l30.distanceSquaredTo(target))) / v30;
            if (dist30 > bestEstimation) {
                bestEstimation = dist30;
                ans = d30;
            }
            double dist31 = (initialDist - Math.sqrt(l31.distanceSquaredTo(target))) / v31;
            if (dist31 > bestEstimation) {
                bestEstimation = dist31;
                ans = d31;
            }
            double dist32 = (initialDist - Math.sqrt(l32.distanceSquaredTo(target))) / v32;
            if (dist32 > bestEstimation) {
                bestEstimation = dist32;
                ans = d32;
            }
            double dist33 = (initialDist - Math.sqrt(l33.distanceSquaredTo(target))) / v33;
            if (dist33 > bestEstimation) {
                bestEstimation = dist33;
                ans = d33;
            }

            double dist34 = (initialDist - Math.sqrt(l34.distanceSquaredTo(target))) / v34;
            if (dist34 > bestEstimation) {
                bestEstimation = dist34;
                ans = d34;
            }
            double dist42 = (initialDist - Math.sqrt(l42.distanceSquaredTo(target))) / v42;
            if (dist42 > bestEstimation) {
                bestEstimation = dist42;
                ans = d42;
            }
            double dist43 = (initialDist - Math.sqrt(l43.distanceSquaredTo(target))) / v43;
            if (dist43 > bestEstimation) {
                bestEstimation = dist43;
                ans = d43;
            }
            double dist47 = (initialDist - Math.sqrt(l47.distanceSquaredTo(target))) / v47;
            if (dist47 > bestEstimation) {
                bestEstimation = dist47;
                ans = d47;
            }
            double dist48 = (initialDist - Math.sqrt(l48.distanceSquaredTo(target))) / v48;
            if (dist48 > bestEstimation) {
                bestEstimation = dist48;
                ans = d48;
            }

            double dist54 = (initialDist - Math.sqrt(l54.distanceSquaredTo(target))) / v54;
            if (dist54 > bestEstimation) {
                bestEstimation = dist54;
                ans = d54;
            }
            double dist55 = (initialDist - Math.sqrt(l55.distanceSquaredTo(target))) / v55;
            if (dist55 > bestEstimation) {
                bestEstimation = dist55;
                ans = d55;
            }
            double dist61 = (initialDist - Math.sqrt(l61.distanceSquaredTo(target))) / v61;
            if (dist61 > bestEstimation) {
                bestEstimation = dist61;
                ans = d61;
            }
            double dist62 = (initialDist - Math.sqrt(l62.distanceSquaredTo(target))) / v62;
            if (dist62 > bestEstimation) {
                bestEstimation = dist62;
                ans = d62;
            }

            double dist67 = (initialDist - Math.sqrt(l67.distanceSquaredTo(target))) / v67;
            if (dist67 > bestEstimation) {
                bestEstimation = dist67;
                ans = d67;
            }
            double dist75 = (initialDist - Math.sqrt(l75.distanceSquaredTo(target))) / v75;
            if (dist75 > bestEstimation) {
                bestEstimation = dist75;
                ans = d75;
            }
            double dist80 = (initialDist - Math.sqrt(l80.distanceSquaredTo(target))) / v80;
            if (dist80 > bestEstimation) {
                bestEstimation = dist80;
                ans = d80;
            }
            double dist88 = (initialDist - Math.sqrt(l88.distanceSquaredTo(target))) / v88;
            if (dist88 > bestEstimation) {
                bestEstimation = dist88;
                ans = d88;
            }

            double dist93 = (initialDist - Math.sqrt(l93.distanceSquaredTo(target))) / v93;
            if (dist93 > bestEstimation) {
                bestEstimation = dist93;
                ans = d93;
            }
            double dist101 = (initialDist - Math.sqrt(l101.distanceSquaredTo(target))) / v101;
            if (dist101 > bestEstimation) {
                bestEstimation = dist101;
                ans = d101;
            }
            double dist106 = (initialDist - Math.sqrt(l106.distanceSquaredTo(target))) / v106;
            if (dist106 > bestEstimation) {
                bestEstimation = dist106;
                ans = d106;
            }
            double dist107 = (initialDist - Math.sqrt(l107.distanceSquaredTo(target))) / v107;
            if (dist107 > bestEstimation) {
                bestEstimation = dist107;
                ans = d107;
            }

            double dist113 = (initialDist - Math.sqrt(l113.distanceSquaredTo(target))) / v113;
            if (dist113 > bestEstimation) {
                bestEstimation = dist113;
                ans = d113;
            }
            double dist114 = (initialDist - Math.sqrt(l114.distanceSquaredTo(target))) / v114;
            if (dist114 > bestEstimation) {
                bestEstimation = dist114;
                ans = d114;
            }
            double dist120 = (initialDist - Math.sqrt(l120.distanceSquaredTo(target))) / v120;
            if (dist120 > bestEstimation) {
                bestEstimation = dist120;
                ans = d120;
            }
            double dist121 = (initialDist - Math.sqrt(l121.distanceSquaredTo(target))) / v121;
            if (dist121 > bestEstimation) {
                bestEstimation = dist121;
                ans = d121;
            }

            double dist125 = (initialDist - Math.sqrt(l125.distanceSquaredTo(target))) / v125;
            if (dist125 > bestEstimation) {
                bestEstimation = dist125;
                ans = d125;
            }
            double dist126 = (initialDist - Math.sqrt(l126.distanceSquaredTo(target))) / v126;
            if (dist126 > bestEstimation) {
                bestEstimation = dist126;
                ans = d126;
            }
            double dist134 = (initialDist - Math.sqrt(l134.distanceSquaredTo(target))) / v134;
            if (dist134 > bestEstimation) {
                bestEstimation = dist134;
                ans = d134;
            }
            double dist135 = (initialDist - Math.sqrt(l135.distanceSquaredTo(target))) / v135;
            if (dist135 > bestEstimation) {
                bestEstimation = dist135;
                ans = d135;
            }
            double dist136 = (initialDist - Math.sqrt(l136.distanceSquaredTo(target))) / v136;
            if (dist136 > bestEstimation) {
                bestEstimation = dist136;
                ans = d136;
            }

            double dist137 = (initialDist - Math.sqrt(l137.distanceSquaredTo(target))) / v137;
            if (dist137 > bestEstimation) {
                bestEstimation = dist137;
                ans = d137;
            }
            double dist138 = (initialDist - Math.sqrt(l138.distanceSquaredTo(target))) / v138;
            if (dist138 > bestEstimation) {
                bestEstimation = dist138;
                ans = d138;
            }

            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}