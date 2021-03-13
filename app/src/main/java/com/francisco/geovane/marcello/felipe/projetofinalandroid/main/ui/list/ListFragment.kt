package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.AnalyticsUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics

class ListFragment : Fragment() {

    private var bundle: Bundle = Bundle()
    private lateinit var analytics: FirebaseAnalytics

    private var appId: String = BuildConfig.APP_ID
    private var pageId: String = "List"

    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        analytics = FirebaseAnalytics.getInstance(context)
        AnalyticsUtils.setPageData(analytics, bundle, appId, pageId)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val textView: TextView = root.findViewById(R.id.text_dashboard)
        listViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val btnAdd: FloatingActionButton = root.findViewById(R.id.btn_add)

        btnAdd.setOnClickListener {

            AnalyticsUtils.setClickData(analytics, bundle, appId, pageId,"AddNew")
        }

        return root
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}