
--rootShell:statrtApp("-a android.intent.action.VIEW -d  "..argArray[3]);

local mainView = "com.donson.leplay.store/com.donson.leplay.store.gui.main.MainActivity";

rootShell.startActivity("-n "..mainView);

Log.i("lualog", "!!!!!!!!~~~~~~~")

while true
do
	if string.find(rootShell.getCurrentActivityName(), mainView) == nil then
		Log.i("lualog", "!!!!!!!!++++++++")
		sleep(1)
	else
		Log.i("lualog", "!!!!!!!!--------")
		sleep(8)
		rootShell.simulateTap(630, 630);
		break
	end
end

sleep(1)
--
--  installView = "com.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity";
--  while true
--  do
--  	if string.find(rootShell:getCurrentActivityName(), installView) == nil then
--  		sleep(1)
--  	else
--  		sleep(3)
--  		rootShell:simulateTap(200, 1200);
--  		sleep(2)
--  		rootShell:simulateTap(630, 811);
--  		break
--  	end
--  end
--
--  while true
--  do
--  	if string.find(rootShell:getCurrentActivityName(), installView) == nil then
--  		sleep(1)
--  	else
--  		sleep(3)
--  		rootShell:simulateTap(200, 1200);
--  		sleep(2)
--  		rootShell:simulateTap(630, 1000);
--  		break
--  	end
--  end
--
--  while true
--  do
--  	if string.find(rootShell:getCurrentActivityName(), installView) == nil then
--  		sleep(1)
--  	else
--  		sleep(3)
--  		rootShell:simulateTap(200, 1200);
--  		break
--  	end
--  end
--
--  sleep(1);
--  rootShell:simulateSwipe(630, 1000, 630, 630, 600)
--  sleep(1);
--  rootShell:simulateTap(630, 811);
--
--  while true
--  do
--  	if string.find(rootShell:getCurrentActivityName(), installView) == nil then
--  		sleep(1)
--  	else
--  		sleep(3)
--  		rootShell:simulateTap(200, 1200);
--  		break
--  	end
--  end
--
--  rootShell:sleep(300);

sleep(10)
  
  