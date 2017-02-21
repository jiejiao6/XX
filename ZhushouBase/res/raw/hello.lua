import "http"

local url = "http://zhushou.dot7.cn/?mod=mv1&action=loginPhoneToken"
local body,_,code = http.get(url)

if code == 200 then
	Log.i("Luaprint", "result: "..body)
else
	Log.i("Luaprint", "error: "..code)
end

sleep(10)
