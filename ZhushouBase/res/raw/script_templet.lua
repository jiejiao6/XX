-- 文本输入
rootShell.simulateTypeText("17x21tf79")

-- 读取多个像素点上的颜色值
local colorArray = ScreenAssistant.getColors({{50,50.6},{100,100.3},{550,550.6},{200.5,200.9}})
local colors = luajava.astable(colorArray)
for key, value in pairs(colors) do
	Log.i("Luaprint", "come here...."..key.." "..value);
end

-- 找图
import "android.graphics.Point"
local points = ScreenAssistant.findImage({387, 474, 505, 595}, "weipinghui.png", 10, 0.9, false)

local pointTable = luajava.astable(points)
if next(pointTable) ~= nil then
	local point = pointTable[1]
	Log.i("Luaprint", "come here...."..point.x..", "..point.y);
end

-- 区域找色
import "android.graphics.Point"
local point = ScreenAssistant.findColor({196, 257, 346, 444}, {{"C0E16B"}}, 0)
Log.i("Luaprint", "come here...."..point.x..", "..point.y);

-- 多点找色
import "android.graphics.Point"
local color = "BEDB67"
local point = ScreenAssistant.findMultiColor({206, 279, 342, 440}, {{color}}, {{"25","6","FEFEFD"}, {"6","25","B1D765"}, {"43","22","51DDEA"}}, 0.9)
Log.i("Luaprint", "come here...."..point.x..", "..point.y);

-- 多点比色
local find = ScreenAssistant.cmpColor({{"227", "417", "FFFFFF", "101010"}, {"440", "200", "FFFFFF"}}, 0.9)
if find then
	Log.i("Luaprint", "come here....")
end

-- 找字
import "android.graphics.Point"
local textPoints = ScreenAssistant.findText({200, 400, 350, 450}, {"0FFE06007100034000280003000060000C000340004600307EF800F800", "0", "52", "19"},
{"FFFFFF", "101010"}, 0.9, true)
local pointTable = luajava.astable(textPoints)
if next(pointTable) ~= nil then
	for k, textPoint in pairs(pointTable) do
		Log.i("Luaprint", "come here...."..textPoint.getPoint().x..", "..textPoint.getPoint().y);
	end
end

-- 网络请求
import "http"

local url = "http://zhushou.dot7.cn/?mod=mv1&action=loginPhoneToken"
local body,_,code = http.get(url)

if code == 200 then
	Log.i("Luaprint", "result: "..body)
else
	Log.i("Luaprint", "error: "..code)	
end

sleep(10)


