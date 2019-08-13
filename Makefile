ANDROID_VERSION=29
BUILD_TOOLS_VERSION=29.0.1
JAVA_BIN=/snap/android-studio/77/android-studio/jre/bin
ANDROID_SDK_PATH=${HOME}/Android/Sdk
ANDROID_JAR=${ANDROID_SDK_PATH}/platforms/android-${ANDROID_VERSION}/android.jar
DX=${ANDROID_SDK_PATH}/build-tools/${BUILD_TOOLS_VERSION}/dx
AAPT=${ANDROID_SDK_PATH}/build-tools/${BUILD_TOOLS_VERSION}/aapt
ADB=${ANDROID_SDK_PATH}/platform-tools/adb
JARSIGNER=${JAVA_BIN}/jarsigner 
TGTCP=target/classes/me/leo/tripdb

CLASSES=${TGTCP}/TripList.class ${TGTCP}/This.class ${TGTCP}/TripsUI.class ${TGTCP}/Trip.class ${TGTCP}/DefaultLayout.class ${TGTCP}/TripEditor.class ${TGTCP}/Button.class

all: clean init target/tripdb.apk test

deps-ok:
	./deps.sh

init: .deps-ok
	mkdir -p target/classes target/code


target/classes/%.class: src/%.java target/code/me
	@-rm $@
	javac -classpath $(shell cat .deps-ok)${ANDROID_JAR} -sourcepath target/code -sourcepath src -d target/classes target/code/me/leo/tripdb/R.java $<

classes.dex: ${CLASSES}
	${DX} --dex --output $@ target/classes deps/*

target/code/me target/tripdb.ap_: res/layout/main.xml AndroidManifest.xml
	${AAPT} package -I ${ANDROID_JAR} -f -M AndroidManifest.xml -m -J target/code -S res -F target/tripdb.ap_

target/tripdb.apk: classes.dex target/tripdb.ap_
	${AAPT} add target/tripdb.ap_ $<
	${JARSIGNER} -keystore leo.keystore -storepass 123456 -signedjar $@ target/tripdb.ap_ tripdb
	rm target/tripdb.ap_



test: target/tripdb.apk
	${ADB} install -r target/tripdb.apk
	${ADB} shell am start -n me.leo.tripdb/me.leo.tripdb.TripsUI

clean:
	rm -r target

.PHONY: init clean test
