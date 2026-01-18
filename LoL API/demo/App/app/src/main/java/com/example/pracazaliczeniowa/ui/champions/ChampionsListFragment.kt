package com.example.pracazaliczeniowa.ui.champions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pracazaliczeniowa.R
import com.example.pracazaliczeniowa.databinding.FragmentChampionsListBinding

/**
 * A fragment that displays a list of champions.
 * It includes a search bar to filter the list and handles navigation to the detail screen.
 */
class ChampionsListFragment : Fragment() {

    private var _binding: FragmentChampionsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChampionsViewModel
    private lateinit var championsAdapter: ChampionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChampionsListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(ChampionsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    /**
     * Sets up the RecyclerView, including its adapter, layout manager, and item decorator.
     */
    private fun setupRecyclerView() {
        championsAdapter = ChampionsAdapter { champion ->
            val action = ChampionsListFragmentDirections.actionChampionsListFragmentToChampionDetailFragment(champion.id)
            findNavController().navigate(action)
        }

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.list_separator)?.let {
            divider.setDrawable(it)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = championsAdapter
            addItemDecoration(divider)
        }
    }

    /**
     * Observes the LiveData from the ViewModel and submits the list to the adapter.
     */
    private fun observeViewModel() {
        viewModel.champions.observe(viewLifecycleOwner) {
            championsAdapter.submitList(it)
        }
    }

    /**
     * Sets up the search view and its text listener to filter the list.
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchChampions(newText)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
