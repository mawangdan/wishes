--KEYS（为key的参数）:
--所有BanSet的名称
--BanIndex的名称
--ARGV（不为key的参数）:
--BanSet的数量
--要加入BanSet的jwt
--BanSet的过期时间

local setCounts = tonumber(ARGV[1])
local jwt = ARGV[2]
local expireTime = tonumber(ARGV[3])

local banIndexKey = KEYS[setCounts + 1]
local banIndexInRedis = 0;

if(0 == redis.call('EXISTS', banIndexKey)) then
    --不存在就新建值为0的banIndex
    redis.call('SET', banIndexKey, banIndexInRedis)
else
    banIndexInRedis = tonumber(redis.call('GET', banIndexKey))
end

--获取banIndex指向的set的名字
local setName = KEYS[banIndexInRedis % setCounts + 1]

if(0 == redis.call('EXISTS', setName)) then
    --set不存在就新建set，将jwt扔到其中，并设置过期时间
    redis.call('SADD', setName, jwt)
    redis.call('EXPIRE', setName, 2 * expireTime)
else
    --获取set的过期时间
    local timeToLive = tonumber(redis.call('TTL', setName))
    if(timeToLive > expireTime) then
        --还没有过半，就直接将jwt扔到set中
        redis.call('SADD', setName, jwt)
    else
        --已经过半，得进行切换
        redis.call('INCR', banIndexKey)
        banIndexInRedis = tonumber(redis.call('GET', banIndexKey))

        --获得切换后的set的名字
        setName = KEYS[banIndexInRedis % setCounts + 1]

        --保险起见删除原set，并建立新的set，将jwt扔到set中，并设置过期时间
        redis.call('DEL', setName)
        redis.call('SADD', setName, jwt)
        redis.call('EXPIRE', setName, 2 * expireTime)
    end
end
