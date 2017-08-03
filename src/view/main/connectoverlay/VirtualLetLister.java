package view.main.connectoverlay;

import java.util.ArrayList;
import model.*;
import utils.structures.*;

/**
 * Dies ist eine Hilfsklasse die dem ConnectOverlayDrafter dabei helfen soll
 * Lets richtig darzustellen.
 */
public class VirtualLetLister {

    public final static String PURE_VIRTUAL_LET_LABEL_COPY = "copy";
    public final static String PURE_VIRTUAL_LET_LABEL_APPEND = "append";

    public static int getConnectedVirtualLetCount(InOutlet[] lets) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    virtualLetCount += let.getConnectedLetsCount();
                } else {
                    virtualLetCount++;
                }
            } else { // Fall: Nicht verbundenes Let
                // Mach nichts
            }
        }
        return virtualLetCount;
    }

    /**
     * Zaehlt Lets die angezeigt werden sollen (evt mehr als real vorhanden)
     */
    public static int getVirtualLetCount(InOutlet[] lets) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    virtualLetCount += let.getConnectedLetsCount() + 1;
                } else {
                    virtualLetCount++;
                }
            } else { // Fall: Nicht verbundenes Let
                virtualLetCount++; // 1x angezeigen
            }
        }
        return virtualLetCount;
    }

    /**
     * Gibt virtuellen Index fuer InOutlet zurueck. let MUSS in lets enthalten
     * sein!
     */
    public static int getVirtualLetIndexFor(InOutlet[] lets, InOutlet let, int index) {
        int virtualLetCount = 0;
        for (InOutlet letOfLets : lets) {
            if (letOfLets == let) {
                return virtualLetCount + index;
            } else {
                if (letOfLets.canConnectToMultipleLets()) {
                    virtualLetCount += letOfLets.getConnectedLetsCount() + 1;
                } else {
                    virtualLetCount++;
                }
            }
        }
        throw new IllegalArgumentException("let muss Element von lets sein!");
    }

    public static String getNameForConnectedVirtualLet(InOutlet[] lets, int virtualIndex) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    if (virtualIndex == virtualLetCount) { // Erster von Let das zu mehreren Lets verbinden kann
                        return let.toString();
                    }
                    virtualLetCount += let.getConnectedLetsCount();
                    if (virtualIndex < virtualLetCount) { // Rein virtueller Let
                        if (lets[0] instanceof Outlet) {
                            return PURE_VIRTUAL_LET_LABEL_COPY;
                        } else {
                            return PURE_VIRTUAL_LET_LABEL_APPEND;
                        }
                    }
                } else {
                    if (virtualIndex == virtualLetCount) { // Let das nur zu einem Let verbinden kann
                        return let.toString();
                    }
                    virtualLetCount++;
                }
            } else { // Fall: Nicht verbundenes Let
                // Mach nichts
            }
        }
        throw new IllegalArgumentException("virtualIndex (" + virtualIndex + ") muss kleiner VirtualLetCount (" + getVirtualLetCount(lets) + ")");
    }

    public static String getNameForVirtualLet(InOutlet[] lets, int virtualIndex) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    if (virtualIndex == virtualLetCount) { // Erster von Let das zu mehreren Lets verbinden kann
                        return let.toString();
                    }
                    virtualLetCount += let.getConnectedLetsCount() + 1;
                    if (virtualIndex < virtualLetCount) { // Rein virtueller Let
                        if (lets[0] instanceof Outlet) {
                            return PURE_VIRTUAL_LET_LABEL_COPY;
                        } else {
                            return PURE_VIRTUAL_LET_LABEL_APPEND;
                        }
                    }
                } else {
                    if (virtualIndex == virtualLetCount) { // Let das nur zu einem Let verbinden kann
                        return let.toString();
                    }
                    virtualLetCount++;
                }
            } else { // Nicht verbundenes Let wird immer nur 1x angezeigt
                if (virtualIndex == virtualLetCount) {
                    return let.toString();
                }
                virtualLetCount++;
            }
        }
        throw new IllegalArgumentException("virtualIndex (" + virtualIndex + ") muss kleiner VirtualLetCount (" + getVirtualLetCount(lets) + ")");
    }

    public static Tuple<InOutlet, Integer> getCorrespondingLetForConnectedVirtualLet(InOutlet[] lets, int virtualIndex) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    for (int i = 0; i < let.getConnectedLetsCount(); i++) {
                        if (virtualIndex == virtualLetCount) { // Virtueller Let
                            return new Tuple<>(let, i);
                        }
                        virtualLetCount++;
                    }
                } else {
                    if (virtualIndex == virtualLetCount) { // Let das nur zu einem Let verbinden kann
                        return new Tuple<>(let, 0);
                    }
                    virtualLetCount++;
                }
            } else { // Fall: Nicht verbundenes Let
                // Mach nichts
            }
        }
        throw new IllegalArgumentException("virtualIndex (" + virtualIndex + ") muss kleiner-gleich VirtualLetCount (" + getVirtualLetCount(lets) + ")");
    }

    public static Tuple<InOutlet, Integer> getCorrespondingLetForVirtualLet(InOutlet[] lets, int virtualIndex) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    for (int i = 0; i < let.getConnectedLetsCount() + 1; i++) {
                        if (virtualIndex == virtualLetCount) { // Virtueller Let
                            return new Tuple<>(let, i);
                        }
                        virtualLetCount++;
                    }
                } else {
                    if (virtualIndex == virtualLetCount) { // Let das nur zu einem Let verbinden kann
                        return new Tuple<>(let, 0);
                    }
                    virtualLetCount++;
                }
            } else { // Nicht verbundenes Let wird immer nur 1x angezeigt
                if (virtualIndex == virtualLetCount) {
                    return new Tuple<>(let, 0);
                }
                virtualLetCount++;
            }
        }
        throw new IllegalArgumentException("virtualIndex (" + virtualIndex + ") muss kleiner-gleich VirtualLetCount (" + getVirtualLetCount(lets) + ")");
    }

    public static boolean isVirtualLetConnected(InOutlet[] lets, int virtualIndex) {
        int virtualLetCount = 0;
        for (InOutlet let : lets) {
            if (let.isConnected()) {
                if (let.canConnectToMultipleLets()) {
                    for (int i = 0; i < let.getConnectedLetsCount() + 1; i++) {
                        if (virtualIndex == virtualLetCount) { // Virtueller Let
                            return i < let.getConnectedLetsCount();
                        }
                        virtualLetCount++;
                    }
                } else {
                    if (virtualIndex == virtualLetCount) { // Let das nur zu einem Let verbinden kann
                        return true;
                    }
                    virtualLetCount++;
                }
            } else { // Nicht verbundenes Let wird immer nur 1x angezeigt
                if (virtualIndex == virtualLetCount) {
                    return false;
                }
                virtualLetCount++;
            }
        }
        throw new IllegalArgumentException("virtualIndex (" + virtualIndex + ") muss kleiner-gleich VirtualLetCount (" + getVirtualLetCount(lets) + ")");
    }
}
