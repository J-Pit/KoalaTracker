package com.example.csc202assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KoalaListFragment : Fragment() {
    private lateinit var koalaRecyclerView : RecyclerView
    private val KoalaListViewModel : KoalaListViewModel by lazy {
        ViewModelProvider(this)[com.example.csc202assignment.KoalaListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.koala_list_fragment, container, false)
        koalaRecyclerView = view.findViewById(R.id.koala_recycler_view) as RecyclerView
        koalaRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }
    companion object {
        fun newInstance(): KoalaListFragment{
            return KoalaListFragment()
        }
    }
}