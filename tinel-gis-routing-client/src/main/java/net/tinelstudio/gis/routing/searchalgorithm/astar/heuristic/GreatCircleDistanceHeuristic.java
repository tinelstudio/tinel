/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

/**
 * The heuristic function h(n) that is defined with the great-circle distance of
 * the node n to the goal node and applied weight.<br>
 * <br>
 * <i>The next is copied from <a
 * href="http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html">A*’s
 * Use of the Heuristic</a>:</i><br>
 * The heuristic can be used to control A*’s behavior.
 * <ul>
 * <li>At one extreme, if h(n) is 0, then only g(n) plays a role, and A* turns
 * into Dijkstra’s algorithm, which is guaranteed to find a shortest path.</li>
 * <li>If h(n) is always lower than (or equal to) the cost of moving from n to
 * the goal, then A* is guaranteed to find a shortest path. The lower h(n) is,
 * the more node A* expands, making it slower.</li>
 * <li>If h(n) is exactly equal to the cost of moving from n to the goal, then
 * A* will only follow the best path and never expand anything else, making it
 * very fast. Although you can’t make this happen in all cases, you can make it
 * exact in some special cases. It’s nice to know that given perfect
 * information, A* will behave perfectly.</li>
 * <li>If h(n) is sometimes greater than the cost of moving from n to the goal,
 * then A* is not guaranteed to find a shortest path, but it can run faster.</li>
 * <li>At the other extreme, if h(n) is very high relative to g(n), then only
 * h(n) plays a role, and A* turns into BFS.</li>
 * </ul>
 * 
 * @author TineL
 * @see Heuristic
 * @see <a
 *      href="http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html">A’s
 *      Use of the Heuristic</a>
 */
public class GreatCircleDistanceHeuristic extends AbstractHeuristic {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-3671466241585088158L;

  /**
   * Constructs a heuristic with weight = 1.0.
   * 
   * @see #getWeight()
   */
  public GreatCircleDistanceHeuristic() {}

  /**
   * Constructs a heuristic with the specified weight.
   * 
   * @param weight the heuristic weight
   * @see #getWeight()
   */
  public GreatCircleDistanceHeuristic(double weight) {
    setWeight(weight);
  }
}
