local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]
--redis.log(redis.LOG_WARNING, "tokens_key " .. tokens_key)

local rate = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local now = tonumber(ARGV[3])

local window_size = tonumber(capacity / rate)
local window_time = 1

--redis.log(redis.LOG_WARNING, "rate " .. ARGV[1])
--redis.log(redis.LOG_WARNING, "capacity " .. ARGV[2])
--redis.log(redis.LOG_WARNING, "now " .. ARGV[3])
--redis.log(redis.LOG_WARNING, "window_size " .. window_size)

local last_requested = 0
local exists_key = redis.call('exists', tokens_key)
if (exists_key == 1) then
    last_requested = redis.call('zcard', tokens_key)
end
--redis.log(redis.LOG_WARNING, "last_requested " .. last_requested)

local remain_request = capacity - last_requested
local allowed_num = 0
if (last_requested < capacity) then
    allowed_num = 1
    redis.call('zadd', tokens_key, now, timestamp_key)
end

--redis.log(redis.LOG_WARNING, "remain_request " .. remain_request)
--redis.log(redis.LOG_WARNING, "allowed_num " .. allowed_num)

redis.call('zremrangebyscore', tokens_key, 0, now - window_size / window_time)
redis.call('expire', tokens_key, window_size)

return { allowed_num, remain_request }

