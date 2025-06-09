@echo off
pushd %~dp0

FOR %%I IN (.) DO set CurrDirName=%%~nI%%~xI
REM for %%* in (.) do set CurrDirName=%%~n*
echo %CurrDirName%

cd ..\..\..\..\tools\exec\

call %CurrDirName%.bat

popd
