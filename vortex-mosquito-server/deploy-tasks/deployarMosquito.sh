#!/bin/bash
cp -rf deploy-tasks/mosquito /archivos/vortex/vortex-mosquito-server
cp -rfv deploy-tasks/mosquito/* /archivos/vortex/vortex-mosquito-server/
cp -fv target/vortex-mosquito-server-jar-with-dependencies.jar /archivos/vortex/vortex-mosquito-server/lib
