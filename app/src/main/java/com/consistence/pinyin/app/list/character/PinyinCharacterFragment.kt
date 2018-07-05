package com.consistence.pinyin.app.list.character

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.consistence.pinyin.R
import com.consistence.pinyin.ViewModelFactory
import com.consistence.pinyin.api.PinyinEntity
import com.consistence.pinyin.app.list.PinyinListFragment
import com.consistence.pinyin.app.list.PinyinListIntent
import com.consistence.pinyin.app.list.PinyinListLayout
import com.consistence.pinyin.kit.Interaction
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class PinyinCharacterFragment : PinyinListFragment() {

    @Inject lateinit var model: ViewModelFactory<PinyinCharacterViewModel>

    @BindView(R.id.pinyin_character_fragment_recyclerview)
    lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PinyinCharacterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.pinyin_character_fragment, container, false)
        ButterKnife.bind(this, view)

        val adapterInteraction: PublishSubject<Interaction<PinyinEntity>> = PublishSubject.create()
        adapter = PinyinCharacterAdapter(context!!, adapterInteraction)
        recyclerView.adapter = adapter

        return view
    }

    override fun intents(): Observable<PinyinListIntent> = Observable.merge(
            super.intents(),
            adapter.interaction.map({
                when (it.id) {
                    R.id.pinyin_list_audio_button -> PinyinListIntent.PlayAudio(it.data.audioSrc!!)
                    else -> PinyinListIntent.SelectItem(it.data)
                }
            })
    )

    override fun inject() {
        AndroidSupportInjection.inject(this)
    }

    override fun layout(): PinyinListLayout = this

    override fun model(): PinyinCharacterViewModel = getViewModel(model)

    override fun populate(pinyin: List<PinyinEntity>) {
        adapter.clear()
        adapter.populate(pinyin)
    }

    override fun showError() {
    }

    companion object {
        fun newInstance() = PinyinCharacterFragment()
    }
}