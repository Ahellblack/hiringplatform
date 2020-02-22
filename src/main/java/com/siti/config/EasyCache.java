package com.siti.config;

import com.github.benmanes.caffeine.cache.Cache;

import com.siti.common.ConstantYmlValue;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by DC on 2019/12/10 - 17:21- {TIME}.
 **/
@Component
public class EasyCache<K, V, T, R> {

    private static Logger logger = LoggerFactory.getLogger(EasyCache.class);

    private static final String NIL = "nil";

    public static Cache initCaffine;

    public static RedissonClient redisson;

    public ReentrantLock reentrantLock=new ReentrantLock();

    @Resource
    ConstantYmlValue constantYmlValue;

    public void init(){
        reentrantLock.lock();
        try{
            if(initCaffine==null){
                initCaffine = InitCaffine.getInstance(constantYmlValue.getCaffineDuration(), TimeUnit.SECONDS, 5000);
                logger.info("init caffine...");
            }
            if(redisson==null){
                Config config = new Config();
                config.useSingleServer()
                        .setAddress(constantYmlValue.getRedisAddr())
                        .setPassword(constantYmlValue.getRedisPassword());
                //初始化内存缓存
                redisson = Redisson.create(config);
                logger.info("init redisson...");
            }
        }catch (Exception e){
            logger.error("init cache error");
        }finally {
            reentrantLock.unlock();
        }

    }

    public EasyCache(){

    }

    /* *//**
     * 集群配置
     *//*
    private void initClusterCache() {
        //初始化进程内缓存
        initCaffine = InitCaffine.getInstance(3, TimeUnit.MINUTES, 1000);
        //创建配置
        Config config = new Config();
        //指定使用集群部署方式
        config.useClusterServers()
                // 集群状态扫描间隔时间 单位是毫秒
                .setScanInterval(2000)
                //cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
                .addNodeAddress("redis://127.0.0.1:6379")
                .addNodeAddress("redis://127.0.0.1:6380")
                .addNodeAddress("redis://127.0.0.1:6381")
                .addNodeAddress("redis://127.0.0.1:6382")
                .addNodeAddress("redis://127.0.0.1:6383")
                .addNodeAddress("redis://127.0.0.1:6384");
        //初始化内存缓存
        redisson = Redisson.create(config);
    }

    *//**
     * 单节点配置
     *//*
    public void initSingleCache() {
        //初始化进程内缓存
        initCaffine = InitCaffine.getInstance(3, TimeUnit.MINUTES, 1000);
        //创建配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        //初始化内存缓存
        redisson = Redisson.create(config);
    }

    *//**
     * 哨兵模式配置
     *//*
    private void initMasterCache() {
        //初始化进程内缓存
        initCaffine = InitCaffine.getInstance(3, TimeUnit.MINUTES, 1000);
        //创建配置
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName("master")
                .addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379")
                .addSentinelAddress("127.0.0.1:26319");
        RedissonClient redisson = Redisson.create(config);
        //初始化内存缓存
        redisson = Redisson.create(config);
    }
*/

    /**
     * 1.默认先放入caffeine（默认过期时间3分钟）
     * 2.然后放入redis
     * @return
     */
    public void putNonPersistence(K key, String redissonKey, V value) {
        RLock lock = null;
        try {
            initCaffine.put(key, value);
            lock = redisson.getLock(redissonKey);
            try {
                if (!lock.tryLock(0, 20, TimeUnit.SECONDS)) {
                    return;
                }
                RBucket bucket = redisson.getBucket(key.toString());
                bucket.expire(60 + new Random().nextInt(30), TimeUnit.SECONDS);
                bucket.set(value);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * -------------RBucket可以是任何一种对象-------------
     * 1.修改数据库
     * 2.存入caffeine缓存
     * 3.存入redis
     * 4.对于查询为空得数据也进行缓存防止缓存穿透或者是
     * Todo 使用布隆过滤
     *
     * @param key      存入缓存的key，对应的value是function返回的value
     * @param args1    function入参
     * @param function 传入方法操作数据库r
     * @return
     */
    public R putAndGetFunction(K key, T args1, String redissonKey, Function<T, R> function) {
        RLock lock = null;
        R ret1 = null;
        lock = redisson.getLock(redissonKey);
        try {
            ret1 = fetchBucketValue(key);
            if (!Objects.isNull(ret1)) {
                return ret1;
            }
            if (!lock.tryLock(1, 15, TimeUnit.SECONDS)) {
                ret1 = (R) NIL;
                return ret1;
            }
            if(!Objects.isNull(fetchBucketValue(key))){
                return ret1;
            }
            ret1 = function.apply(args1);
            long expiretime;
            if (Objects.isNull(ret1)) {
                ret1 = (R) NIL;
                expiretime = 60;
            } else {
                expiretime = 60 + new Random().nextInt(30);
            }
            initCaffine.put(key, ret1);
            RBucket bucket = redisson.getBucket(key.toString());
            bucket.set(ret1);
            bucket.expire(expiretime, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if(!Objects.isNull(lock)){
                lock.unlock();
            }
        }
        return ret1;
    }

    /**
     * 1.修改数据库
     * 2.存入caffeine缓存
     * 3.存入redis
     * 4.对于查询为空得数据也进行缓存防止缓存穿透或者是
     * 使用布隆过滤器BitMap/set过滤没有的数据
     *
     * @param key      存入缓存的key，对应的value是function返回的value
     * @param Supplier 传入方法操作数据库
     * @return
     */
    public R putAndGetSupplier(K key, String redissonKey, Supplier<R> Supplier) {
        RLock lock = null;
        R ret1 = null;
        lock = redisson.getLock(redissonKey);
        try {
            ret1 = fetchBucketValue(key);
            if (!Objects.isNull(ret1)) {
                return ret1;
            }
            if (!lock.tryLock(1, 5, TimeUnit.SECONDS)) {
                ret1 = (R) NIL;
                return ret1;
            }
            if(!Objects.isNull(fetchBucketValue(key))){
                return ret1;
            }
            ret1 = Supplier.get();
            long expiretime;
            if (Objects.isNull(ret1)) {
                ret1 = (R) NIL;
                expiretime = 60;
            } else {
                expiretime = 90 + new Random().nextInt(30);
            }
            initCaffine.put(key, ret1);
            RBucket bucket = redisson.getBucket(key.toString());
            bucket.set(ret1);
            bucket.expire(expiretime, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if(!Objects.isNull(lock)){
                lock.unlock();
            }
        }
        return ret1;
    }

    /**
     * 1.从caffeine中获取
     * 2.从redis中获取数据
     *
     * @param key
     * @return
     */
    public R fetchBucketValue(K key) {
        R r = (R) initCaffine.getIfPresent(key); // 缓存中存在相应数据，则返回；不存在返回null
        if (Objects.isNull(r)) {
            RBucket bucket = redisson.getBucket(key.toString());
            r = (R) bucket.get();
        }
        return r;
    }
}
