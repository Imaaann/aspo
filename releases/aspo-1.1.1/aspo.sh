#!/bin/bash
DIR="$(cd "$(dirname "$0")" && pwd)"
java -jar "$DIR/aspo-cli-1.1.1.jar" "$@"
