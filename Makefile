ANDROID_VERSION=29
BUILD_TOOLS_VERSION=29.0.1
JAVA_BIN=/snap/android-studio/77/android-studio/jre/bin
ANDROID_SDK_PATH=${HOME}/Android/Sdk
ANDROID_JAR=${ANDROID_SDK_PATH}/platforms/android-${ANDROID_VERSION}/android.jar 
DX=${ANDROID_SDK_PATH}/build-tools/${BUILD_TOOLS_VERSION}/dx
AAPT=${ANDROID_SDK_PATH}/build-tools/${BUILD_TOOLS_VERSION}/aapt
ADB=${ANDROID_SDK_PATH}/platform-tools/adb
JARSIGNER=${JAVA_BIN}/jarsigner 

all: init target/tripdb.apk test

init:
	mkdir -p target/classes target/code


target/classes/%.class: src/%.java target/code/me
	javac -classpath ${ANDROID_JAR} -sourcepath target/code -sourcepath src -d target/classes target/code/me/leo/tripdb/R.java $<

classes.dex: target/classes/me/leo/tripdb/MainActivity.class
	${DX} --dex --output $@ target/classes

target/code/me target/tripdb.ap_: res/layout/main.xml AndroidManifest.xml
	${AAPT} package -I ${ANDROID_JAR} -f -M AndroidManifest.xml -m -J target/code -S res -F target/tripdb.ap_

target/tripdb.apk: classes.dex target/tripdb.ap_
	${AAPT} add target/tripdb.ap_ $<
	${JARSIGNER} -keystore leo.keystore -signedjar $@ target/tripdb.ap_ tripdb



test:
	${ADB} install -r target/tripdb.apk
	${ADB} shell am start -n me.leo.tripdb/me.leo.tripdb.MainActivity

clean:
	rm -r target

.PHONY: init clean test
