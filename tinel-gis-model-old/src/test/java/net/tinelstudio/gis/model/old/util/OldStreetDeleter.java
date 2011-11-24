/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.util;

import net.tinelstudio.gis.model.old.SpringContexts;
import net.tinelstudio.gis.model.old.dao.OldStreetDao;
import net.tinelstudio.gis.model.old.domain.Eazvceste_fixed;
import net.tinelstudio.gis.model.old.domain.Ebzsceste2_fixed;
import net.tinelstudio.gis.model.old.domain.Eczmceste_fixed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * A utility to manually delete streets from DB.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={SpringContexts.OLD_DB_CONTEXT})
@TransactionConfiguration(transactionManager="oldTxManager")
@Transactional
public class OldStreetDeleter {

  private final long[] EAZVCESTE={2319, 2483, 1530};

  private final long[] EBZSCESTE2={3162, 3167, 8254, 8255, 8256, 8257, 3021,
    2961, 2947, 3588, 2818, 478, 629, 4312, 7509, 4747, 5392, 2273, 6312, 6492,
    3594, 3660, 3659, 3899, 4748, 1012, 5393, 5394, 5619, 5618};

  private final long[] ECZMCESTE={22449, 29027, 310, 35378, 22585, 37516,
    15300, 26998, 37517, 27840, 37518, 37514, 37515, 37519, 37520, 37521,
    37522, 37523, 37524, 37525, 37526, 37527, 37528, 37529, 21521, 37553,
    37554, 37555, 37556, 37557, 37511, 37500, 37501, 37502, 37503, 37504,
    37505, 37506, 37507, 37508, 37509, 37510, 37512, 37513, 21520, 35928,
    35929, 35930, 23174, 22462, 22463, 22464, 22465, 22455, 22456, 22457,
    22458, 22459, 22460, 22461, 22466, 22467, 22468, 279, 23870, 24872, 19458,
    19391, 22544, 22545, 22543, 22546, 22547, 22548, 22549, 22550, 22551,
    22552, 22553, 22554, 22555, 22556, 22557, 22558, 22559, 22560, 22561,
    22562, 22563, 22506, 22507, 22508, 22509, 22510, 22511, 22512, 22513,
    22514, 22515, 22516, 22517, 22518, 22519, 22520, 22521, 22522, 22523,
    22524, 22525, 22526, 22527, 22528, 22529, 22530, 22531, 22532, 22533,
    22534, 29085, 29097, 22813, 22814, 33080, 15347, 22808, 22809, 22810,
    22811, 22812, 22815, 22816, 22817, 22818, 22819, 22820, 22821, 22822,
    22823, 22824, 22500, 22499, 22498, 22497, 22496, 22495, 22494, 22493,
    22492, 22491, 22490, 22489, 22488, 22487, 22486, 22485, 22484, 22483,
    22482, 22481, 22480, 22479, 30701, 30702, 30703, 30704, 30705, 30706,
    30707, 30708, 30709, 30710, 30696, 30697, 30698, 30699, 30700, 31708,
    31709, 31710, 22825, 30809, 30808, 30807, 30806, 30131, 30130, 30129,
    30128, 30127, 30126, 30125, 29562, 29561, 22471, 22472, 22473, 22565,
    22566, 22567, 22568, 22569, 22586, 22587, 22588, 22589, 22590, 22591,
    22592, 22593, 22594, 22595, 22596, 22597, 22598, 22599, 22600, 22604,
    22605, 315, 26994, 26995, 26996, 26997, 6987, 22894, 25738, 25739, 25740,
    25741, 25742, 25743, 25744, 25745, 25746, 25747, 25748, 25749, 25750,
    25751, 25752, 25753, 25754, 25755, 25756, 25757, 25758, 25759, 25760,
    25761, 25762, 25763, 25764, 25765, 25766, 25767, 25768, 25769, 2259, 23566,
    23564, 23563, 23562, 23560, 23873, 23874, 23875, 23907, 23908, 23910,
    23911, 23912, 23913, 23914, 23915, 23916, 23917, 23918, 23919, 23920,
    23921, 23922, 23923, 23924, 23925, 23926, 23927, 23928, 23929, 23930,
    23931, 23932, 23933, 23934, 23936, 2925, 25731, 25732, 25733, 25734, 5181,
    25770, 25771, 25772, 5183, 26626, 27152};

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private OldStreetDao oldStreetDao;

  @Rollback
  // @Rollback(false)
  @Test
  public void deleteEazvceste() {
    logger.info("Deleting EAZVCESTE...");
    for (long id : EAZVCESTE) {
      boolean deleted=oldStreetDao.delete(Eazvceste_fixed.class, id);
      logger.info("Street id="+id+" "+(deleted?"deleted":"not found"));
    }
  }

  @Rollback
  // @Rollback(false)
  @Test
  public void deleteEbzsceste2() {
    logger.info("Deleting EBZSCESTE2...");
    for (long id : EBZSCESTE2) {
      boolean deleted=oldStreetDao.delete(Ebzsceste2_fixed.class, id);
      logger.info("Street id="+id+" "+(deleted?"deleted":"not found"));
    }
  }

  @Rollback
  // @Rollback(false)
  @Test
  public void deleteEczmceste() {
    logger.info("Deleting ECZMCESTE...");
    for (long id : ECZMCESTE) {
      boolean deleted=oldStreetDao.delete(Eczmceste_fixed.class, id);
      logger.info("Street id="+id+" "+(deleted?"deleted":"not found"));
    }
  }
}