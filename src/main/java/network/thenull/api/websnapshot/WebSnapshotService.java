package network.thenull.api.websnapshot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WebSnapshotService {
	
	public String getPageTitle(String url) throws IOException {
		return Jsoup.connect(url)
			.userAgent(url)
			.get()
			.title()
		;
	}
	
	public byte[] getPageScreenshot(String url) {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(
				new BrowserType.LaunchOptions().setHeadless(true)
			);
			
			Page page = browser.newPage();
			page.navigate(
				url,
				new Page.NavigateOptions().setTimeout(5000)
			);
			
			byte[] screenshot = page.screenshot(
				new Page.ScreenshotOptions()
					.setFullPage(true)
			);
			
			browser.close();
			
			return screenshot;
		}
	}
}
