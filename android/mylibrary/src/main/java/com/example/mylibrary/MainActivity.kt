package com.example.mylibrary

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.PackageList
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.config.ReactFeatureFlags
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.soloader.SoLoader


// React native documentation has suggested to use the Activity from (import android.app.Activity). But React Navigation just worked
// with AppCompatActivity from androidx.appcompat.app.AppCompatActivity
class MeiliActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    private lateinit var reactRootView: ReactRootView
    private lateinit var reactInstanceManager: ReactInstanceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null);
        SoLoader.init(this, false)
        reactRootView = ReactRootView(this)

        // overriding Reanimated UI Instance Manager to be possible renderer the UI React Instance Manager in existing apps
        // and adding manually in the packages the new one class instance


        val packages: List<ReactPackage> = PackageList(application).packages.apply {
            add(MeiliNativePackage())
        }

        reactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackages(packages)
            .setUseDeveloperSupport(true)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()

        // Send message from native code
        val initialProperties = Bundle()
        val criteriaList = intent.extras?.get("searchCriteria").toString()

        initialProperties.putString("searchCriteria", criteriaList)
        // The string here (e.g. "MeiliRN") has to match
        // the string in AppRegistry.registerComponent() in
        // the Meili React Native app index.js file.
        ReactFeatureFlags.useTurboModules = false
        reactRootView.setIsFabric(false)
        reactRootView.startReactApplication(reactInstanceManager, "MeiliRN", initialProperties)
        setContentView(reactRootView)
    }

    override fun invokeDefaultOnBackPressed() {
        reactInstanceManager.onBackPressed()
        super.onBackPressed()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        reactInstanceManager.onBackPressed()

        // commented because it was destroying the Meili Activity and not going back to the previous screen
//        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        reactInstanceManager.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()
        reactInstanceManager.onHostResume(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        reactInstanceManager.onHostDestroy(this)
        reactRootView.unmountReactApplication()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            reactInstanceManager.showDevOptionsDialog()
            return true
        }

        return super.onKeyUp(keyCode, event)
    }
}


