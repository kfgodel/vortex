#!/bin/bash
mvn clean compile package -Dmaven.test.skip=true
echo "War generado. Ahora falta deployarlo"