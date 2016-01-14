package com.jin.u2f.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jin.u2f.data.U2FUser;

public interface UserRepository extends MongoRepository<U2FUser, String> {

	public List<U2FUser> findByUsername(String username);

	public U2FUser findByKeyHandle(String keyHandle);
}