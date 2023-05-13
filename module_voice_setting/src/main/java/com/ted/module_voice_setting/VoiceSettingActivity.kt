package com.ted.module_voice_setting

import android.widget.SeekBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ted.lib_base.base.BaseActivity
import com.ted.lib_base.base.adapter.CommonAdapter
import com.ted.lib_base.base.adapter.CommonViewHolder
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.utils.SpUtils
import com.ted.lib_voice.manager.VoiceManager
import kotlinx.android.synthetic.main.activity_voice_setting.*

@Route(path = ARouterHelper.PATH_VOICE_SETTING)
class VoiceSettingActivity : BaseActivity() {

    private val mList: ArrayList<String> = ArrayList()
    private var mTtsPeopleIndex: Array<String>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_voice_setting
    }

    override fun getTitleText(): String {
        return getString(com.ted.lib_base.R.string.app_title_voice_setting)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        //默认值
        //bar_voice_speed.progress =  SpUtils.getInt("tts_speed", 5)
        //bar_voice_volume.progress =   SpUtils.getInt("tts_volume", 5)
        bar_voice_speed.progress =  5
        bar_voice_volume.progress = 5

                //设置最大值
        bar_voice_speed.max = 15
        bar_voice_volume.max = 15

        initData()
        initListener()
        initPeopleView()

        btn_test.setOnClickListener {
            VoiceManager.ttsStart(getString(R.string.text_test_tts))
        }
    }

    //初始化数据
    private fun initData() {
        val mTtsPeople = resources.getStringArray(R.array.TTSPeople)

        mTtsPeopleIndex = resources.getStringArray(R.array.TTSPeopleIndex)
        mTtsPeople.forEach { mList.add(it) }
    }

    //初始化发音人列表
    private fun initPeopleView() {
        rv_voice_people.layoutManager = LinearLayoutManager(this)
        rv_voice_people.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        rv_voice_people.adapter =
            CommonAdapter(mList, object : CommonAdapter.OnBindDataListener<String> {
                override fun onBindViewHolder(
                    model: String,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    viewHolder.setText(R.id.mTvPeopleContent, model)
                    viewHolder.itemView.setOnClickListener {
                        mTtsPeopleIndex?.let {
                            VoiceManager.setPeople(it[position])
                        }
                        SpUtils.getInt("tts_people", position)
                    }
                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.layout_tts_people_list
                }
            })
    }

    //初始化监听
    private fun initListener() {
        //监听
        bar_voice_speed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                VoiceManager.setVoiceSpeed(progress.toString())
                SpUtils.putInt("tts_speed", progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        //监听
        bar_voice_volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                VoiceManager.setVoiceVolume(progress.toString())
                SpUtils.putInt("tts_volume", progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

}