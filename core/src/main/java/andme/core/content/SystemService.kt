/*
 * Copyright (C) 2018 Lucio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package andme.core.content

import android.app.ActivityManager
import android.content.Context
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager


/**
 * 震动服务
 */
inline val Context.vibrator: android.os.Vibrator
    get() = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator

/**
 * 布局加载服务LayoutInflater
 */
inline val Context.layoutInflater: android.view.LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater

/**
 * 网络服务ConnectivityManager
 */
inline val Context.connectivityManager: android.net.ConnectivityManager?
    get() = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager

inline val Context.wifiManager: WifiManager
    get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
/**
 * TelephonyManager
 */
inline val Context.telephonyManager: TelephonyManager
    get() = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

/**
 * ActivityManager
 */
inline val Context.activityManager: ActivityManager?
    get() = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

/**
 * PowerManager
 */
inline val Context.powerManager: PowerManager
    get() = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager


/**
 * InputMethodManager
 */
inline val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


inline val Context.clipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

inline val Context.locationManager
    get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager