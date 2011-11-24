
package net.tinelstudio.gis.model.load;

import java.util.Collection;
import java.util.List;

import net.tinelstudio.gis.model.dao.GeoNameDao;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

/**
 * @author TineL
 */
public class MockGeoNameDao implements GeoNameDao {

  @Override
  public boolean delete(Long id) {
    return false;
  }

  @Override
  public void deleteAll() {}

  @Override
  public GeoName load(Long id) {
    return null;
  }

  @Override
  public GeoName load(String name, Type type) {
    return null;
  }

  @Override
  public List<GeoName> loadAll() {
    return null;
  }

  @Override
  public void save(GeoName element) {}

  @Override
  public void saveAll(Collection<GeoName> elements) {}
}
