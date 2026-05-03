package org.example.kurki.web.repository;

import org.example.kurki.web.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDetailsRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAllByIsBannedFalse(Pageable pageable);

}
