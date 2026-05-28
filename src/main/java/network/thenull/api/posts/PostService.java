package network.thenull.api.posts;

import java.io.IOException;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import network.thenull.api.posts.dto.PostBrowsingPageDto;
import network.thenull.api.websnapshot.WebSnapshotService;

@Service
@Slf4j
public class PostService {
	
	private final PostRepository postRepo;
	private final WebSnapshotService webSnapshotService;
	
	public PostService(PostRepository postRepo, WebSnapshotService webSnapshotService) {
		this.postRepo = postRepo;
		this.webSnapshotService = webSnapshotService;
	}
	
	
	
	public Long getExistingPostId(String domain) {
		return postRepo.findIdByDomain(domain).orElse(null);
	}
	
	public String getPageTitle(String url) throws IOException {
		return webSnapshotService.getPageTitle(url);
	}
	
	public byte[] getPageScreenshot(String url) {
		return webSnapshotService.getPageScreenshot(url);
	}
	
	public PostBrowsingPageDto getPostBrowsingPageData(Integer page, String search) {
		String[]searchTokens = search.split(" ");
		
		System.out.println("Page:");
		System.out.println(page);
		System.out.println("Search:");
		System.out.println(search);
		System.out.println("Search tokens:");
		
		for(String token : searchTokens) {
			System.out.println(token);
		}
		
		return null;
	}
}
