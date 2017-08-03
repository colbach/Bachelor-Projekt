package model.pathfinder;

import java.util.ArrayList;
import java.util.HashSet;
import model.*;
import utils.structures.Tuple;

public class PathFinder {

    public static boolean pathExists(Node start, FindTest tester, PathDirection direction) {
        return pathExistsRecursive(start, tester, direction, new HashSet<>()) == RecursivePathFinderControl.FOUND;
    }

    private static RecursivePathFinderControl pathExistsRecursive(Node node, FindTest tester, PathDirection direction, HashSet<Node> metNodes) {
        if (metNodes.contains(node)) {
            return RecursivePathFinderControl.ALREADY_VISITED;
        } else {
            metNodes.add(node);
            TestResult result = tester.test(node);
            switch (result) {
                case ABORD:
                    return RecursivePathFinderControl.ABORD;
                case FOUND:
                    return RecursivePathFinderControl.FOUND;
                case CONTINUE:
                    if (direction == PathDirection.FORWARD) {
                        for (Outlet outlet : node.getOutlets()) {
                            for (Inlet inlet : outlet.getConnectedLets()) {
                                RecursivePathFinderControl returns = pathExistsRecursive(inlet.getNode(), tester, direction, metNodes);
                                if (returns == RecursivePathFinderControl.ABORD) {
                                    return RecursivePathFinderControl.ABORD;
                                } else if (returns == RecursivePathFinderControl.ERROR) {
                                    return RecursivePathFinderControl.ERROR;
                                } else if (returns == RecursivePathFinderControl.FOUND) {
                                    return RecursivePathFinderControl.FOUND;
                                }
                            }
                        }
                        return RecursivePathFinderControl.DEAD_END;
                    } else {
                        for (Inlet inlet : node.getInlets()) {
                            for (Outlet outlet : inlet.getConnectedLets()) {
                                RecursivePathFinderControl returns = pathExistsRecursive(outlet.getNode(), tester, direction, metNodes);
                                if (returns == RecursivePathFinderControl.ABORD) {
                                    return RecursivePathFinderControl.ABORD;
                                } else if (returns == RecursivePathFinderControl.ERROR) {
                                    return RecursivePathFinderControl.ERROR;
                                } else if (returns == RecursivePathFinderControl.FOUND) {
                                    return RecursivePathFinderControl.FOUND;
                                }
                            }
                        }
                        return RecursivePathFinderControl.DEAD_END;
                    }
            }
        }
        return null;
    }

    public static Node findNodeRecursive(Node start, FindTest tester, PathDirection direction) {
        Tuple<Node, RecursivePathFinderControl> findNodeRecursive = findNodeRecursive(start, tester, direction, new HashSet<>());
        if (findNodeRecursive.r == RecursivePathFinderControl.FOUND) {
            return findNodeRecursive.l;
        } else {
            return null;
        }
    }

    public static Tuple<Node, RecursivePathFinderControl> findNodeRecursive(Node node, FindTest tester, PathDirection direction, HashSet<Node> metNodes) {
        if (metNodes.contains(node)) {
            return new Tuple<>(null, RecursivePathFinderControl.ALREADY_VISITED);
        } else {
            metNodes.add(node);
            TestResult result;
            if (tester != null) {
                result = tester.test(node);
            } else {
                result = TestResult.CONTINUE;
            }
            switch (result) {
                case ABORD:
                    return new Tuple<>(null, RecursivePathFinderControl.ABORD);
                case FOUND:
                    return new Tuple<>(node, RecursivePathFinderControl.FOUND);
                case CONTINUE:
                    if (direction == PathDirection.FORWARD) {
                        for (Outlet outlet : node.getOutlets()) {
                            for (Inlet inlet : outlet.getConnectedLets()) {
                                Tuple<Node, RecursivePathFinderControl> returns = findNodeRecursive(inlet.getNode(), tester, direction, metNodes);
                                if (null != returns.r) {
                                    switch (returns.r) {
                                        case ABORD:
                                            return returns;
                                        case ERROR:
                                            return returns;
                                        case FOUND:
                                            return returns;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                        return new Tuple<>(null, RecursivePathFinderControl.DEAD_END);
                    } else {
                        for (Inlet inlet : node.getInlets()) {
                            for (Outlet outlet : inlet.getConnectedLets()) {
                                Tuple<Node, RecursivePathFinderControl> returns = findNodeRecursive(outlet.getNode(), tester, direction, metNodes);
                                if (null != returns.r) {
                                    switch (returns.r) {
                                        case ABORD:
                                            return returns;
                                        case ERROR:
                                            return returns;
                                        case FOUND:
                                            return returns;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                        return new Tuple<>(null, RecursivePathFinderControl.DEAD_END);
                    }
            }
        }
        return null;
    }

}
