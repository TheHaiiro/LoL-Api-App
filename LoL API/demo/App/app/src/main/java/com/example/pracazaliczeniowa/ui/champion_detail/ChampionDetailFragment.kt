package com.example.pracazaliczeniowa.ui.champion_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pracazaliczeniowa.databinding.FragmentChampionDetailBinding

/**
 * A fragment that displays the details of a single champion.
 * It gets the champion ID from navigation arguments and uses the ViewModel to fetch
 * and display the corresponding data.
 */
class ChampionDetailFragment : Fragment() {

    private var _binding: FragmentChampionDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChampionDetailViewModel by viewModels()
    private val args: ChampionDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChampionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChampionDetails(args.championId)

        viewModel.champion.observe(viewLifecycleOwner) { champion ->
            champion?.let {
                binding.championDetailName.text = it.name
                binding.championDetailTitle.text = it.title
                binding.championDetailBlurb.text = it.blurb

                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .circleCrop()
                    .into(binding.championDetailImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
