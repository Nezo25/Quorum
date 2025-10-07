package pack.treefrog.system.spring.security.qourum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.treefrog.system.spring.security.qourum.models.Tweet;

public interface TweetRepository extends JpaRepository <Tweet, Long> {
}
