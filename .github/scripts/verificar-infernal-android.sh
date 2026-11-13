#!/usr/bin/env bash
# Real-device emulator smoke test. A successful Java compilation is not enough:
# this launches the APK, generates levels and starter inventories, renders
# gameplay on two regions, and fails on a fatal logcat signal.
set -euo pipefail
APP="com.erick.shattereddungeon.personalizado.tibia2"
ACTIVITY="com.shatteredpixel.shatteredpixeldungeon.android.AndroidLauncher"
APK="android/build/outputs/apk/debug/android-debug.apk"
test -s "$APK"
adb install -r "$APK"

# Android 16 emulator sometimes briefly drops ADB/logcat right after a cold
# Activity launch. Don't interpret a transient log reader failure as an APK
# crash. Fetch a filtered on-device log, retrying connection failures.
capture_logs () {
  local attempt
  for attempt in 1 2 3 4 5; do
    if adb shell logcat -d -v brief -s 'INFERNAL_SMOKE:I' 'AndroidRuntime:E' '*:S'          > /tmp/infernal-log.txt 2>/tmp/infernal-adb-error.txt; then
      return 0
    fi
    echo "WARN: Android logcat connection interrupted (try $attempt/5)"
    cat /tmp/infernal-adb-error.txt || true
    adb devices -l || true
    sleep 3
  done
  echo "FAILED: unable to read Android 16 logcat after 5 attempts"
  return 1
}

app_alive () {
  local attempt
  for attempt in 1 2 3 4 5; do
    if adb shell pidof "$APP" >/dev/null 2>&1; then
      return 0
    fi
    sleep 2
  done
  return 1
}

check_depth () {
  local depth="$1"
  echo "== Android gameplay smoke test - depth ${depth} =="
  adb shell am force-stop "$APP" || true
  adb logcat -c
  adb shell am start -W -n "$APP/$ACTIVITY" \
    --ez infernal_smoke true --ei infernal_smoke_depth "$depth"

  local ready=0
  for i in $(seq 1 90); do
    capture_logs
    if grep -q 'INFERNAL_SMOKE.*FAIL' /tmp/infernal-log.txt; then
      grep -E 'INFERNAL_SMOKE|FATAL EXCEPTION|java.lang.' /tmp/infernal-log.txt | tail -n 100 || true
      echo "FAILED: game initialization or dungeon-generation smoke test"
      exit 1
    fi
    if grep -q "INFERNAL_SMOKE.*PASS READY_TO_RENDER depth=${depth}" /tmp/infernal-log.txt; then
      ready=1
      break
    fi
    sleep 2
  done
  if [ "$ready" -ne 1 ]; then
    grep -E 'INFERNAL_SMOKE|FATAL EXCEPTION|AndroidRuntime' /tmp/infernal-log.txt | tail -n 100 || true
    echo "FAILED: did not reach test gameplay scene"
    exit 1
  fi

  # Give the real GameScene a chance to load sprites, textures and animations.
  sleep 16
  if ! app_alive; then
    echo "FAILED: game process exited after entering GameScene"
    capture_logs || true
    grep -E 'FATAL EXCEPTION|AndroidRuntime|INFERNAL_SMOKE' /tmp/infernal-log.txt | tail -n 100 || true
    exit 1
  fi

  capture_logs
  if grep -q 'INFERNAL_SMOKE.*FAIL' /tmp/infernal-log.txt || \
    (grep -q 'FATAL EXCEPTION' /tmp/infernal-log.txt && grep -q "Process: $APP" /tmp/infernal-log.txt); then
    echo "FAILED: Android exception while rendering"
    grep -E 'INFERNAL_SMOKE|FATAL EXCEPTION|AndroidRuntime|Caused by:' /tmp/infernal-log.txt | tail -n 100 || true
    exit 1
  fi
  echo "PASS_ANDROID_RENDER depth=$depth"
  grep 'INFERNAL_SMOKE.*PASS' /tmp/infernal-log.txt | tail -n 35 || true
}

check_depth 1
check_depth 5
check_depth 10
check_depth 12
check_depth 20
check_depth 25
echo "INFERNAL_ANDROID_SMOKE_COMPLETE"
