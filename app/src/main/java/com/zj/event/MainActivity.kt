package com.zj.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.zj.event.core.EventBus
import com.zj.event.core.Subscribe
import com.zj.event.core.ThreadModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.register(this)
        initView()
    }

    private fun initView() {
        btnJump.setOnClickListener {
            startActivity(Intent(this, TwoActivity::class.java))
        }
    }

    @Subscribe
    fun zhu(person: Person) {
        tvText.text = "name=${person.name}   age=${person.age}"
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.unRegister(this)
    }

}
