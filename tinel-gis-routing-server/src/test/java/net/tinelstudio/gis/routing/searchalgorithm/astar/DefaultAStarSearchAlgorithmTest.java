
package net.tinelstudio.gis.routing.searchalgorithm.astar;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;
import net.tinelstudio.gis.model.dao.FindingDao;

import org.junit.Test;

/**
 * @author TineL
 */
public class DefaultAStarSearchAlgorithmTest extends
  AbstractAStarSearchAlgorithmTest {

  @Override
  protected DefaultAStarSearchAlgorithm createInstance() {
    return new DefaultAStarSearchAlgorithm();
  }

  @Test
  public void testSetFindingDao() {
    FindingDao findingDao=createMock(FindingDao.class);
    DefaultAStarSearchAlgorithm aStar=createInstance();
    aStar.setFindingDao(findingDao);
    assertSame(findingDao, aStar.getFindingDao());
  }
}
