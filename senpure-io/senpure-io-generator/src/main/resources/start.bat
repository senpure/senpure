@echo off
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
start javaw -jar %fileName%
:end
echo 程序启动中请稍后
ping /n 1 127.1 >nul
cls
echo 程序启动中请稍后。
ping /n 2 127.1 >nul
cls
echo 程序启动中请稍后。。
ping /n 2 127.1 >nul
