package com.yatoufang.test.dao;

import cn.daxiang.framework.database.BaseJdbcTemplate;
import cn.daxiang.framework.database.DBQueue;
import cn.daxiang.framework.database.SingleEntity;
import cn.daxiang.framework.database.cache.DataCache;
import cn.daxiang.framework.database.cache.GoogleCacheImpl;
import cn.daxiang.framework.identity.IdentiyKey;
import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.List;

/**
 * 单主键数据处理类
 *
 * @author CharonWang
 */
public abstract class SingleEntityDaoImpl extends CacheLoader<Object, Object> implements BaseDao {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
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
     * 从缓存获取实体
     *
     * @param actorId
     * @param clz
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends SingleEntity<?>> T getFromCache(final Object key) {
        return (T) dataCache.getFromCache(key);
    }

    /**
     * 获取实体
     *
     * @param actorId
     * @param clz
     * @return
     */
    public <T extends SingleEntity<?>> T get(IdentiyKey key) {
        return getFromCache(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends SingleEntity<?>> Collection<T> findAll() {
        return (Collection<T>) dataCache.all();
    }

    public void updateQueue(SingleEntity<?> entity) {
        set2Cache(entity);
        dbQueue.updateQueue(entity);
    }

    public void delete(SingleEntity<?> entity) {
        cleanCache(entity);
        dbQueue.deleteQueue(entity);
    }

    public void cleanCache(SingleEntity<?> entity) {
        List<IdentiyKey> keys = entity.keyLists();
        for (Object tmp : keys) {
            dataCache.cleanCache(tmp);
        }
    }

    public void set2Cache(SingleEntity<?> entity) {
        List<IdentiyKey> keys = entity.keyLists();
        for (Object tmp : keys) {
            dataCache.setToCache(tmp, entity);
        }
    }

    public DataCache getDataCache() {
        return dataCache;
    }

    @Override
    public Object load(Object key) throws Exception {
        IdentiyKey pk = (IdentiyKey) key;
        SingleEntity<?> entity = loadFromDB(pk);
        if (entity == null) {
            entity = newInstance(pk, forClass());
        }
        List<IdentiyKey> keys = entity.keyLists();
        for (Object tmp : keys) {
            if (!key.equals(tmp)) {
                dataCache.setToCache(tmp, entity);
            }
        }
        return entity;
    }

    /**
     * 从数据库加载
     *
     * @param condition
     * @param clz
     * @return
     */
    protected SingleEntity<?> loadFromDB(IdentiyKey key) {
        SingleEntity<?> entity = jdbc.get(forClass(), key);
        return entity;
    }

    protected <T extends SingleEntity<?>> T newInstance(IdentiyKey actorId, Class<T> clz) {
        try {
            T entity = clz.newInstance();
            // LOGGER.debug(String.format("entity create
            // new,class:[%s]",clz.getName()));
            entity.setPkId(actorId);
            entity.setNewEntity(true);
            return entity;
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
        return null;
    }

    /**
     * 对应class类
     *
     * @return
     */
    protected abstract Class<? extends SingleEntity<?>> forClass();

    /**
     * 初始化maxId
     *
     * @return
     */
    protected abstract void initMaxId();

    /**
     * 判断实体是否存在
     *
     * @param key
     * @param clz
     * @return
     */
    public boolean exsit(IdentiyKey key, Class<? extends SingleEntity<?>> clz) {
        if (dataCache.exsit(key)) {
            return true;
        }
        SingleEntity<?> result = jdbc.get(clz, key);
        if (result != null) {
            return true;
        }
        return false;
    }
}
