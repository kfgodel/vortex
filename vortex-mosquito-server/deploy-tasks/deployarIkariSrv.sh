#!/bin/bash
cp -rf deploy-tasks/ikariSrv02 /instalados/propios/vortex/vortex-mosquito-server
cp -rfv deploy-tasks/ikariSrv02/* /instalados/propios/vortex/vortex-mosquito-server/
cp -fv target/vortex-mosquito-server-jar-with-dependencies.jar /instalados/propios/vortex/vortex-mosquito-server/lib
