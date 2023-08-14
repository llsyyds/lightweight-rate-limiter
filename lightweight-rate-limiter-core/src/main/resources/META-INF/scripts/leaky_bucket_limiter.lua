-- current key
local leaky_bucket_key = KEYS[1]
-- last update key
local last_bucket_key = KEYS[2]
-- the rate of leak water
local rate = tonumber(ARGV[1])
-- capacity
local capacity = tonumber(ARGV[2])
-- current timestamp
local now = tonumber(ARGV[3])
-- request count
local requested = tonumber(ARGV[4])
--设置redis中的过期时间为一个满桶的请求全部流完的时间+1，
--与下面的key_bucket_count设置相呼应，当leaky_bucket_key不在redis中存在时候，证明这个键对应的漏桶已经是空的了
local key_lifetime = math.ceil((capacity / rate) + 1)


-- the yield of water in the bucket default 0
local key_bucket_count = tonumber(redis.call("GET", leaky_bucket_key)) or 0

-- the last update time default now
local last_time = tonumber(redis.call("GET", last_bucket_key)) or now

-- the time difference
local millis_since_last_leak = now - last_time

-- the yield of water had lasted
local leaks = millis_since_last_leak * rate

if leaks > 0 then
    -- clean up the bucket
    if leaks >= key_bucket_count then
        key_bucket_count = 0
    else
        -- compute the yield of water in the bucket
        key_bucket_count = key_bucket_count - leaks
    end
    last_time = now
end

-- is allowed pass default not allow
local is_allow = 0

local new_bucket_count = key_bucket_count + requested
-- allow
if new_bucket_count <= capacity then
    is_allow = 1
else
    -- not allow
    return {is_allow, 0}
end

-- update the key bucket water yield
redis.call("SETEX", leaky_bucket_key, key_lifetime, new_bucket_count)

-- update last update time
redis.call("SETEX", last_bucket_key, key_lifetime, now)

-- return
return {is_allow, capacity-new_bucket_count}