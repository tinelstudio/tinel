
package net.tinelstudio.gis.model.load;

import java.util.Collection;
import java.util.List;

import net.tinelstudio.gis.model.dao.StreetNodeDao;
import net.tinelstudio.gis.model.domain.StreetNode;

/**
 * @author TineL
 */
public class MockStreetNodeDao implements StreetNodeDao {

  @Override
  public boolean delete(Long id) {
    return false;
  }

  @Override
  public void deleteAll() {}

  @Override
  public StreetNode load(Long id) {
    return null;
  }

  @Override
  public List<StreetNode> loadAll() {
    return null;
  }

  @Override
  public void save(StreetNode element) {}

  @Override
  public void saveAll(Collection<StreetNode> elements) {}
}
