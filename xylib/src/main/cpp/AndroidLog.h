
#pragma once
#ifndef WLPLAYER_ANDROIDLOG_H
#define WLPLAYER_ANDROIDLOG_H

#include <android/log.h>

#define LOG_SHOW true

#define LOGD(FORMAT,...) __android_log_print(ANDROID_LOG_DEBUG,"jnilib",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"jnilib",FORMAT,##__VA_ARGS__);

#endif //WLPLAYER_ANDROIDLOG_H
