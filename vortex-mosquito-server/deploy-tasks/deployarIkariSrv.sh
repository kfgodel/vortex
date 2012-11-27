#!/bin/bash
cp -rf deploy-tasks/mosquito /instalados/propios/vortex/vortex-mosquito-server
cp -rfv deploy-tasks/mosquito/* /instalados/propios/vortex/vortex-mosquito-server/
cp -fv target/vortex-mosquito-server-jar-with-dependencies.jar /instalados/propios/vortex/vortex-mosquito-server/lib
