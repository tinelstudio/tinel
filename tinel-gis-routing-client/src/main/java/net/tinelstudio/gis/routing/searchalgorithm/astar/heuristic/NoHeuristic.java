/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

/**
 * The heuristic function h(n) = 0.<br>
 * At this condition, only g(n) plays a role, and A* turns into Dijkstra’s
 * algorithm, which is guaranteed to find the cheapest path.
 * 
 * @author TineL
 * @see Heuristic
 * @see <a
 *      href="http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html">A’s
 *      Use of the Heuristic</a>
 */
public class NoHeuristic extends AbstractHeuristic {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=467181564877450567L;

  /**
   * Constructs a heuristic with weight = 0.0.
   */
  public NoHeuristic() {
    setWeight(0.0);
  }
}
