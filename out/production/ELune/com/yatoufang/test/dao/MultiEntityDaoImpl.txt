package com.yatoufang.test.dao;

import cn.daxiang.framework.database.BaseJdbcTemplate;
import cn.daxiang.framework.database.DBQueue;
import cn.daxiang.framework.database.EntityInfo;
import cn.daxiang.framework.database.MultiEntity;
import cn.daxiang.framework.database.cache.DataCache;
import cn.daxiang.framework.database.cache.GoogleCacheImpl;
import cn.daxiang.framework.identity.IdentiyKey;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多主键数据处理类
 * Muti
 * multi
 *
 * @author CharonWang
 */
public abstract class MultiEntityDaoImpl extends CacheLoader<Object, Object> implements BaseDao {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    protected BaseJdbcTemplate jdbc;
    @Autowired
    protected DBQueue dbQueue;
    protected DataCache dataCache;
    @Autowired(required = false)
    @Qualifier("cache.time")
    private Integer cacheTime = 900;
    @Autowired(required = false)
    @Qualifier("cache.size")
    private Long cacheSize = 100000L;

    @Override
    public void initialize() {
        dataCache = new GoogleCacheImpl(this, cacheTime, cacheSize);
        initMaxId();
    }

    /**
     * 获取实体
     *
     * @param actorId
     * @param clz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <FK, T extends MultiEntity<FK>> Map<IdentiyKey, T> getByFk(FK fk) {
        Object r = dataCache.getFromCache(fk);
        return (Map<IdentiyKey, T>) r;
    }

    /**
     * 缓存中是否存在
     *
     * @param fk
     * @return
     */
    public <FK> boolean exits(FK fk) {
        return dataCache.exsit(fk);
    }

    public void updateQueue(MultiEntity<?> entity) {
        set2Cache(entity);
        dbQueue.updateQueue(entity);
    }

    @SuppressWarnings("unchecked")
    private void set2Cache(MultiEntity<?> entity) {
        Map<Object, MultiEntity<?>> map = (Map<Object, MultiEntity<?>>) dataCache.getFromCache(entity.findFkId());
        map.put(entity.findPkId(), entity);
    }

    public DataCache getDataCache() {
        return dataCache;
    }

    @Override
    public Object load(Object key) throws Exception {
        List<MultiEntity<?>> entitys = loadFromDBWithFK(key);
        Map<Object, MultiEntity<?>> map = new ConcurrentHashMap<>();
        for (MultiEntity<?> entity : entitys) {
            if (entity.findFkId() == null) {
                throw new RuntimeException("FK is null!");
            }
            map.put(entity.findPkId(), entity);

        }
        // LOGGER.debug("load from db, key:{}, size:{}", key.toString(), entitys.size());
        return map;
    }

    /**
     * 从数据库加载
     *
     * @param condition
     * @param clz
     * @return
     */
    protected List<MultiEntity<?>> loadFromDBWithFK(Object key) {
        LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
        Class<? extends MultiEntity<?>> clz = forClass();
        EntityInfo info = jdbc.getEntityInfo(clz);
        if (info.fkName == null) {
            throw new RuntimeException("fk not exsit!");
        }
        condition.put(info.fkName, key);
        return loadFromDBWithFK(condition, forClass());
    }

    /**
     * 从数据库加载
     *
     * @param condition
     * @param clz
     * @return
     */
    protected <T extends MultiEntity<?>> List<MultiEntity<?>> loadFromDBWithFK(LinkedHashMap<String, Object> condition, Class<T> clz) {
        List<T> list = jdbc.getList(clz, condition);
        List<MultiEntity<?>> result = Lists.newArrayList();
        result.addAll(list);
        return result;
    }

    /**
     * 删除实体
     *
     * @param entity
     */
    @SuppressWarnings("unchecked")
    public void delete(MultiEntity<?> entity) {
        Map<Object, MultiEntity<?>> map = (Map<Object, MultiEntity<?>>) dataCache.getFromCache(entity.findFkId());
        map.remove(entity.findPkId());
        dbQueue.deleteQueue(entity);
    }

    /**
     * 获取一个实体
     *
     * @param fk
     * @param pk
     * @return
     */
    @SuppressWarnings("unchecked")
    public <FK, T extends MultiEntity<FK>> T getMultiEnity(FK fk, IdentiyKey pk) {
        Map<IdentiyKey, MultiEntity<FK>> map = this.getByFk(fk);
        return (T) map.get(pk);
    }

    /**
     * 对应class类
     *
     * @return
     */
    protected abstract Class<? extends MultiEntity<?>> forClass();

    /**
     * 初始化maxId
     *
     * @return
     */
    protected abstract void initMaxId();

}
