package com.example.noteapp.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.model.Note
import com.example.noteapp.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.setOnQueryTextListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel

        setUpRecyclerView()
        binding.fabNewNote.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )

            setHasFixedSize(true)
            adapter = noteAdapter

        }

        activity?.let {
            noteViewModel.getAllNote().observe(viewLifecycleOwner, { note ->
                noteAdapter.differ.submitList(note)

                updateUI(note)
            })

        }

    }

    private fun updateUI(note: List<Note>) {
        if (note.isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvNoNotes.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.tvNoNotes.visibility = View.VISIBLE
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun searchNotes(query: String) {
        val searchQuery = "%$query%"
        noteViewModel.searchNote(searchQuery).observe(this, { list ->

            noteAdapter.differ.submitList(list)
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!= null){
            searchNotes(query)
        }

        return true

    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!= null){
            searchNotes(newText)
        }

        return true

    }


}