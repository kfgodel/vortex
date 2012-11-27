rem mvn clean compile package -Dmaven.test.skip=true
rem mkdir P:\Propios\Vortex\Ikari01
xcopy /E/Y deploy-tasks\Ikari01\* P:\Propios\Vortex\Ikari01\
copy target\vortex-mosquito-server-jar-with-dependencies.jar P:\Propios\Vortex\Ikari01\lib\

