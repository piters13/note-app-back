/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.marganski.noteapp.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marganski.noteapp.models.Note;
import pl.marganski.noteapp.models.UserEntity;
import pl.marganski.noteapp.repo.NoteRepository;
import pl.marganski.noteapp.repo.UserRepository;

/**
 *
 * @author Margański Piotr
 */
@Service
public class NotesService {
    @Autowired
    NoteRepository noteRepository;
    
    @Autowired
    UserRepository userRepository;
    
    public List<Note> getAll() {
        return noteRepository.findAll();
    }
    
    public Note save(UserEntity user, Note note) {
        Set<Note> notes = new HashSet<>();
        UserEntity _user = new UserEntity();

        note.setUser(user);

        Note _note = noteRepository.save(note);
        notes.add(_note);
        _user.setNotes(notes);

        return _note;
    }
    
    public Note update(Optional<Note> noteOptional, Note note, UserEntity user) {
        Note _note = noteOptional.get();
        _note.setTitle(note.getTitle());
        _note.setContent(note.getContent());
        _note.setUser(user);

        return noteRepository.save(_note);
    }
    
    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }
    
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }
    
}
