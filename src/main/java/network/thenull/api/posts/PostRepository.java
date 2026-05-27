package network.thenull.api.posts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import network.thenull.api.common.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Long> findIdByDomain(String domain);
}
