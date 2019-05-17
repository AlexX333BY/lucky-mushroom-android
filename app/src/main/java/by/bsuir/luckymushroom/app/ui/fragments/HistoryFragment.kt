package by.bsuir.luckymushroom.app.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.ui.adapters.HistoryRecyclerViewAdapter
import by.bsuir.luckymushroom.app.viewmodels.AppViewModel
import by.bsuir.luckymushroom.app.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {
    private lateinit var historyModel: HistoryViewModel
    private lateinit var appModel: AppViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyModel = activity?.run {
            ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        appModel = activity?.run {
            ViewModelProviders.of(this).get(AppViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyModel.launchFetchData(appModel)
        return inflater.inflate(R.layout.fragment_history, container, false)
            .apply {
                activity?.let {
                    historyModel.getRecognitionRequests()
                        .observe(it, Observer { recognitionRequests ->
                            recognitionRequests?.let {
                                viewManager = LinearLayoutManager(activity)
                                viewAdapter = HistoryRecyclerViewAdapter(
                                    recognitionRequests
                                )
                                recyclerView = findViewById<RecyclerView>(
                                    R.id.recycleview_history
                                ).apply {
                                    setHasFixedSize(true)
                                    layoutManager = viewManager
                                    adapter = viewAdapter
                                }
                            }
                        })
                }
            }
    }

}
