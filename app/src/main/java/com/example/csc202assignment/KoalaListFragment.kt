package com.example.csc202assignment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Update
import com.example.csc202assignment.databinding.KoalaListFragmentBinding
import com.example.csc202assignment.databinding.KoalaListRowBinding
import kotlinx.coroutines.launch
import java.util.*

class KoalaListFragment : Fragment() {

    private lateinit var koalaRecyclerView : RecyclerView
    private var _binding: KoalaListFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val koalaListViewModel : KoalaListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.koala_list_fragment,menu)
    }

    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_koala -> {
                val koala = Koala()
                koalaListViewModel.addKoala(koala)
                findNavController().navigate(KoalaListFragmentDirections.showKoalaDetail(koala.id))
                true
            }
            R.id.help_koala -> {findNavController().navigate(KoalaListFragmentDirections.actionKoalaListFragmentToWebView())
            true}
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                koalaListViewModel.koalas.collect { koalas ->
                    binding.koalaRecyclerView.adapter =
                        KoalaAdapter(koalas) { koalaId ->
                            findNavController().navigate(KoalaListFragmentDirections.showKoalaDetail(koalaId))
                        }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = KoalaListFragmentBinding.inflate(
            inflater, container,
            false
        )
        binding.koalaRecyclerView.layoutManager =
            LinearLayoutManager(context)
        return binding.root
    }

    companion object {
        fun newInstance(): KoalaListFragment{
            return KoalaListFragment()
        }
    }
    private inner class KoalaHolder(private val binding: KoalaListRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(koala: Koala, onKoalaClicked: (koalaId: UUID) -> Unit) {
            binding.koalaTitle.text = koala.title
            binding.koalaDate.text = koala.date.toString()
            binding.koalaPlace.text = koala.place
            binding.root.setOnClickListener {
                onKoalaClicked(koala.id)
            }

        }
    }

    private inner class KoalaAdapter(
        val koalas: List<Koala>,
        val onKoalaClicked: (crimeId : UUID) -> Unit):RecyclerView.Adapter<KoalaHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): KoalaHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = KoalaListRowBinding.inflate(inflater, parent, false)
            return KoalaHolder(binding)
        }

        override fun getItemCount() = koalas.size


        override fun onBindViewHolder(holder: KoalaHolder, position: Int) {
            val koala = koalas[position]
            holder.bind(koala, onKoalaClicked)
        }


    }

}