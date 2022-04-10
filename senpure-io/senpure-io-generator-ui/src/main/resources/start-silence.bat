@echo off
set rr="HKCU\Console\%%SystemRoot%%_system32_cmd.exe"
  reg add %rr% /v "WindowPosition" /t REG_DWORD /d 0x00080008 /f>nul
  reg add %rr% /v "ScreenBufferSize" /t REG_DWORD /d 0x03e800c8 /f>nul
  reg add %rr% /v "WindowSize" /t REG_DWORD /d 0x03200c8 /f>nul
  if not defined ff (set ff=0&start cmd /c %0&(exit))
  reg delete %rr% /f>nul
set currentPath=%cd%
::echo %currentPath%
set fileName=""
for  %%a in (*) do (
   echo %%a|find /i "senpure-io-generator" >nul && set fileName=%%a
)
if %fileName% =="" (
    echo "没有找到可运行的jar文件"
    goto :end
)

::echo %fileName%
title %fileName%
call java -Dsilence=true -jar %fileName% 
echo 5秒后关闭该窗口,要想停止请按ctrl + c
ping /n 2 127.1 >nul
echo 4秒后关闭该窗口,要想停止请按ctrl + c
ping /n 2 127.1 >nul
echo 3秒后关闭该窗口,要想停止请按ctrl + c
ping /n 2 127.1 >nul
echo 2秒后关闭该窗口,要想停止请按ctrl + c
ping /n 2 127.1 >nul
echo 1秒后关闭该窗口,要想停止请按ctrl + c
ping /n 2 127.1 >nul
:end

