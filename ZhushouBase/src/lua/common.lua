-- 分割字符串
function split(szFullString, szSeparator)
	local nFindStartIndex = 1;
	local nSplitIndex = 1;
	local nSplitArray = {};
	while true do
		local nFindLastIndex = string.find(szFullString, szSeparator, nFindStartIndex);
		if not nFindLastIndex then
			nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, string.len(szFullString));
			break;
		end
		nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, nFindLastIndex - 1);
		nFindStartIndex = nFindLastIndex + string.len(szSeparator);
		nSplitIndex = nSplitIndex + 1;
	end
	return nSplitArray;
end

-- 线程休眠
function sleep(n)
	os.execute("sleep " .. n)
end

-- 产生随机数
function random(...)
	--math.randomseed(tonumber(tostring(os.time()):reverse():sub(1, 6)))
	--math.random(...) -- 清除前三次劣质随机数
	--math.random(...)
	--math.random(...)
	math.randomseed(os.clock()*math.random(1000000, 90000000) + math.random(1000000, 90000000))
	return math.random(...)
end