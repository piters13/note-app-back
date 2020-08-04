/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.marganski.noteapp.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.marganski.noteapp.config.JwtTokenUtil;
import pl.marganski.noteapp.models.JwtRequest;
import pl.marganski.noteapp.models.JwtResponse;
import pl.marganski.noteapp.models.Note;
import pl.marganski.noteapp.models.UserEntity;
import pl.marganski.noteapp.services.JwtUserDetailsService;
import pl.marganski.noteapp.services.NotesService;

/**
 *
 * @author Margański Piotr
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class NoteAppController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private NotesService notesService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
            );

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserEntity user) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    @GetMapping("notes")
    public ResponseEntity<Set<Note>> getUserNotes(Principal principal) {
        UserEntity user = userDetailsService.getUserByUsername(principal.getName());

        if (user != null) {
            return ResponseEntity.ok(user.getNotes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("notes")
    public ResponseEntity<?> createNote(@RequestBody Note note, Principal principal) {
        UserEntity user = userDetailsService.getUserByUsername(principal.getName());
        note.setCreationDate(LocalDateTime.now());

        return ResponseEntity.ok(notesService.save(user, note));
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<?> updateNote(@RequestBody Note note, @PathVariable Long id, Principal principal) {
        Optional<Note> noteOptional = notesService.findById(id);
        UserEntity user = userDetailsService.getUserByUsername(principal.getName());

        if (!noteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(notesService.update(noteOptional, note, user));
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        notesService.delete(id);

        return ResponseEntity.ok().build();
    }
}
