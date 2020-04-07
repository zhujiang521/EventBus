package com.zj.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.event.core.EventBus
import kotlinx.android.synthetic.main.activity_two.*

class TwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        initView()
    }

    private fun initView() {
        btnSendMessage.setOnClickListener {
            EventBus.post(Person(name = "朱江",age = 23))
        }
    }
}
