#!/bin/sh
#
# Start/Stop the Schedule-p2p server.
#



SERVER_PID_DIR="./logs"
SERVER_LOG="${SERVER_PID_DIR}/server.out"
SERVER_PID="${SERVER_PID_DIR}/server.pid"

CLASSPATH="../schedule/:../dep-lib/*"  

if [ ! -d "${SERVER_PID_DIR}" ]; then
mkdir "${SERVER_PID_DIR}"
fi 

if [ ! -f "${SERVER_LOG}" ]; then
touch "${SERVER_LOG}"
fi 
 
loadJavaOpts () {
  server_java_opts=`grep '^[ \t]*server\.java\.opts' ./server.conf | sed 's/[ \t]*server\.java\.opts=//'`
  if [ -z "${server_java_opts}" ]; then
  echo "-XX:MaxPermSize=100m -Xms512M -Xmx1024M"
  fi
  echo "${server_java_opts}"
}

waitForPid () {
  SPPID=${1}
  MAXTRIES=${2}

  TRIES=0
  echo "waitForPid: waiting for ${SPPID}"
  while [ 1 -eq 1 ]; do
    PIDCHECK=`kill -0 ${SPPID} 2> /dev/null`
    if [ $? -eq 1 ]; then
      echo "Schedule-p2p server PID ${SPPID} exited"
      return 1
    fi
 	echo "waitForPid: PID ${SPPID} still alive" 
    sleep 2
    TRIES=`expr ${TRIES} + 1`
    if [ ${TRIES} -ge ${MAXTRIES} ] ; then
       echo "num TRIES exhausted: ${TRIES} -ge ${MAXTRIES}"
       break
    fi
  done

  echo "Schedule-p2p server PID ${SPPID} did not exit."
  return 0;
}

doStop () {
  doStopSignal "TERM"
}

doStopSignal () {

  SIGNAME=${1}
  if [ "x${SIGNAME}" = "x" ] ; then
    echo "No signal specified"
    exit 127
  fi

  echo "checking pidfile exists: ${SERVER_PID}"
  if [ -f "${SERVER_PID}" ] ; then
    SPPID=`cat ${SERVER_PID} | tr -d ' '`
    if [ "x${SPPID}" = "x" ] ; then
      echo "Schedule-p2p pid file was empty: ${SERVER_PID}"
      exit 127
    fi
    kill -${SIGNAME} ${SPPID} 2> /dev/null
    
    waitForPid ${SPPID} 60
    if [ $? -eq 0 ] ; then
      exit 1
    fi
    rm -f ${SERVER_PID}
  else 
    echo "Schedule-p2p server not running (no pid file found: ${SERVER_PID})"
  fi
}

doStart () {
# Is the server already running?
echo "checking pidfile exists: ${SERVER_PID}"
if [ -f "${SERVER_PID}" ] ; then
  SPPID=`cat ${SERVER_PID} | tr -d ' '`
  if [ ! "x${SPPID}" = "x" ] ; then
    PIDCHECK=`kill -0 ${SPPID} 2> /dev/null`
    if [ $? -eq 1 ]; then
      echo "Removing stale pid file ${SERVER_PID}"
      rm -f ${SERVER_PID}
    else 
      echo "Schedule-p2p server is already running (pid ${SPPID})."
      exit 0
    fi
  fi
fi

# Setup JAVA_OPTS from server.conf
JAVA_OPTS=`loadJavaOpts`

# Start the server
echo "Booting the Schedule-p2p server (Using JAVA_OPTS=${JAVA_OPTS})..."

java ${JAVA_OPTS} -cp ${CLASSPATH} com.fenlibao.p2p.schedule.server.Schedule > ${SERVER_LOG} 2>&1 &

  # Save the pid to a pidfile
  SPPID=$!
  echo "${SPPID}" > ${SERVER_PID}
}

case "$1" in
  start)
    echo "Starting schedule-p2p server..."
    doStart
    echo "Schedule-p2p server booted."
    ;;
  stop)
    echo "Stopping Schedule-p2p server..."
    doStop
    mv ${SERVER_LOG} /data/log/fenlibao-schedule/logs/server.out_$(date +'%Y-%m-%d_%H_%M_%S')
    echo "Schedule-p2p server is stopped."
    ;;
  *)
    # Print help, don't advertise halt, it's nasty
    echo "Usage: $0 {start|stop}" 1>&2
    exit 1
    ;;
esac

exit 0
