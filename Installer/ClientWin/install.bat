@echo off
goto check_Permissions

:check_Permissions
    echo Administrative permissions required...

    net session >nul 2>&1
    if %errorLevel% == 0 (
        echo Success: Administrative permissions confirmed.
		goto create_dir
    ) else (
        echo Failure: Inadequate current permissions, execute as Administrative Level
		pause >nul
    )
	
:create_dir
	if exist "%ProgramFiles%\MachineWatcher" (
		echo directory MachineWatcher exists and cleanup inside.
		del "%ProgramFiles%\MachineWatcher\*.*" /s /f/ q
		goto copy_files
	) else (
		echo directory MachineWatcher didn't exists and create then.
		MKDIR "%ProgramFiles%\MachineWatcher"
		goto copy_files
	)
    
:copy_files
	COPY "%~dp0MachineWatcher.exe" "%ProgramFiles%\MachineWatcher\MachineWatcher.exe"
	COPY "%~dp0config.ini" "%ProgramFiles%\MachineWatcher\config.ini"
	goto install_process
	
:install_process
	REM The following directory is for .NET 4.0
	set DOTNETFX2=%SystemRoot%\Microsoft.NET\Framework\v4.0.30319
	set PATH=%PATH%;%DOTNETFX2%

	echo Installing Service...
	echo ---------------------------------------------------
	InstallUtil /i "%ProgramFiles%\MachineWatcher\MachineWatcher.exe"
	net start MachineWatcherService
	echo ---------------------------------------------------
	echo Done.