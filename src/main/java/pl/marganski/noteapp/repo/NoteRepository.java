/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.marganski.noteapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.marganski.noteapp.models.Note;

/**
 *
 * @author Margański Piotr
 */
public interface NoteRepository extends JpaRepository<Note, Long> {

}
