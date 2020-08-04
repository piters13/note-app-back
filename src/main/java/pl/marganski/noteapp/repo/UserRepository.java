/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.marganski.noteapp.repo;

import org.springframework.data.repository.CrudRepository;
import pl.marganski.noteapp.models.UserEntity;

/**
 *
 * @author Margański Piotr
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
