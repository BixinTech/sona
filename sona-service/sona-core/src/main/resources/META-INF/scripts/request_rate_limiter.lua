local capacity = tonumber(ARGV[1])
local h_capacity = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local deduct = tonumber(ARGV[4])
local request = tonumber(ARGV[5])

local allowed_num = 0

local h_last_refreshed = tonumber(redis.call('get', KEYS[3]))
if h_last_refreshed == nil then
    h_last_refreshed = 0
end

local h_tokens = tonumber(redis.call('hget', KEYS[1], 'h_token'))
if h_tokens == nil then
    h_tokens = h_capacity
end

local delta = math.max(0, now - h_last_refreshed)
local h_filled_tokens = math.min(h_capacity, h_tokens + (delta * h_capacity))

if capacity == 0 then

    local allowed = h_filled_tokens >= request
    local new_tokens = h_filled_tokens

    if allowed then
        allowed_num = 1
        new_tokens = h_filled_tokens - request
    end

    redis.call('hset', KEYS[1], 'h_token', new_tokens)
    redis.call('setex', KEYS[3], '1', now)

else

    local tokens = tonumber(redis.call('hget', KEYS[1], 'token'))
    if tokens == nil then
        tokens = capacity
    end

    local last_refreshed = tonumber(redis.call('get', KEYS[2]))
    if last_refreshed == nil then
        last_refreshed = 0
    end

    local delta = math.max(0, now - last_refreshed)

    local filled_tokens = math.min(capacity, tokens + (delta * capacity))
    local allowed = h_filled_tokens + filled_tokens > request
    local new_tokens = filled_tokens

    if allowed then
        allowed_num = 1
        new_tokens = filled_tokens - deduct
    end

    redis.call('hset', KEYS[1], 'token', new_tokens)
    redis.call('setex', KEYS[2], '1', now)

end

redis.call('expire', KEYS[1], '1')

return allowed_num