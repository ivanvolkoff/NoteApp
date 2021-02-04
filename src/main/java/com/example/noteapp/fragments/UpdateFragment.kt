package com.example.noteapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentUpdateBinding
import com.example.noteapp.model.Note
import com.example.noteapp.toast
import com.example.noteapp.viewmodel.NoteViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()
    private lateinit var currentNote: Note

    private lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.etNoteUpdateTitle.setText(currentNote.noteTitle)
        binding.etNoteUpdateBody.setText(currentNote.noteBody)

        binding.fabUpdate.setOnClickListener {
            val title = binding.etNoteUpdateTitle.text.toString().trim()
            val body = binding.etNoteUpdateBody.text.toString().trim()

            if (title.isNotEmpty()) {
                val note = Note(currentNote.id, title, body)
                noteViewModel.updateNote(note)
                activity?.toast("Note Updated!")
                view.findNavController().navigate(
                    R.id.action_updateFragment_to_homeFragment
                )
            } else {
                activity?.toast("Please enter title name!")
            }

        }


    }

    private fun deleteNote() {

        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to delete this note?")
            setPositiveButton("DELETE"){
                _,_ ->
                noteViewModel.deleteNote(currentNote)
                view?.findNavController()?.navigate(
                    R.id.action_updateFragment_to_homeFragment
                )
            }
            setNegativeButton("CANCEL", null)
        }.create().show()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.update_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_menu ->
                deleteNote()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}