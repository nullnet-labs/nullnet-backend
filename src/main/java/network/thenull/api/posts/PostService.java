package network.thenull.api.posts;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {
	
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
