@echo off

TITLE anal_watch.cmd

REM from exec.cmd
pushd %~dp0

set info_bk=
set isFirst=yes

:loop
    REM clear
    set info=

    REM sleep 1 second
    ping 127.0.0.1 -n 2 > nul

    REM Get file infos
    REM "delims=" is useful to show long filenames with spaces in it....
    REM 
    REM '/b" show only names, not size dates etc..
    REM 
    REM Some things to know about dir's /a argument.
    REM 
    REM Any use of "/a" would list everything, including hidden and system attributes.
    REM "/ad" would only show subdirectories, including hidden and system ones.
    REM "/a-d" argument eliminates content with 'D'irectory attribute.
    REM "/a-d-h-s" will show everything, but entries with 'D'irectory, 'H'idden 'S'ystem attribute.
    REM If you use this on the commandline, remove a "%".
    REM More help see: for /?
    REM ~z : size of file
    REM ~t : date/time of file
    REM pushd case
        REM for /f "delims=" %%f in ('dir /a-d-h-s /b *.xls') do call :concat %%~zf %%~tf

    REM Get time with seconds version
    pushd case
        for /f "delims=" %%i in ('"forfiles /m *.xls /c "cmd /c echo @fname @ftime" "') do call :concat %%i
    popd


    REM Detect if info changed. Notice: if broken while contains " ", so we replace all " " to "_"
    if "%info: =___%" NEQ "%info_bk: =___%" (
        set info_bk=%info%

        if %isFirst%==no (
            REM (1) Call here can't kill current task! cause cmd is a single thread program
            REM (1) call :killtask exec-auto

            REM (2) taskkill /F /T /FI "WINDOWTITLE eq analyzer.cmd"
            REM (2) start "analyzer.cmd" analyzer.cmd < nul

            REM echo changed from %info% to %info_bk%
            REM use "< nul" to disable pause in sub command
            call anal.cmd < nul

            TITLE anal_watch.cmd
            echo = Watching: "%case_path%"
            echo ========================= Waiting next change - %time% =========================
        ) else (
            set isFirst=no
            echo = Watching: "%case_path%"
            echo ========================= Waiting change =========================
        )
    )

goto loop


REM    litsento case/*.xls 
REM      if changed? 
REM        kill_all_existed_process
REM        call analyzer.cmd







:concat

set fileName=%1
REM Check if file name contains string '~$' (editing excel tmp files), by remove '$' then compare both string
REM echo %*
if %fileName:$=%==%fileName% (
    set info=%info% %*;
)

goto :eof



REM REM (1)
REM :killtask
REM 
REM set processName=%1
REM REM Kill ant task only
REM for /f "skip=1 usebackq" %%h in (`wmic process where "Name like 'java%%.exe' and CommandLine like '%%%processName%%%'" get ProcessId ^| findstr .`) do taskkill /F /T /PID %%h
REM 
REM goto :eof
