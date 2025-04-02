#!/bin/bash

CLI_MAINCLASS="com.aspodev.cli.Main"
WEB_MAINCLASS="com.aspodev.web.MainWeb"

function usage() {
  echo "Usage:"
  echo "  $0 run <module>   - Run the main class of the module (web or cli)"
  echo "  $0 test <module>  - Run tests for the module (web or cli)"
  echo "  $0 build          - Clean and build the project"
  echo "  $0 clean          - Clean the project"
  exit 1
}

# Check for at least one argument
if [ $# -lt 1 ]; then
  usage
fi

ACTION=$1
ARGUMENTS=${@:3}

case "$ACTION" in
  run)
    # Ensure module is specified
    if [ $# -lt 2 ]; then
      usage
    fi
    MODULE=$2
    if [ "$MODULE" == "cli" ]; then
      echo "Running CLI module..."
      mvn -pl cli exec:java -Dexec.mainClass="$CLI_MAINCLASS" -Dexec.args="$ARGUMENTS"
    elif [ "$MODULE" == "web" ]; then
      echo "Running Web module..."
      mvn -pl web exec:java -Dexec.mainClass="$WEB_MAINCLASS"
    else
      echo "Invalid module specified: $MODULE"
      usage
    fi
    ;;
  test)
    # Ensure module is specified
    if [ $# -ne 2 ]; then
      usage
    fi
    MODULE=$2
    if [ "$MODULE" == "cli" ]; then
      echo "Running tests for CLI module..."
      mvn -pl cli test
    elif [ "$MODULE" == "web" ]; then
      echo "Running tests for Web module..."
      mvn -pl web test
    else
      echo "Invalid module specified: $MODULE"
      usage
    fi
    ;;
  build)
    echo "Cleaning and building the entire project..."
    mvn clean install
    ;;
  clean)
    echo "Cleaning the project..."
    mvn clean
    ;;
  *)
    usage
    ;;
esac
