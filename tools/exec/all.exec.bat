title %~n0
for %%i in (eu*.bat) do call ant exec-gw -Dname=eu/%%~ni
pause