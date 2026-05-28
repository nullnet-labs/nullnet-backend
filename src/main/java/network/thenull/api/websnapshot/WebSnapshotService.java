package network.thenull.api.websnapshot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WebSnapshotService {
	
	public String getPageTitle(String url) throws IOException {
		return Jsoup.connect(url).get().title();
	}
}
