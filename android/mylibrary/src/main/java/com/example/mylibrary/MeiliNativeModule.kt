package com.example.mylibrary


//import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class MeiliNativeModule internal constructor(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var query = ""

    override fun getName(): String {
        return "MeiliNativeModule"
    }

    @ReactMethod
    fun sendMessageToNative(meiliQuery: String?) {
        Log.d("This log is from java", meiliQuery ?: "")
        if (meiliQuery != null) {
            query = meiliQuery
        }
        // meiliQuery is being parsed to json with JSON.stringfy in JS side. Then, it can
        // be used wherever and to be saved in native side.
    }

    @ReactMethod
    fun sendCallbackToNative(rnCallback: Callback) {
        rnCallback.invoke("A greeting from java")
        // to be executed in native side from JS CODE to communicate.
    }

    @ReactMethod
    fun finishActivity() {
        currentActivity?.finish()
    }

//    @ReactMethod
//    fun goToSecondActivity() {
//        val intent = Intent(currentActivity, LoggedActivity::class.java)
//        intent.putExtra("meiliQuery", query)
//        currentActivity?.startActivity(intent)
//    }

    companion object

}
