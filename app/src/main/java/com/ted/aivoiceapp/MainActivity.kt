package com.ted.aivoiceapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ted.lib_base.event.EventManager
import com.ted.lib_base.event.MessageEvent
import com.ted.lib_base.helper.ARouterHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventManager.register(this)
        btn1.setOnClickListener {
            ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
        }
        btn2.setOnClickListener {
            EventManager.post(222, "hello")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        when (event?.type) {
            111 -> Log.d("TestApp", "onMessageEvent: 111")
            222 -> Log.d("TestApp", "onMessageEvent: 222, ${event?.stringValue}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.unRegister(this)
    }
}