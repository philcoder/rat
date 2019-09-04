@echo off
goto check_Permissions

:check_Permissions
    echo Administrative permissions required...

    net session >nul 2>&1
    if %errorLevel% == 0 (
        echo Success: Administrative permissions confirmed.
		goto install_process
    ) else (
        echo Failure: Inadequate current permissions, execute as Administrative Level
		pause >nul
    )

:install_process
	REM The following directory is for .NET 4.0
	set DOTNETFX2=%SystemRoot%\Microsoft.NET\Framework\v4.0.30319
	set PATH=%PATH%;%DOTNETFX2%

	echo Installing Service...
	echo ---------------------------------------------------
	InstallUtil /i "%~dp0MachineWatcher.exe"
	net start MachineWatcherService
	echo ---------------------------------------------------
	echo Done.