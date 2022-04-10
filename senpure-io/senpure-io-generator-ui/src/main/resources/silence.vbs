Dim fso,f
Set fso = CreateObject("Scripting.FileSystemObject")
 f = wscript.scriptname
 f = left(f,len(f)-4)+".bat"
'msgbox  f

DIM objShell
set objShell=wscript.createObject("wscript.shell")
iReturn=objShell.Run("cmd.exe /C "+f, 0, TRUE)