package com.dominik.intro.project.db.repo;

import com.dominik.intro.project.db.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAllByUserId(int userId);
    Article findById(int articleId);
}
